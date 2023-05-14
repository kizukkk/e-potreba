package com.eteam.epotreba.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.eteam.epotreba.R
import com.eteam.epotreba.presentation.activity.MarkerCreateActivity
import com.eteam.epotreba.presentation.activity.SignInActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val addMarkerButton = view.findViewById<Button>(R.id.addMarkerButton)
        val signOut = view.findViewById<Button>(R.id.but_sign_out)

        addMarkerButton.setOnClickListener {
            activity?.let {
                val intent = Intent(it, MarkerCreateActivity::class.java)
                it.startActivity(intent)
            }
        }

        signOut.setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(activity, SignInActivity::class.java))
            activity?.finish()
        }




        return view
    }

}
