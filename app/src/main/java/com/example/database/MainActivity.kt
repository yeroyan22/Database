package com.example.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.example.database.database.ArticleDatabase
import kotlinx.android.synthetic.main.activity_main.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.database.Article
import com.example.database.model.ArticlesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var model: ArticleViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerViewAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter

        val dataSource = ArticleDatabase.getInstance(application).articleDao()

        val viewModelFactory = ArticleViewModelFactory(dataSource, application = MyApplication())

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(ArticleViewModel::class.java)

        viewModel.getAllPosts().observe(this, Observer { articles ->
            recyclerAdapter.setData(articles)
        })

        val apiInterface = ApiInterface.create().getArticles()

        apiInterface.enqueue(object : Callback<ArticlesData> {
            override fun onResponse(call: Call<ArticlesData>, response: Response<ArticlesData>) {
                if (response?.body() != null)
                    recyclerAdapter.setData(response.body()!!.response.articles)            }

            override fun onFailure(call: Call<ArticlesData>, t: Throwable) {
            }
        })
    }
}
