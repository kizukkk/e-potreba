package com.eteam.epotreba.presentation.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eteam.epotreba.R
import com.eteam.epotreba.domain.adapter.ProfileAdapter
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.presentation.activity.MarkerCreateActivity
import com.eteam.epotreba.presentation.activity.SignInActivity
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import com.eteam.epotreba.presentation.viewModel.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private val profileViewModel: ProfileViewModel by viewModels()

    private val adapter = ProfileAdapter()

    lateinit var favorite: Button

    lateinit var owned: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val addMarkerButton = view.findViewById<Button>(R.id.addMarkerButton)
        val signOut = view.findViewById<Button>(R.id.but_sign_out)

        val phoneInfo = view.findViewById<TextView>(R.id.phone_profile)
        val nameInfo = view.findViewById<TextView>(R.id.name_profile)
        val avatar = view.findViewById<TextView>(R.id.avatar_profile)

        favorite = view.findViewById(R.id.favorite_profile)
        owned = view.findViewById(R.id.owned_profile)

        phoneInfo.text = mainViewModel.currentUser?.phoneNumber
        nameInfo.text = mainViewModel.currentUser?.displayName
        avatar.text = mainViewModel.currentUser?.displayName!!.first().toString()

        switchList(profileViewModel.selectedList)

        val recycler = view.findViewById<RecyclerView>(R.id.rv_marker_list_profile)
        recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter




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

        owned.setOnClickListener {
            profileViewModel.selectedList = 0
            switchList(0)
        }

        favorite.setOnClickListener {
            profileViewModel.selectedList = 1
            switchList(1)
        }

        adapter.setOnClickListener(object : ProfileAdapter.OnClickListener {
            override fun onClick(position: Int, model: MarkerModel) {

                mainViewModel.passMarkerToFragment(model)

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if(transaction != null){
                    transaction.add(R.id.fragmentContainerView, DetailFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }

        })

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun switchList(number: Int){
        when (number) {
            1 -> {
                adapter.submit(profileViewModel.getFavoriteMarkers(mainViewModel.markerList))
                favorite.setTextColor(Color.parseColor("#4E23AA"));
                owned.setTextColor(Color.parseColor("#1C1B1F"));
            }
            0 -> {
                adapter.submit(profileViewModel.getOwnMarkers(mainViewModel.markerList))
                owned.setTextColor(Color.parseColor("#4E23AA"));
                favorite.setTextColor(Color.parseColor("#1C1B1F"));

            }
        }
    }



}
