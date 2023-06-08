package com.ayushsinghal.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ayushsinghal.notes.databinding.ActivityAllNotesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllNotesBinding
    private lateinit var list: ArrayList<DataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        val rvAdapter = RecyclerViewAdapter(this, list)
        binding.recyclerView.adapter = rvAdapter
        rvAdapter.setOnCardClickListener(object : RecyclerViewAdapter.onCardClickListener {
            override fun onCardClick(position: Int) {
                val intent = Intent(this@AllNotesActivity, ExpandedNoteActivity::class.java)
                intent.putExtra("TITLE", list[position].title)
                intent.putExtra("DESCRIPTION", list[position].description)
                intent.putExtra("NOTE ID", list[position].noteID)

                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

        })


        val dbRef = Firebase.database.getReference("Notes").child(Firebase.auth.currentUser!!.uid)

        dbRef.keepSynced(true)

        var valueEventListener: ValueEventListener? = null
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.linearProgressBarAllNotesPage.isVisible = false
                list.clear()

                for (snap in snapshot.children) {
                    val data = snap.getValue(DataModel::class.java)
                    list.add(data!!)
                }
                list.reverse()
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AllNotesActivity, "Error!!! $error", Toast.LENGTH_SHORT).show()
            }
        }
        dbRef.addValueEventListener(valueEventListener)

        binding.createNoteButton.setOnClickListener()
        {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.signOutButton.setOnClickListener()
        {
            valueEventListener.let { listener -> dbRef.removeEventListener(listener) }
            Firebase.auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}