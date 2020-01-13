package com.example.database

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView

import kotlinx.android.synthetic.main.recyclerview_item.view.*
import com.example.database.database.Article
import com.facebook.drawee.view.SimpleDraweeView

class RecyclerViewAdapter(
    context: Context,
    var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var articles: MutableList<Article> = mutableListOf()



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

        fun bind(article: Article,clickListener: OnItemClickListener){
            title.text = article.title
            image.setImageURI(article.image)
            category.text = article.category
            itemView.setOnClickListener {
                clickListener.onItemClicked(article)
            }
        }
    }

    fun setData(newData: List<Article>) {
        this.articles.clear()
        articles.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: List<Article>){
        this.articles.addAll(newData)
        notifyDataSetChanged()
    }

}
interface OnItemClickListener{
    fun onItemClicked(article: Article)
}
