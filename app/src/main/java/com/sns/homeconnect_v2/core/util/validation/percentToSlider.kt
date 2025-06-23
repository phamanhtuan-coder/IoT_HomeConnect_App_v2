package com.sns.homeconnect_v2.core.util.validation

import kotlin.math.roundToInt

fun percentToSlider(p: Int): Float = p * 2.55f

fun sliderToPercent(s: Float): Int = (s / 2.55f).roundToInt()