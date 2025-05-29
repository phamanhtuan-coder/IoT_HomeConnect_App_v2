package com.sns.homeconnect_v2.presentation.component

import java.time.LocalTime

fun isDayTime(): Boolean {
    val hour = LocalTime.now().hour
    return hour in 6..18
}
