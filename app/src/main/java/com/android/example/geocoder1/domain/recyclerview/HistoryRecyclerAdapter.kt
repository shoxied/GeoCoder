package com.android.example.geocoder1.domain.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.geocoder1.R

class HistoryRecyclerAdapter(private val names: List<String>, private val listener: Listener): RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textHistory: TextView = itemView.findViewById(R.id.textHistory)

        fun click(listener: Listener){
            textHistory.setOnClickListener {
                listener.clickHistoryItem(itemViewText = textHistory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = names.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textHistory.text = names[position]
        holder.click(listener)
    }

    interface Listener{
        fun clickHistoryItem(itemViewText: TextView)
    }
}