package com.sns.homeconnect_v2.data.remote.dto.response.house
import com.sns.homeconnect_v2.data.remote.dto.base.IHouseBase

data class House(
    override val houseId: Int,
    override val name: String,
    override val address: String
) : IHouseBase