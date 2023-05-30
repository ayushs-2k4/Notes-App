package com.ayushsinghal.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ayushsinghal.notesapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener()
        {
            if ((binding.emailSignUpEditText.text.toString() == "") || (binding.passwordSignUpEditText.text.toString() == "")) {
                Toast.makeText(this, "Email and Password can not be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Firebase.auth.createUserWithEmailAndPassword(
                    binding.emailSignUpEditText.text.toString(),
                    binding.passwordSignUpEditText.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AllNotesActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    } else {
//                        Toast.makeText(this, "SignUp UnSuccessful", Toast.LENGTH_SHORT).show()
                        val exception = result.exception
                        if (exception is FirebaseAuthException) {
                            val errorCode = exception.errorCode
                            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        binding.logInInsteadButton.setOnClickListener()
        {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }
    }
}