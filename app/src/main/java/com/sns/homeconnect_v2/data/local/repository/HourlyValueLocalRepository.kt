package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.HourlyValueDao
import com.sns.homeconnect_v2.data.local.entity.HourlyValueEntity
import jakarta.inject.Inject

class HourlyValueLocalRepository @Inject constructor(
    private val dao: HourlyValueDao
) {
    suspend fun getAllHourlyValues() = dao.getAll()

    suspend fun saveHourlyValues(values: List<HourlyValueEntity>) = dao.insertAll(values)

    suspend fun clearHourlyValues() = dao.clear()

    suspend fun getValuesByDeviceSerial(deviceSerial: String) = dao.getValuesByDeviceSerial(deviceSerial)

    suspend fun getValuesBySpaceId(spaceId: Int) = dao.getValuesBySpaceId(spaceId)

    suspend fun getValuesInTimeRange(deviceSerial: String, startTime: Long, endTime: Long) =
        dao.getValuesInTimeRange(deviceSerial, startTime, endTime)
}
