package com.example.database

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.model.ArticlesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.net.Uri
import com.example.database.EndlessScroll as EndlessScroll
import android.net.ConnectivityManager
import com.example.database.database.*
import java.util.*
import kotlin.collections.ArrayList
import android.app.NotificationManager
import android.app.Notification
import android.app.PendingIntent
import androidx.core.app.NotificationCompat


class MainActivity : AppCompatActivity(), OnItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerViewAdapter
    lateinit var endlessScroll: EndlessScroll
    lateinit var layoutManager: LinearLayoutManager
    var page: Int = 1
    lateinit var dataSource: ArticleDao
    lateinit var arrayList: ArrayList<Article>
    lateinit var idSource: LikedIdDao
    lateinit var likedIdList: List<LikedId>
    lateinit var likedId: LikedId
    lateinit var deletedId: DeletedId
    lateinit var deletedSource: DeletedIdDao
    lateinit var deletedIdList: List<DeletedId>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerViewAdapter(this, this)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter
        arrayList = ArrayList()
        dataSource = ArticleDatabase.getInstance(application).articleDao()
        idSource = ArticleDatabase.getInstance(application).likedIdDao()
        deletedSource = ArticleDatabase.getInstance(application).deletedIdDao()

        val apiInterface = ApiInterface.create().getArticles(page)

        val cm =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting && arrayList != null) {
            apiInterface.enqueue(object : Callback<ArticlesData> {
                override fun onResponse(
                    call: Call<ArticlesData>,
                    response: Response<ArticlesData>
                ) {
                    if (response?.body() != null) {
                        dataSource.deleteAll()
                        likedIdList = idSource.getAll()
                        deletedIdList = deletedSource.getAll()
                        for (id in likedIdList) {
                            for (article in response.body()!!.response.articles) {
                                if (article.id == id.id) {
                                    article.isLiked = true
                                }
                            }
                        }
                        for (id in deletedIdList) {
                            for (article in response.body()!!.response.articles) {
                                if (article.id == id.id) {
                                    article.isDeleted = true
                                }
                            }
                        }
                        recyclerAdapter.setData(response.body()!!.response.articles)
                        dataSource.insertAllArticles(response.body()!!.response.articles)
                    }
                }

                override fun onFailure(call: Call<ArticlesData>, t: Throwable) {
                }
            })

        } else {

            fetchfromRoom()
        }



        endlessScroll = object : EndlessScroll(layoutManager) {
            override fun onLoadMore(pageNum: Int, recyclerView: RecyclerView) {
                var apiInterface = ApiInterface.create().getArticles(pageNum)
                apiInterface.enqueue(object : Callback<ArticlesData> {
                    override fun onResponse(
                        call: Call<ArticlesData>,
                        response: Response<ArticlesData>
                    ) {
                        if (response?.body() != null) {
                            likedIdList = idSource.getAll()
                            deletedIdList = deletedSource.getAll()
                            for (id in likedIdList) {
                                for (article in response.body()!!.response.articles) {
                                    if (article.id == id.id) {
                                        article.isLiked = true
                                    }
                                }
                            }
                            for (id in deletedIdList) {
                                for (article in response.body()!!.response.articles) {
                                    if (article.id == id.id) {
                                        article.isDeleted = true
                                    }
                                }
                            }
                            recyclerAdapter.addData(response.body()!!.response.articles)
                            dataSource.insertAllArticles(response.body()!!.response.articles)
                        }
                    }

                    override fun onFailure(call: Call<ArticlesData>, t: Throwable) {
                    }
                })
            }
        }

        recyclerView.addOnScrollListener(endlessScroll)

        val viewModelFactory = ArticleViewModelFactory(dataSource, application = MyApplication())

        val viewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(ArticleViewModel::class.java)

        viewModel.getAllPosts().observe(this, Observer { articles ->
            recyclerAdapter.setData(articles)
        })

        if (dataSource.getAll().size == 50) {
            updateIn30Seconds(page)
        } else {
            for (page in 1..dataSource.getAll().size / 50)
                updateIn30Seconds(page)
        }

        fun notification() {
            if (needToSendNotification) {
                val intent = Intent(this, MainActivity::class.java)
                val contentIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                val b = NotificationCompat.Builder(this)

                b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle("New Article")
                    .setContentText("Hey you have new articles")
                    .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent)


                val notificationManager =
                    this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, b.build())
            }
        }
    }

    var needToSendNotification: Boolean = false

    private fun fetchfromRoom() {

        val thread = Thread(Runnable {
            val articleList =
                ArticleDatabase.getInstance(application).articleDao().getAll()
            arrayList.clear()
            for (article in articleList) {
                article.title
                article.category
                article.fields!!.image
                arrayList.add(article)
            }
            runOnUiThread { recyclerAdapter.notifyDataSetChanged() }
        })
        thread.start()
    }

    private fun updateIn30Seconds(page: Int) {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val articleListDb =
                    ArticleDatabase.getInstance(application).articleDao().getAll()
                arrayList.clear()
                for (article in articleListDb) {
                    article.title
                    article.category
                    article.fields!!.image
                    arrayList.add(article)
                }
                var apiInterface = ApiInterface.create().getArticles(page)
                apiInterface.enqueue(object : Callback<ArticlesData> {
                    override fun onResponse(
                        call: Call<ArticlesData>,
                        response: Response<ArticlesData>
                    ) {
                        if (response?.body() != null) {
                            if (!articleListDb.containsAll(response.body()!!.response.articles)
                                && !response.body()!!.response.articles.containsAll(articleListDb)
                            ) {
                                dataSource.deleteAll()
                                likedIdList = idSource.getAll()
                                deletedIdList = deletedSource.getAll()
                                for (id in likedIdList) {
                                    for (article in response.body()!!.response.articles) {
                                        if (article.id == id.id) {
                                            article.isLiked = true
                                        }
                                    }
                                }
                                for (id in deletedIdList) {
                                    for (article in response.body()!!.response.articles) {
                                        if (article.id == id.id) {
                                            article.isDeleted = true
                                        }
                                    }
                                }
                                recyclerAdapter.setData(response.body()!!.response.articles)
                                dataSource.insertAllArticles(response.body()!!.response.articles)
                                needToSendNotification = true
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArticlesData>, t: Throwable) {
                    }
                })
            }
        }, 30, 1000)
    }


    override fun onItemClicked(article: Article) {
        val uri = Uri.parse(article.url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onLikedClicked(article: Article, liked: Boolean) {
        likedId = LikedId(article.id)
        if (liked) {
            idSource.insert(likedId)
        } else {
            idSource.delete(likedId)
        }

    }

    override fun onDeleteClicked(article: Article, deleted: Boolean) {
        deletedId = DeletedId(article.id)
        deletedSource.insert(deletedId)

    }


}


