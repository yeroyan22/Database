package com.example.database

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

import kotlinx.android.synthetic.main.recyclerview_item.view.*
import com.example.database.database.Article
import com.example.database.database.ArticleDao
import com.example.database.database.ArticleDatabase
import com.facebook.drawee.view.SimpleDraweeView

class RecyclerViewAdapter(
    context: Context,
    var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var articles: MutableList<Article> = mutableListOf()
    var likedArticles: MutableList<String> = mutableListOf()
    var viewModel: ArticleViewModel? = null




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = articles[position]
        holder.bind(item, itemClickListener)

    }

    override fun getItemCount(): Int = articles.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.item_name
        val image: SimpleDraweeView = itemView.item_image
        val category: TextView = itemView.item_section
        val delete: ImageView = itemView.image_delete
        val like: ImageView = itemView.image_like

        fun bind(article: Article,clickListener: OnItemClickListener){
            title.text = article.title
            image.setImageURI(article.fields!!.image)
            category.text = article.category
            itemView.setOnClickListener {
                clickListener.onItemClicked(article)
            }
            delete.setOnClickListener{
                removeAt(adapterPosition)
            }
            like.setOnClickListener{
                if(like.drawable.constantState == like.resources.getDrawable(R.drawable.like_disabled).constantState){
                    like.setImageResource(R.drawable.like_enabled)
                    likedArticles.add(articles[adapterPosition].id)
                    clickListener.onLikeClicked(article)
                }
                else{
                    like.setImageResource(R.drawable.like_disabled)
                    likedArticles.remove(articles[adapterPosition].id)
                }

            }
        }
    }

    fun addData(newData: List<Article>){
        this.articles.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        articles.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, articles.size)
    }

}
interface OnItemClickListener{
    fun onItemClicked(article: Article)
    fun onLikeClicked(article: Article)
}
