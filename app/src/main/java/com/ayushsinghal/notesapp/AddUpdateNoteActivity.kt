package com.ayushsinghal.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ayushsinghal.notesapp.databinding.ActivityAddUpdateNoteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddUpdateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUpdateNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbRef = Firebase.database.getReference("Notes")

        if (intent.getStringExtra("MODE") == "CREATE") {
            binding.button.setOnClickListener()
            {
                if ((binding.editNoteTitleID.text.toString() == "") && (binding.editNoteDescriptionID.text.toString() == "")) {
                    binding.errorTextView.text = "Both Title and Description can not be empty!!!"
                } else {
                    val userID = Firebase.auth.currentUser?.uid
                    val noteID = dbRef.child(userID!!).push().key
                    val dataModel = DataModel(
                        binding.editNoteTitleID.text.toString(),
                        binding.editNoteDescriptionID.text.toString(),
                        userID,
                        noteID!!
                    )
                    dbRef.child(userID).child(noteID).setValue(dataModel)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                Toast.makeText(this, "Note Added Successfully", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this, AllNotesActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Note can not added", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }
        } else if (intent.getStringExtra("MODE") == "UPDATE") {
            binding.button.text = "Update Note"

            binding.editNoteTitleID.setText(intent.getStringExtra("TITLE"))
            binding.editNoteDescriptionID.setText(intent.getStringExtra("DESCRIPTION"))

            binding.button.setOnClickListener()
            {
                if ((binding.editNoteTitleID.text.toString() == "") && (binding.editNoteDescriptionID.text.toString() == "")) {
                    binding.errorTextView.text = "Both Title and Description can not be empty!!!"
                } else {
                    val userID = Firebase.auth.currentUser?.uid
                    val noteID = intent.getStringExtra("NOTE ID")
                    val dataModel = DataModel(
                        binding.editNoteTitleID.text.toString(),
                        binding.editNoteDescriptionID.text.toString(),
                        userID!!,
                        noteID!!
                    )
                    dbRef.child(userID).child(noteID).setValue(dataModel)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Note Updated Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                startActivity(Intent(this, AllNotesActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Note can not Updated", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }
        }
    }
}