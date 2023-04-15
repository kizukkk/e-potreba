package com.eteam.epotreba

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.eteam.epotreba.R
import com.eteam.epotreba.databinding.ActivityMainBinding
import com.eteam.epotreba.presentation.fragment.NearFragment
import com.eteam.epotreba.presentation.fragment.ProfileFragment
import com.eteam.epotreba.presentation.fragment.ToiletsListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toiletsListFragment = ToiletsListFragment()
        val nearFragment = NearFragment()
        val profileFragment = ProfileFragment()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.list -> setFragment(toiletsListFragment)
                R.id.near -> setFragment(nearFragment)
                R.id.profile -> setFragment(profileFragment)
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, fragment)
        }
    }
}
