package com.eteam.epotreba.presentation.dialog_fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.eteam.epotreba.R
import com.eteam.epotreba.presentation.viewModel.MainViewModel


class EditMarkerDialogFragment(val textView: TextView) : DialogFragment() {
    private val viewModel: MainViewModel by activityViewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_edit_marker, null)

        val submit = view.findViewById<Button>(R.id.but_sub_edit)

        submit.setOnClickListener{
            val value = view.findViewById<EditText>(R.id.editValue).text.toString()

            if(textView.id == R.id.edit_price){
                try {
                    value.toDouble()
                }catch (e: NumberFormatException){
                    Toast.makeText(activity, "Некоректний формат!",
                        Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if(value.isNotEmpty()){
                viewModel.temp = value
                textView.text = value
            }
            super.dismiss()
        }

        return view
    }
}