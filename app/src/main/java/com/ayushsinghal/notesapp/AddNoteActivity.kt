package com.ayushsinghal.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.ayushsinghal.notesapp.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noteDescriptionID.requestFocus()

        binding.backButton.setOnClickListener()
        {
            saveNote()
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveNote()
                    finish()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }

//        // Add the onBackPressedCallback to the activity's onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun saveNote() {
        val dbRef = Firebase.database.getReference("Notes").child(Firebase.auth.currentUser!!.uid)

        if ((binding.noteTitleID.text.toString() == "") && (binding.noteDescriptionID.text.toString() == "")) {
        } else {
            val userID = Firebase.auth.currentUser?.uid
            val noteID = dbRef.child(userID!!).push().key
            val dataModel = DataModel(
                binding.noteTitleID.text.toString(),
                binding.noteDescriptionID.text.toString(),
                userID,
                noteID!!
            )
            dbRef.child(noteID).setValue(dataModel)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        Toast.makeText(this, "Note Added Successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Note can not be added", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}