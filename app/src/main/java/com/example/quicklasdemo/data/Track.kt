package com.example.quicklasdemo.data

import kotlinx.serialization.Serializable

@Serializable
data class Track (
        var trackName: String = "Default Name",
        var curveList: MutableList<Curve> = mutableListOf(),
        var showGrid: Boolean = true,
        var isLinear: Boolean = true,
        var verticalDivCount: Int = 4,
        var uniqueID: Long = System.currentTimeMillis())