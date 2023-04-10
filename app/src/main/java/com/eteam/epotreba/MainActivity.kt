package com.eteam.epotreba

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eteam.epotreba.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toiletsListFragment = ToiletsListFragment()
        val nearFragment = NearFragment()
        val profileFragment = ProfileFragment()

        loadFragment(nearFragment)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.list -> {
                    loadFragment(toiletsListFragment)
                    true
                }
                R.id.near -> {
                    loadFragment(nearFragment)
                    true
                }
                R.id.profile -> {
                    loadFragment(profileFragment)
                    true
                }

                else -> throw IllegalStateException("Invalid menu item id")
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navigationFragmentsContainer, fragment)
        transaction.commit()
    }
}