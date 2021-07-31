package com.github.bookhub

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DashboardRecyclerView(private val context: Context, private val itemList: ArrayList<Books>) :
    RecyclerView.Adapter<DashboardRecyclerView.DashboardViewHolder>() {

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtRecyclerChildBookName)
        val imageView: ImageView = view.findViewById(R.id.imgRecyclerChildBookIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
//        Log.d("child view", "onCreateViewHolder : creating view")
//        Toast.makeText(context, "onCreateViewHolder", Toast.LENGTH_LONG).show()
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_child, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
//        Toast.makeText(context, "onCreateViewHolder", Toast.LENGTH_LONG).show()
        holder.textView.text = itemList[position].name
        Picasso.get().load(itemList[position].image).error(R.drawable.ic_book_foreground).into(holder.imageView)
    }

    override fun getItemCount(): Int {
//        Log.d("child view","getItemCount : ${itemList.size}")
        return itemList.size
    }

}