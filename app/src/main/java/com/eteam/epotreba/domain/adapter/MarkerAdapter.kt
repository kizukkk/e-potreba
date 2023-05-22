package com.eteam.epotreba.domain.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eteam.epotreba.R
import com.eteam.epotreba.domain.models.MarkerModel

class MarkerAdapter : RecyclerView.Adapter<MarkerAdapter.ViewHolder>() {

    private var markerList = emptyList<MarkerModel>()
    private var onClickListener: OnClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.marker_title)
        val about : TextView = view.findViewById(R.id.marker_about)
        val pos : TextView = view.findViewById(R.id.marker_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.marker_item, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = markerList[position].title
        holder.about.text = markerList[position].about
        holder.pos.text = markerList[position].address

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, markerList[position] )
            }
        }
    }

    override fun getItemCount(): Int {
        return markerList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submit(array: List<MarkerModel>){
        markerList = array
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener

    }

    interface OnClickListener {
        fun onClick(position: Int, model: MarkerModel)
    }

}