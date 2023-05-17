package com.eteam.epotreba.domain.models

import com.google.android.gms.maps.model.LatLng

data class MarkerModel(
    val id: String,
    val title: String,
    val about: String,
    val position: LatLng,
    val rate: Double,
    val userId: String){
    constructor(title: String,
                about: String,
                position: LatLng,
                rate: Double,
                userId: String): this("null", title, about, position, rate, userId)
}
