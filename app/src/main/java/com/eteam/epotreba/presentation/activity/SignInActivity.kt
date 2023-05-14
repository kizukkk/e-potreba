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
            //signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
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


        auth.setLanguageCode("ua")
        if(auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential: success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential: failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


}