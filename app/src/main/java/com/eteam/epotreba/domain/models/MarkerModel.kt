package com.eteam.epotreba.domain.models

import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat

data class MarkerModel(
    val id: String,
    var title: String,
    var about: String,
    val position: LatLng,
    var sumRate: Double,
    val userId: String,
    var votes: Int,
    var price: Double,
    var address: String = "невизначено",
    var distance: Double = 0.0
    ) {
    constructor(
        title: String,
        about: String,
        position: LatLng,
        rate: Double,
        userId: String,
        price: Double
    ) : this("null", title, about, position, rate, userId, 0, price)


    fun convertDistance(): String {
        return if (distance >= 1000) {
            val kilometers = distance / 1000
            "${"%.2f".format(kilometers)} км"
        } else {
            "${distance.toInt()} м"
        }
    }

    fun getScore(): Double {
        val df = DecimalFormat("#.#")
        return if(sumRate == 0.0)
            sumRate
        else
            df.format(sumRate / votes).toDouble()
    }

}
