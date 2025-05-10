package com.sns.homeconnect_v2.data.remote.dto.response

import com.sns.homeconnect_v2.data.remote.dto.base.ISpaceBase

data class Space(
    override val spaceId: Int,
    override val name: String,
    override val houseId: Int
) : ISpaceBase