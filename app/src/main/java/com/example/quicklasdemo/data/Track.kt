package com.example.quicklasdemo.data

import kotlinx.serialization.Serializable

@Serializable
data class Track (
        var trackName: String = "Default Name",
        var curveList: MutableList<Curve> = mutableListOf(),
        var showGrid: Boolean = false,
        var isLinear: Boolean = true,
        var verticalDivCount: Int = 3,
        var horizontalDivHeight: Int = 5,
        var uniqueID: Long = System.currentTimeMillis())