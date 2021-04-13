package com.example.quicklasdemo.data

import kotlinx.serialization.Serializable

@Serializable
data class Curve(val curveName: String = "Default Name",
                 val lineStyle: String = "Default Style",
                 val curveColor: String = "#000000",
                 val scaleMin: Float = .1f,
                 val scaleMax: Float = 1.0f,
                 val cutoffHighlighting: String = "Default?",
                 var picked: Boolean = false) //picked is for internal UI Logic