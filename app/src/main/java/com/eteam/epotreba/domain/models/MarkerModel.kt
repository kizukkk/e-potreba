package com.eteam.epotreba.domain.models

import com.google.android.gms.maps.model.LatLng

data class MarkerModel(
    val id: String,
    val title: String,
    val about: String,
    val position: LatLng,
    val rate: Double){
    constructor(title: String,
                about: String,
                position: LatLng,
                rate: Double,): this("null", title, about, position, rate)
}
