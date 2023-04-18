package com.eteam.epotreba.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eteam.epotreba.R
import com.eteam.epotreba.databinding.FragmentNearBinding
import com.eteam.epotreba.domain.adapter.MarkerAdapter
import com.eteam.epotreba.domain.usecase.GetMarkersUseCase

class NearFragment : Fragment(R.layout.fragment_near) {
    private val adapter = MarkerAdapter()
    private val items = GetMarkersUseCase().execute()
    lateinit var binding : FragmentNearBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = binding.rvMarkerList

        recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter

        adapter.submit(items)
    }
}
