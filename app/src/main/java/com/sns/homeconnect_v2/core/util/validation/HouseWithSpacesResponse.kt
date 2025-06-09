package com.sns.homeconnect_v2.core.util.validation

import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.presentation.model.HouseUi

fun HouseWithSpacesResponse.toHouseUi(role: String): HouseUi {
    return HouseUi(
        id         = house_id,
        name       = house_name,
        spaces     = spaces.size,
        role       = role,
        isRevealed = false,
        icon       = getIconByName(icon_name),
        iconColor  = parseColorOrDefault(icon_color)
    )
}
