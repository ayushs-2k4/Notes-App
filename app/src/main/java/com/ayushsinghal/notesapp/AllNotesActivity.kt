package com.ayushsinghal.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayushsinghal.notesapp.databinding.ActivityAllNotesBinding
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

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val rvAdapter = RecyclerViewAdapter(this, list)
        binding.recyclerView.adapter = rvAdapter


        val dbRef = Firebase.database.getReference("Notes").child(Firebase.auth.currentUser!!.uid)

        var valueEventListener: ValueEventListener? = null
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (snap in snapshot.children) {
                    val data = snap.getValue(DataModel::class.java)
                    list.add(data!!)
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AllNotesActivity, "Error!!! $error", Toast.LENGTH_SHORT).show()
            }
        }
        dbRef.addValueEventListener(valueEventListener)

        binding.createNoteButton.setOnClickListener()
        {
            val intent = Intent(this, AddUpdateNoteActivity::class.java)
            intent.putExtra("MODE", "CREATE")
            startActivity(intent)
        }

        binding.signOutButton.setOnClickListener()
        {
            valueEventListener.let { listener -> dbRef.removeEventListener(listener) }
            Firebase.auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}