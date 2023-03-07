package com.example.registrationtemplate

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {

    val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        Log.d(TAG, "onCreate: onCreate() is called here.. ")

        listeners()
    }

    private fun listeners() {

//        Log.d(TAG, "createAccount: listeners() is called here.. ")
//
//        edt_full_name.setOnClickListener {
//            Log.d(TAG, "Enter full name is clicked ")
//        }
//
//        edt_enter_name.setOnClickListener {
//            Log.d(TAG, "listeners: enter-name is clicked... ")
//        }
//
//        edt_enter_email.setOnClickListener {
//            Log.d(TAG, "listeners: enter-mail is clicked... ")
//        }
//
//        edt_password.setOnClickListener {
//            Log.d(TAG, "listeners: password is clicked... ")
//        }

        btn_register.setOnClickListener {
            Log.d(TAG, "listeners: btn-register is clicked... ")
            createAccount()
        }
    }

    private fun createAccount() {

        Log.d(TAG, "createAccount: createAccount() is called here.. ")
        val fullName = edt_full_name.text.toString()
        val userName = edt_enter_name.text.toString()
        val email = edt_enter_email.text.toString()
        val password = edt_password.text.toString()

        // input validation..
        when {
            TextUtils.isEmpty(fullName) -> Toast.makeText(
                this@SignUpActivity,
                "Full name-123 is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(userName) -> Toast.makeText(
                this@SignUpActivity,
                "Username is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(email) -> Toast.makeText(
                this@SignUpActivity,
                "Email is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                this@SignUpActivity,
                "Password is required",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val progressDialog = ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUserInto(fullName, userName, email, progressDialog)

                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Error + $message",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }


    // F/B Database to store authenticated user credentials
    // (user data will be passed as an object with HashMap)
    // User will be taken to the second activity upon successful verification of user credentials

    // Go to SignInActivity for the next step [auto-sign-in once registered]
    // The length of the password has got to be 6 or more
    private fun saveUserInto(
        fullName: String,
        userName: String,
        email: String,
        progressDialog: ProgressDialog
    ) {

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName
        userMap["username"] = userName
        userMap["email"] = email
        userMap["bio"] = "I am now signed-up can you see me Luxolis? $currentUserID"

        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@SignUpActivity,
                        "Account has been created successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(this@SignUpActivity, SecondActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this@SignUpActivity, "Error + $message", Toast.LENGTH_LONG)
                        .show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }

    }


    private fun navigateToSecondActivity() {
        Log.d(TAG, "navigateToSecondActivity: navigateToSecondActivity() is called here.. ")
        val intenter = Intent(this@SignUpActivity, SecondActivity::class.java)
        startActivity(intenter)

    }


}