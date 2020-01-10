package com.serdang.PKK.Auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.serdang.PKK.AdminSite.AdminActivity
import com.serdang.PKK.R
import com.serdang.PKK.UserSite.UserActivity
import kotlinx.android.synthetic.main.login_layout_2.*

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 7

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout_2)

        signup_google.setOnClickListener {
            val i = Intent(this, RegistrActivity::class.java)
            startActivity(i)
        }

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_Login.setOnClickListener {
            val email = edt_input_mail.text.toString()
            val password = edt_input_password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Dimohon Untuk Mengisi Email dan Password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                Log.e("fungsi login", "Login gagal : ${it}")
            }

            if (email == "admin@mail.com" && password == "admin123") {
                Toast.makeText(this, "Login Admin Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val progressDialog = ProgressDialog(this)
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Login Di Proses")
                progressDialog.show()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) {
                            return@addOnCompleteListener
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            progressDialog.dismiss()
                        } else {
                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, UserActivity::class.java)
                            startActivity(intent)
                            progressDialog.dismiss()
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        Log.e("fungsi login", "Login Gagal : ${it.message}")
                        Toast.makeText(this, "Email atau Password ada yang salah", Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.dismiss()
                    }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e:ApiException) {
                Log.w("Login", "Google Sign In Gagal")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Log.d("Login", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Auth Failed", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }

                // ...
            }
    }

    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, UserActivity::class.java))
            Toast.makeText(this, "Hello ${user.email}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

}