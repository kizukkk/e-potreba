package com.eteam.epotreba.domain.models

import com.google.android.gms.maps.model.LatLng

data class MarkerModel(
    val title: String,
    val about: String,
    val position: LatLng,
    val rate: Double)
