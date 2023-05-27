package com.ayushsinghal.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import com.ayushsinghal.notesapp.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed(2300) {
            if (Firebase.auth.currentUser != null) {
                startActivity(Intent(this, AllNotesActivity::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
            finish()
        }
    }
}