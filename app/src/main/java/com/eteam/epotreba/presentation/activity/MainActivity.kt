package com.eteam.epotreba.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.eteam.epotreba.R
import com.eteam.epotreba.databinding.ActivityMainBinding
import com.eteam.epotreba.presentation.fragment.NearFragment
import com.eteam.epotreba.presentation.fragment.ProfileFragment
import com.eteam.epotreba.presentation.fragment.ToiletsListFragment
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toiletsListFragment = ToiletsListFragment()
        val nearFragment = NearFragment()
        val profileFragment = ProfileFragment()


        //АХТУНГ! КОСТИЛІ
        if (viewModel.markerList.value?.isEmpty() == true){
            lifecycleScope.launch {
                viewModel.update()
            }
        }

        swipeRefreshLayout = binding.swiperRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.update()
                swipeRefreshLayout.isRefreshing = false
            }
        }

        //Заплатка по першочерговому виклику фрагменту Near
        setFragment(toiletsListFragment)

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
