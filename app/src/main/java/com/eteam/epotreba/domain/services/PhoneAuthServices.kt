package com.eteam.epotreba.domain.services

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthServices(val context: Context, private val auth: FirebaseAuth) {

    lateinit var _verificationId: String
    lateinit var _resendToken: PhoneAuthProvider.ForceResendingToken

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(ContentValues.TAG, "onVerificationFailed", e)

            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Toast.makeText(context, "Помилковий запит, спробуйте ще раз!!",Toast.LENGTH_LONG).show()
                }
                is FirebaseTooManyRequestsException -> {
                    Toast.makeText(context, "Досягнуто ліміту СМС на проєкт!",Toast.LENGTH_LONG).show()
                }
                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    //throw FirebaseAuthMissingActivityForRecaptchaException()
                }
            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d(ContentValues.TAG, "onCodeSent:$verificationId")

            _verificationId = verificationId
            _resendToken = token
        }
    }


    fun phoneAuth(phone: String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context as Activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}