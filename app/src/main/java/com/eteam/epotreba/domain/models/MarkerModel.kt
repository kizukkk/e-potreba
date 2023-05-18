package com.eteam.epotreba.domain.models

import com.google.android.gms.maps.model.LatLng

data class MarkerModel(
    val id: String,
    val title: String,
    val about: String,
    val position: LatLng,
    var sumRate: Double,
    val userId: String,
    var votes: Int,
    val price: Double
    ) {
    constructor(
        title: String,
        about: String,
        position: LatLng,
        rate: Double,
        userId: String,
        price: Double
    ) : this("null", title, about, position, rate, userId, 0, price)
}
