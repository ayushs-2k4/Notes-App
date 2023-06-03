package com.ayushsinghal.notesapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewAdapter(val context: Context, val itemList: ArrayList<DataModel>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onCardClick(position: Int)
    }

    fun setOnCardClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        var noteDescription: TextView = itemView.findViewById(R.id.noteDescription)

        var cardView: CardView = itemView.findViewById(R.id.cardView)

        init {
            cardView.setOnClickListener() {
                listener.onCardClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_view_design, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitle.text = itemList[position].title
        holder.noteDescription.text = itemList[position].description
    }
}