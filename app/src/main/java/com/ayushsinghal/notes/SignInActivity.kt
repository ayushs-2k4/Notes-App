package com.ayushsinghal.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.ayushsinghal.notes.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linearProgressBarSignInPage.isVisible = false

        binding.signInButton.setOnClickListener()
        {
            if ((binding.emailSignInEditText.editText?.text.toString() == "") || (binding.passwordSignInEditText.editText?.text.toString() == "")) {
                Toast.makeText(this, "Email and Password can not be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.linearProgressBarSignInPage.isVisible = true
                Firebase.auth.signInWithEmailAndPassword(
                    binding.emailSignInEditText.editText?.text.toString(),
                    binding.passwordSignInEditText.editText?.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
//                        Toast.makeText(this, "SignIn Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AllNotesActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                    } else {
//                        Toast.makeText(this, "SignIn UnSuccessful", Toast.LENGTH_SHORT).show()
                        val exception = result.exception
                        if (exception is FirebaseAuthException) {
                            val errorCode = exception.errorCode
                            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    binding.linearProgressBarSignInPage.isVisible = false
                }
            }
        }

        binding.signUpInsteadButton.setOnClickListener()
        {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }
    }
}