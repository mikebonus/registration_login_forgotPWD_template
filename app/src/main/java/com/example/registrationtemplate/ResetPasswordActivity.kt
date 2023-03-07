package com.example.registrationtemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        Log.d(TAG, "onCreate: onCreate() is called from Reset-Password-Activity.. ")
        
        listeners()
    }

    private fun listeners() {

        btn_reset_email.setOnClickListener {
            val resetUserEmail = edt_reset_activity_email.text.toString()

            if (resetUserEmail == "") {
                Toast.makeText(applicationContext, "Please write your email", Toast.LENGTH_LONG).show()

            } else {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.sendPasswordResetEmail(resetUserEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Please check your email inbox", Toast.LENGTH_LONG).show()
                            val intenter = Intent(this@ResetPasswordActivity, MainActivity::class.java)
                            startActivity(intenter)

                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "Error + $message",
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }
                    }
            }
        }
    }
}