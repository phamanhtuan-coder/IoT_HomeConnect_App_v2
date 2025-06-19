package com.sns.homeconnect_v2.core.util.validation

import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.presentation.model.HouseUi

fun HouseWithSpacesResponse.toHouseUi(role: String): HouseUi {
    return HouseUi(
        id         = house_id,
        name       = house_name,
        spaces     = spaces?.size ?:0,
        role       = role,
        isRevealed = false,
        iconName    = icon_name ?: "house",
        iconColor  = parseColorOrDefault(icon_color)
    )
}
