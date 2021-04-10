package com.example.quicklasdemo.data

import android.util.Log
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Track (
        var trackName: String = "Default Name",
        val curveList: MutableList<Curve> = mutableListOf(),
        var showGrid: Boolean = false,
        var isLinear: Boolean = true,
        val trackMin: Float = 0f,
        val trackMax: Float = 100f)