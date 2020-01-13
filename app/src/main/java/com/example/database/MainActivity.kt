package com.example.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.example.database.database.ArticleDatabase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.database.Article
import com.example.database.model.ArticlesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.example.database.database.ArticleDao
import kotlinx.android.synthetic.main.recyclerview_item.*
import com.example.database.EndlessScroll as EndlessScroll




class MainActivity : AppCompatActivity(), OnItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerViewAdapter
    lateinit var endlessScroll: EndlessScroll
    lateinit var layoutManager: LinearLayoutManager
    var page: Int = 1
    lateinit var dataSource: ArticleDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerViewAdapter(this, this)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter
        dataSource = ArticleDatabase.getInstance(application).articleDao()

        val viewModelFactory = ArticleViewModelFactory(dataSource, application = MyApplication())

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(ArticleViewModel::class.java)

        viewModel.getAllPosts().observe(this, Observer { articles ->
            recyclerAdapter.addData(articles)
        })

        var apiInterface = ApiInterface.create().getArticles(page)

        apiInterface.enqueue(object : Callback<ArticlesData> {
            override fun onResponse(call: Call<ArticlesData>, response: Response<ArticlesData>) {
                if (response?.body() != null)
                    recyclerAdapter.addData(response.body()!!.response.articles)
            }

            override fun onFailure(call: Call<ArticlesData>, t: Throwable) {
            }
        })

        endlessScroll = object : EndlessScroll(layoutManager) {
            override fun onLoadMore(pageNum: Int, recyclerView: RecyclerView) {
                    apiInterface = ApiInterface.create().getArticles(pageNum+1)
                    apiInterface.enqueue(object : Callback<ArticlesData> {
                        override fun onResponse(call: Call<ArticlesData>, response: Response<ArticlesData>) {
                            if (response?.body() != null)
                                recyclerAdapter.addData(response.body()!!.response.articles)
                        }

                        override fun onFailure(call: Call<ArticlesData>, t: Throwable) {
                        }
                    })
            }
        }

        recyclerView.addOnScrollListener(endlessScroll)

    }

    override fun onItemClicked(article: Article) {
        val uri = Uri.parse(article.url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onLikeClicked(article: Article) {
        dataSource.update(article)
    }

}


