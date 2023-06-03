package com.eteam.epotreba.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eteam.epotreba.databinding.ActivityProfileCreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.launch

class ProfileCreateActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfileCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            currentUser!!.updateProfile(userProfileChangeRequest{displayName = "Vasyl"})
        }

        binding.butCreateProfile.setOnClickListener {

            val nameInfo: String = binding.nameField.text.toString()

            val profileUpdates = userProfileChangeRequest {
                displayName = nameInfo
            }

            lifecycleScope.launch {
                currentUser!!.updateProfile(profileUpdates)
            }

            Toast.makeText(this, "Успішно додано!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }
}