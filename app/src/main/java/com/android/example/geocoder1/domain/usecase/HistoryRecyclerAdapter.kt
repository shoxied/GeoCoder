package com.android.example.geocoder1.domain.usecase

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.geocoder1.R

class HistoryRecyclerAdapter(private val names: List<String>): RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textHistory: TextView = itemView.findViewById(R.id.textHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = names.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textHistory.text = names[position]
    }
}