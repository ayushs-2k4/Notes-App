package com.ayushsinghal.notesapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import com.ayushsinghal.notesapp.databinding.ActivityExpandedNoteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog

class ExpandedNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpandedNoteBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandedNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (binding.expandedNoteDescriptionID.text.isEmpty()) {
            binding.expandedNoteDescriptionID.visibility = View.GONE
        } else {
            binding.expandedNoteDescriptionID.visibility = View.VISIBLE
            binding.expandedNoteDescriptionID.maxLines = Integer.MAX_VALUE
        }

        binding.expandedNoteTitleID.setText(intent.getStringExtra("TITLE"))
        binding.expandedNoteDescriptionID.setText(intent.getStringExtra("DESCRIPTION"))

        binding.deleteButton.setOnClickListener() {
            val noteId = intent.getStringExtra("NOTE ID")
            val noteRef =
                Firebase.database.getReference("Notes").child(Firebase.auth.currentUser!!.uid)
                    .child(noteId!!)
//            noteRef.removeValue()
//            finish()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Do you want to delete this note?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                val dbRef =
                    Firebase.database.getReference("Notes").child(Firebase.auth.uid!!)
                        .child(noteId)
                Toast.makeText(this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show()
                dbRef.removeValue()
                dialog.cancel()
                finish()
            })

            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            val alert = builder.create()
            alert.show()

        }

        binding.backButton.setOnClickListener() {
            saveUpdatedNote()
            finish()
        }

        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    saveUpdatedNote()
                    finish()
                }
            }

        // Add the onBackPressedCallback to the activity's onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun saveUpdatedNote() {
        val dbRef = Firebase.database.getReference("Notes")
            .child(Firebase.auth.currentUser!!.uid)

        if ((binding.expandedNoteTitleID.text.toString() == "") && (binding.expandedNoteDescriptionID.text.toString() == "")) {
//                        binding.errorTextView.text = "Both Title and Description can not be empty!!!"
        } else {
            val userID = Firebase.auth.currentUser?.uid
            val noteID = intent.getStringExtra("NOTE ID")
            val dataModel = DataModel(
                binding.expandedNoteTitleID.text.toString(),
                binding.expandedNoteDescriptionID.text.toString(),
                userID!!,
                noteID!!
            )
            dbRef.child(noteID).setValue(dataModel)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
//                        Toast.makeText(this@ExpandedNoteActivity, "Note Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@ExpandedNoteActivity,
                            "Note can not be Updated",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }
    }
}