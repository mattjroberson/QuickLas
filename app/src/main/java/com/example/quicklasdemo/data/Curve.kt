package com.example.quicklasdemo.data

import kotlinx.serialization.Serializable

@Serializable
data class Curve(var curveName: String = "Default Name",
                 var lineStyle: String = "Default Style",
                 var curveColor: String = "000000",
                 var scaleMin: Float = .1f,
                 var scaleMax: Float = 1.0f,
                 var picked: Boolean = false) //picked is for internal UI Logic