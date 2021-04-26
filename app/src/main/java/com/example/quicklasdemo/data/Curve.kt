package com.example.quicklasdemo.data

import kotlinx.serialization.Serializable

@Serializable
data class Curve(var curveName: String = "Default Name",
                 var lineStyle: String = "Default Style",
                 var curveColor: String = "000000",
                 var scaleMin: Float = 0f,
                 var scaleMax: Float = 100f,
                 var picked: Boolean = false) //picked is for internal UI Logic