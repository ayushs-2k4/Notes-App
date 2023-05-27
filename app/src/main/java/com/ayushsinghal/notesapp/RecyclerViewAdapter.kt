package com.ayushsinghal.notesapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayushsinghal.notesapp.databinding.ActivityExpandedNoteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecyclerViewAdapter(val context: Context, val itemList: ArrayList<DataModel>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView = itemView.findViewById(R.id.noteTitle)

        var deleteButton: Button = itemView.findViewById(R.id.deletebtn)
        var updateButton: Button = itemView.findViewById(R.id.updatebtn)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitle.text = itemList[position].title

        holder.deleteButton.setOnClickListener()
        {
            val noteId = itemList[position].noteID
//            Toast.makeText(context, noteId, Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Do you want to delete this note?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                val dbRef =
                    Firebase.database.getReference("Notes").child(Firebase.auth.uid!!)
                        .child(noteId!!)
                Toast.makeText(context, "Note Deleted Successfully", Toast.LENGTH_SHORT).show()
                dbRef.removeValue()
                dialog.cancel()
            })

            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            val alert = builder.create()
            alert.show()

        }

        holder.updateButton.setOnClickListener()
        {
            val intent = Intent(context, AddUpdateNoteActivity::class.java)
            intent.putExtra("MODE", "UPDATE")
            intent.putExtra("TITLE", itemList[position].title)
            intent.putExtra("DESCRIPTION", itemList[position].description)
            intent.putExtra("NOTE ID", itemList[position].noteID)
            context.startActivity(intent)
        }

        holder.cardView.setOnClickListener()
        {
            val intent = Intent(context, ExpandedNoteActivity::class.java)
            intent.putExtra("TITLE", itemList[position].title)
            intent.putExtra("DESCRIPTION", itemList[position].description)
            context.startActivity(intent)
        }

    }
}