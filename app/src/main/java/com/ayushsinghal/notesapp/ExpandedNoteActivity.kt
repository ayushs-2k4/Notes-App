package com.ayushsinghal.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayushsinghal.notesapp.databinding.ActivityExpandedNoteBinding

class ExpandedNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpandedNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandedNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.expandedNoteTitleID.text = intent.getStringExtra("TITLE")
        binding.expandedNoteDescriptionID.text = intent.getStringExtra("DESCRIPTION")
    }
}