package com.ayushsinghal.notes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.ayushsinghal.notes.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

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

        // Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInClient = googleSignInClient.signInIntent

        auth = Firebase.auth

        binding.googleLogoImage.setOnClickListener()
        {
//            Toast.makeText(this, "Clicked on Google Logo - SignIn Activity", Toast.LENGTH_SHORT).show()

            binding.linearProgressBarSignUpPage.isVisible = true
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
                            finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG)
                                .show()
                            binding.linearProgressBarSignUpPage.isVisible = false
                        }
                    }
                }
            } else {
                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
                binding.linearProgressBarSignUpPage.isVisible = false
            }
        }
}