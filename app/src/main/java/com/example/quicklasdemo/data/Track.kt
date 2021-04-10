package com.example.quicklasdemo.data

import kotlinx.serialization.Serializable

@Serializable
data class Track (
        var trackName: String = "Default Name",
        val curveList: MutableList<Curve> = mutableListOf(),
        var showGrid: Boolean = false,
        var isLinear: Boolean = true,
        val trackMin: Float = 0f,
        val trackMax: Float = 100f)