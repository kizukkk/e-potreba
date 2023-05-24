package com.eteam.epotreba.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.eteam.epotreba.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class EditProfileFragment : Fragment(R.layout.fragment_profile_edit) {

    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var name: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        name = view.findViewById(R.id.editProfile_name)

        val cancelButton = view.findViewById<Button>(R.id.but_editProfile_cancel)
        val saveButton = view.findViewById<Button>(R.id.but_editProfile_save)


        name.text = user?.displayName



        cancelButton.setOnClickListener {
            activity?.onBackPressed()
        }

        saveButton.setOnClickListener {
            if(name.text.toString() == user?.displayName)
                return@setOnClickListener
            val profileUpdates = userProfileChangeRequest {
                displayName = name.text.toString()
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Дані успішно змінено!", Toast.LENGTH_SHORT).show()
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        if (transaction != null) {
                            transaction.replace(R.id.fragmentContainerView, ProfileFragment())
                            transaction.commit()
                        }
                    }
                }
        }
        return view
    }



}