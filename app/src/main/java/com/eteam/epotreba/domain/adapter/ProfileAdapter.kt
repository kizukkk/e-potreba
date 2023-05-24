package com.eteam.epotreba.domain.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eteam.epotreba.R
import com.eteam.epotreba.domain.models.MarkerModel

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private var markerList = emptyList<MarkerModel>()

    private var onClickListener: OnClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val score: TextView = view.findViewById(R.id.marker_score)
        val pos: TextView = view.findViewById(R.id.marker_address)
        val dist: TextView = view.findViewById(R.id.marker_dist)
        val priceIcon: ImageView = view.findViewById(R.id.marker_price_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.marker_item, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(markerList[position].price == 0.0){
            holder.priceIcon.visibility = View.INVISIBLE
        }

        val scoreString = if (markerList[position].getScore() == 0.0) "0/5" else
            "${"%.1f".format(markerList[position].getScore())}/5"

        holder.score.text = scoreString
        holder.pos.text = markerList[position].address
        holder.dist.text = markerList[position].convertDistance()

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