package com.eteam.epotreba.presentation.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.eteam.epotreba.R
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eteam.epotreba.databinding.ActivitySignInBinding
import com.eteam.epotreba.domain.services.PhoneAuthServices
import com.google.firebase.auth.*


class SignInActivity : AppCompatActivity() {

    private var auth = FirebaseAuth.getInstance()
    private val phoneAuth: PhoneAuthServices = PhoneAuthServices(this, auth)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBut = binding.butConfirm

        val phoneText = binding.phoneText
        val phoneInput = binding.phoneField

        val codeText = binding.codeText
        val codeInput = binding.codeField


        FirebaseAuth.getInstance().firebaseAuthSettings.forceRecaptchaFlowForTesting(true)

        if (auth.currentUser != null) {
            goToMain()
        }

        auth.setLanguageCode("ua")

        actionBut.setOnClickListener {

            if(codeText.visibility == View.GONE){
                phoneAuth.phoneAuth(phoneInput.text.toString())
                phoneText.visibility = View.GONE
                phoneInput.visibility = View.GONE

                codeText.visibility = View.VISIBLE
                codeInput.visibility = View.VISIBLE

                actionBut.setText(R.string.activity_sign_in_confirm)
                return@setOnClickListener
            }

            val code = codeInput.text.toString()
            try {
                confirmPhoneAuth(code)
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, R.string.activity_sign_in_code_alert, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }
    }


    private fun confirmPhoneAuth(code: String) {
        if (phoneAuth._verificationId == null || phoneAuth._resendToken == null)
            throw Exception("Don't sent CODE!")

        val credential = PhoneAuthProvider.getCredential(phoneAuth._verificationId, code)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential: success")
                    goToMain()
                } else {

                    Log.w(TAG, "signInWithCredential: failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Не вірний код!", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }


    private fun goToMain() {
        if (auth.currentUser?.displayName == null) {
            startActivity(Intent(this, ProfileCreateActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}

