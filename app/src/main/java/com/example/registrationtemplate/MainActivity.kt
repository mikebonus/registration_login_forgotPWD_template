package com.example.registrationtemplate

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_sign_up.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        btn_sign_in_login.setOnClickListener {
            Log.d(TAG, "onCreate: sign-in-button is clicked here.. ")
            loginUser()
        }

        tv_forgot_password.setOnClickListener {
            Log.d(TAG, "listeners: forgot-password is clicked here.. ")
            val intent = Intent(this@MainActivity, ResetPasswordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUser() {

        Log.d(TAG, "loginUser: loginUser() is clicked.. ")
        val email = edt_sign_in_email.text.toString()
        val password = edt_sign_in_password.text.toString()

        when {
            TextUtils.isEmpty(email) -> Toast.makeText(
                this@MainActivity,
                "Full name-123 is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                this@MainActivity,
                "Username is required",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val progressDialog = ProgressDialog(this@MainActivity)
                progressDialog.setTitle("SignIn")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        val intent = Intent(this@MainActivity, SecondActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    } else {
                        val message = task.exception!!.toString()
                        Toast.makeText(this@MainActivity, "Error + $message", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }


    // Auto sign-in once authentication credentials are verified
//    override fun onStart() {
//        super.onStart()
//        Log.d(TAG, "onCreate: sign-in Activity's onStart() is called..")
//
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            val intent = Intent(this@SignInActivity, SecondActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
//        }
//    }

}