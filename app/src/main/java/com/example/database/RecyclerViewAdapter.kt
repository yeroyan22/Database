package com.example.database

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import kotlinx.android.synthetic.main.recyclerview_item.view.*
import com.example.database.database.Article
import com.facebook.drawee.view.SimpleDraweeView

class RecyclerViewAdapter(
    context: Context) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
     var articles: List<Article> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = articles[position]
        holder.title.text = item.title
        holder.category.text = item.category
        holder.image.setImageURI(item.image)
    }

    override fun getItemCount(): Int = articles.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.item_name
        val image: SimpleDraweeView = itemView.item_image
        val category: TextView = itemView.item_section
    }

    fun setData(newData: List<Article>) {
        this.articles = newData
        notifyDataSetChanged()
    }
}
