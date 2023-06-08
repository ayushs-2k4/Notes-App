package com.ayushsinghal.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.ayushsinghal.notes.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linearProgressBarSignUpPage.isVisible = false

        binding.signUpButton.setOnClickListener()
        {
            if ((binding.emailSignUpEditText.editText?.text.toString() == "") || (binding.passwordSignUpEditText.editText?.text.toString() == "")) {
                Toast.makeText(this, "Email and Password can not be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.linearProgressBarSignUpPage.isVisible = true
                Firebase.auth.createUserWithEmailAndPassword(
                    binding.emailSignUpEditText.editText?.text.toString(),
                    binding.passwordSignUpEditText.editText?.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
//                        Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
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
                    binding.linearProgressBarSignUpPage.isVisible = false
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