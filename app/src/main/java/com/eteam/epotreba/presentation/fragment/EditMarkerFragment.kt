package com.eteam.epotreba.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eteam.epotreba.R
import com.eteam.epotreba.presentation.dialog_fragment.EditMarkerDialogFragment
import com.eteam.epotreba.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch

class EditMarkerFragment : Fragment(R.layout.fragment_edit_marker) {


    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var title: TextView
    private lateinit var about: TextView
    private lateinit var price: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_edit_marker, container, false)

        title = view.findViewById(R.id.edit_title)
        about = view.findViewById(R.id.edit_about)
        price = view.findViewById(R.id.edit_price)
        val address = view.findViewById<TextView>(R.id.edit_address)

        val marker = viewModel.passMarker

        val cancelButton = view.findViewById<Button>(R.id.but_edit_cancel)
        val saveButton = view.findViewById<Button>(R.id.but_edit_save)
        val deleteButton = view.findViewById<Button>(R.id.but_edit_delete)


        title.text = marker.title
        about.text = marker.about
        price.text = marker.price.toString()
        address.text = marker.position.toString()


        title.setOnClickListener {
            startDialog(marker.title, title)
        }

        about.setOnClickListener {
            startDialog(marker.about, about)
        }

        price.setOnClickListener {
            startDialog(marker.price.toString(), price)
        }


        cancelButton.setOnClickListener {
            removeFragment()
        }

        saveButton.setOnClickListener {
            updateEdit()
            removeFragment()
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.delete(viewModel.passMarker)
            }
            removeFragment()
        }

        return view
    }

    private fun startDialog(value: String, view: TextView) {
        viewModel.temp = value
        val dialog = EditMarkerDialogFragment(view)
        dialog.show(parentFragmentManager, "dialog")

    }

    private fun updateEdit() {
        viewModel.passMarker.title = title.text.toString()
        viewModel.passMarker.about = about.text.toString()
        viewModel.passMarker.price = price.text.toString().toDouble()

        viewModel.updateMarker(viewModel.passMarker)
    }

    @SuppressLint("CommitTransaction")
    private fun removeFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction!!.remove(this).commit()
        requireActivity().supportFragmentManager.popBackStack()
    }


}