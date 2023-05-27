package com.eteam.epotreba.presentation.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.eteam.epotreba.R
import com.eteam.epotreba.domain.services.PhoneAuthServices
import com.eteam.epotreba.presentation.activity.MainActivity
import com.eteam.epotreba.presentation.viewModel.ProfileViewModel
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.util.concurrent.TimeUnit

class EditProfileFragment : Fragment(R.layout.fragment_profile_edit) {

    private val user = FirebaseAuth.getInstance().currentUser
    private var auth = FirebaseAuth.getInstance()
    private lateinit var phoneAuth: PhoneAuthServices

    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var code: TextView
    private lateinit var codeField: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        phoneAuth = PhoneAuthServices(requireContext(), auth)

        name = view.findViewById(R.id.editProfile_name)
        phone = view.findViewById(R.id.editProfile_phone)
        code = view.findViewById(R.id.editProfile_code)
        codeField = view.findViewById(R.id.textView_code)

        val cancelButton = view.findViewById<Button>(R.id.but_editProfile_cancel)
        val saveButton = view.findViewById<Button>(R.id.but_editProfile_save)


        name.text = user?.displayName
        phone.text = user?.phoneNumber


        cancelButton.setOnClickListener {
            activity?.onBackPressed()
        }

        saveButton.setOnClickListener {

            if (phone.text.toString() != user?.phoneNumber && code.visibility == View.GONE) {
                code.visibility = View.VISIBLE
                codeField.visibility = View.VISIBLE
                Toast.makeText(activity, "Введіть код підтвердження!", Toast.LENGTH_SHORT).show()
                phoneAuth!!.phoneAuth(phone.text.toString())
                return@setOnClickListener
            }

            if (code.text.toString().isEmpty() && code.visibility != View.GONE) {
                Toast.makeText(activity, "Невірний код!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (code.text.toString().isNotEmpty()) {
                try {
                    confirmPhoneAuth(code.text.toString())
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    return@setOnClickListener
                }
            }
            successUpdate()


        }
        return view
    }

    private fun successUpdate() {
        if (name.text.toString() != user?.displayName) {
            val profileUpdates = userProfileChangeRequest {
                displayName = name.text.toString()
            }

            user!!.updateProfile(profileUpdates).addOnSuccessListener {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.replace(R.id.fragmentContainerView, ProfileFragment())
                    transaction.commit()
                }
            }
        }

        Toast.makeText(activity, "Дані успішно змінено!", Toast.LENGTH_SHORT).show()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.fragmentContainerView, ProfileFragment())
            transaction.commit()
        }

    }


    private fun confirmPhoneAuth(code: String) {
        val credential = PhoneAuthProvider.getCredential(phoneAuth!!._verificationId, code)

        user!!.updatePhoneNumber(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "updatePhoneNumber: success")
                    successUpdate()

                } else {

                    Log.w(ContentValues.TAG, "updatePhoneNumber: failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(activity, "Не вірний код!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        throw FirebaseAuthInvalidCredentialsException(code, "invalid code")

    }


}