package com.ayushsinghal.notes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ayushsinghal.notes.databinding.ActivityExpandedNoteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExpandedNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpandedNoteBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var title: String? = null
    private var description: String? = null


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

        title = intent.getStringExtra("TITLE")
        description = intent.getStringExtra("DESCRIPTION")

        binding.expandedNoteTitleID.setText(title)
        binding.expandedNoteDescriptionID.setText(description)

//        binding.deleteButton.setOnClickListener() {
//            val noteId = intent.getStringExtra("NOTE ID")
//            val noteRef =
//                Firebase.database.getReference("Notes").child(Firebase.auth.currentUser!!.uid)
//                    .child(noteId!!)
////            noteRef.removeValue()
////            finish()
//            val builder = MaterialAlertDialogBuilder(this)
//            builder.setTitle("Confirm Delete")
//            builder.setMessage("Do you want to delete this note?")
//            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
//                val dbRef =
//                    Firebase.database.getReference("Notes").child(Firebase.auth.uid!!)
//                        .child(noteId)
//                Toast.makeText(this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show()
//                dbRef.removeValue()
//                dialog.cancel()
//                finish()
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//
//            })
//
//            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
//                dialog.cancel()
//            })
//            val alert = builder.create()
//            alert.show()
//        }

//        binding.backButton.setOnClickListener() {
//            saveUpdatedNote()
//            finish()
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//        }

        binding.toolBar.setOnMenuItemClickListener()
        { menuItem ->

            when (menuItem.itemId) {
                R.id.delete -> {
                    val noteId = intent.getStringExtra("NOTE ID")
                    val noteRef =
                        Firebase.database.getReference("Notes")
                            .child(Firebase.auth.currentUser!!.uid)
                            .child(noteId!!)
                    val builder = MaterialAlertDialogBuilder(this)
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
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    })

                    builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                    val alert = builder.create()
                    alert.show()

                    true
                }

                R.id.share -> {
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(
                        Intent.EXTRA_TEXT, "$title" + "\n" + "$description"
                    )
                    intent.type = "text/plain"
                    val intentChooser = Intent.createChooser(intent, "Hi")
                    startActivity(intentChooser)
                    
                    true
                }

                else -> {
                    true
                }
            }
        }

        binding.toolBar.setNavigationOnClickListener {
            saveUpdatedNote()
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveUpdatedNote()
                    finish()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

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