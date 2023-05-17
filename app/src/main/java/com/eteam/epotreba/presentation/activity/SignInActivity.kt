package com.eteam.epotreba.presentation.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eteam.epotreba.databinding.ActivitySignInBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class SignInActivity : AppCompatActivity(){

    private var auth = FirebaseAuth.getInstance()
    lateinit var _verificationId: String
    lateinit var _resendToken: PhoneAuthProvider.ForceResendingToken

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            goToMain()
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)

            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Toast.makeText(this@SignInActivity, "Помилковий запит, спробуйте ще раз!!",Toast.LENGTH_LONG).show()
                }
                is FirebaseTooManyRequestsException -> {
                    Toast.makeText(this@SignInActivity, "Досягнуто ліміту СМС на проєкт!",Toast.LENGTH_LONG).show()
                }
                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    // reCAPTCHA verification attempted with null Activity
                }
            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            _verificationId = verificationId
            _resendToken = token
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if(auth.currentUser != null) {
            goToMain()
        }

        auth.setLanguageCode("ua")

        binding.butSendCode.setOnClickListener{
            val phone = binding.phoneField.text.toString()
            phoneAuth(phone)
        }

        binding.butConfirm.setOnClickListener {
            val code = binding.codeField.text.toString()
            try {
                confirmPhoneAuth(code)
            }catch (e: java.lang.Exception){
                Toast.makeText(this, "Спочатку надішліть КОД!",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }
    }


    private fun confirmPhoneAuth(code: String){
        if(_verificationId == null || _resendToken == null)
            throw Exception("Don't sent CODE!")

        val credential = PhoneAuthProvider.getCredential(_verificationId, code)

        signInWithPhoneAuthCredential(credential)
    }

    private fun phoneAuth(phone: String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential: success")
                    goToMain()
                } else {

                    Log.w(TAG, "signInWithCredential: failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Не вірний код!",Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun goToMain(){
        if (auth.currentUser?.displayName == null){
            startActivity(Intent(this, ProfileCreateActivity::class.java))
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }


}

