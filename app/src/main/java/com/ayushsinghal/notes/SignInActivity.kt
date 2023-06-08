package com.ayushsinghal.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ayushsinghal.notes.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

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
                        startActivity(Intent(this, AllNotesActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    } else {
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


        // Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInClient = googleSignInClient.signInIntent

        auth = Firebase.auth

        binding.googleLogoImage.setOnClickListener()
        {
//            Toast.makeText(this, "Clicked on Google Logo - SignIn Activity", Toast.LENGTH_SHORT).show()
            binding.linearProgressBarSignInPage.isVisible = true
            launcher.launch(signInClient)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
//                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, AllNotesActivity::class.java))
                            this.finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG)
                                .show()
                            binding.linearProgressBarSignInPage.isVisible = false
                        }
                    }
                }
            } else {
                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
                binding.linearProgressBarSignInPage.isVisible = false
            }
        }
}