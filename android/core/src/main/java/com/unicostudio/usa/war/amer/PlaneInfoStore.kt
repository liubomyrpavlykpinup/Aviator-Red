package com.unicostudio.usa.war.amer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaneInfoStore(private val db: AviatorRedDatabase) {

    suspend fun getLatest() = withContext(Dispatchers.IO) {
        return@withContext db.planeInfoDao().fetchLastPlaneInfo()
    }

    suspend fun insert(name: String) {
        withContext(Dispatchers.IO) {
            db.planeInfoDao().insert(PlaneInfo(0, name, System.currentTimeMillis()))
        }
    }
}