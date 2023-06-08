package com.ayushsinghal.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.ayushsinghal.notes.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noteDescriptionID.requestFocus()

//        testSaveNoteInBulk()

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
//                        Toast.makeText(this, "Note Added Successfully", Toast.LENGTH_SHORT)
//                            .show()
                    } else {
                        Toast.makeText(this, "Note can not be added", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun testSaveNoteInBulk() {
        val dbRef = Firebase.database.getReference("Notes").child(Firebase.auth.currentUser!!.uid)
        val userID = Firebase.auth.currentUser?.uid

        GlobalScope.launch {
            for (i in 1..50) {
                withContext(Dispatchers.IO) {
                    // Perform database or network operations in a background thread
                    val randomNumber = Random.nextInt(20, 100)
                    val randomNumber2 = Random.nextInt(200, 1000)
                    val noteID = dbRef.child(userID!!).push().key
                    val dataModel = DataModel(
                        getRandomString(randomNumber),
                        getRandomString(randomNumber2),
                        userID,
                        noteID!!
                    )
                    dbRef.child(noteID).setValue(dataModel)
                }
            }
        }

    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}