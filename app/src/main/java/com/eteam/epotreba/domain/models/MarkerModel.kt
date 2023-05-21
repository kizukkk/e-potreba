package com.eteam.epotreba.domain.models

import com.google.android.gms.maps.model.LatLng

data class MarkerModel(
    val id: String,
    var title: String,
    var about: String,
    val position: LatLng,
    var sumRate: Double,
    val userId: String,
    var votes: Int,
    var price: Double
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
