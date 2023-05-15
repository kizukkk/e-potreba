package com.eteam.epotreba.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eteam.epotreba.R
import com.eteam.epotreba.databinding.FragmentNearBinding
import com.eteam.epotreba.domain.adapter.MarkerAdapter
import com.eteam.epotreba.domain.models.MarkerModel
import com.eteam.epotreba.presentation.viewModel.MainViewModel

class NearFragment : Fragment(R.layout.fragment_near) {

    private val viewModel: MainViewModel by activityViewModels()

    private val adapter = MarkerAdapter()
    lateinit var binding: FragmentNearBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearBinding.inflate(inflater)
        val recycler = binding.rvMarkerList

        recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter



        viewModel.markerList.observe(this as LifecycleOwner) {
            adapter.submit(it)
        }

        adapter.setOnClickListener(object : MarkerAdapter.OnClickListener {
            override fun onClick(position: Int, model: MarkerModel) {

                viewModel.passMarkerToFragment(model)

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if(transaction != null){
                    transaction.replace(R.id.fragmentContainerView, DetailFragment())
                    transaction.commit()
                }
            }

        })

        return binding.root
    }
}
