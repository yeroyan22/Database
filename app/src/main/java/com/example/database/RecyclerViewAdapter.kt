package com.example.database

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.database.database.*

import kotlinx.android.synthetic.main.recyclerview_item.view.*
import com.facebook.drawee.view.SimpleDraweeView

class RecyclerViewAdapter(
    context: Context,
    var itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var articles: MutableList<Article> = mutableListOf()
    private val TYPE_ARTICLE = 1
    private val TYPE_OTHER = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_ARTICLE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_second, parent, false)
            SecondViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ARTICLE) {
            val item = articles[position]
            holder.bind(item, itemClickListener)
        } else {
            val item = articles[position]
            (holder as SecondViewHolder).bindSecond(item, itemClickListener)
        }

    }

    override fun getItemCount(): Int = articles.size

    override fun getItemViewType(position: Int): Int {
        return if (articles[position].type == "article") {
            TYPE_ARTICLE
        } else {
            TYPE_OTHER
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.item_name
        private val image: SimpleDraweeView = itemView.item_image
        private val category: TextView = itemView.item_section
        private val delete: ImageView = itemView.image_delete
        private val like: ImageView = itemView.image_like


        fun bind(article: Article, clickListener: OnItemClickListener) {
            title.text = article.title
            image.setImageURI(article.fields!!.image)
            category.text = article.category
            itemView.setOnClickListener {
                clickListener.onItemClicked(article)
            }
            delete.setOnClickListener {
                removeAt(adapterPosition)
                clickListener.onDeleteClicked(article, true)
            }
            like.setOnClickListener {
                if (article.isLiked) {
                    like.setImageResource(R.drawable.like_disabled)
                    clickListener.onLikedClicked(article, false)
                } else {
                    like.setImageResource(R.drawable.like_enabled)
                    clickListener.onLikedClicked(article, true)
                }

            }
            if (article.isLiked) {
                like.setImageResource(R.drawable.like_enabled)
            } else {
                like.setImageResource(R.drawable.like_disabled)
            }

            if (article.isDeleted) {
                removeAt(adapterPosition)
            }
        }
    }

    open inner class SecondViewHolder(itemView: View) : ViewHolder(itemView) {
        private val title: TextView = itemView.item_name
        private val image: SimpleDraweeView = itemView.item_image
        private val category: TextView = itemView.item_section
        private val delete: ImageView = itemView.image_delete
        private val like: ImageView = itemView.image_like

        fun bindSecond(article: Article, clickListener: OnItemClickListener) {
            title.text = article.title
            image.setImageURI(article.fields!!.image)
            category.text = article.category
            itemView.setOnClickListener {
                clickListener.onItemClicked(article)
            }
            delete.setOnClickListener {
                removeAt(adapterPosition)
                clickListener.onDeleteClicked(article, true)
            }
            like.setOnClickListener {
                if (article.isLiked) {
                    like.setImageResource(R.drawable.like_disabled)
                    clickListener.onLikedClicked(article, false)
                } else {
                    like.setImageResource(R.drawable.like_enabled)
                    clickListener.onLikedClicked(article, true)
                }

            }
            if (article.isLiked) {
                like.setImageResource(R.drawable.like_enabled)
            } else {
                like.setImageResource(R.drawable.like_disabled)
            }

            if (article.isDeleted) {
                removeAt(adapterPosition)
            }
        }
    }


    fun setData(newData: List<Article>) {
        this.articles.clear()
        this.articles.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: List<Article>) {
        this.articles.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        articles.removeAt(position)
        val handler = Handler()
        handler.post {
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, articles.size)
        }

    }
}

interface OnItemClickListener {
    fun onItemClicked(article: Article)
    fun onLikedClicked(article: Article, liked: Boolean)
    fun onDeleteClicked(article: Article, deleted: Boolean)
}
