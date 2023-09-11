package com.unicostudio.usa.war.amer

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class PlaneInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val timestamp: Long,
)

@Dao
interface PlaneInfoDao {

    @Query("SELECT * FROM PlaneInfo ORDER BY timestamp DESC LIMIT 1")
    suspend fun fetchLastPlaneInfo(): PlaneInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planeInfo: PlaneInfo)

}

@Database(entities = [PlaneInfo::class], version = 1)
abstract class AviatorPurpleDatabase : RoomDatabase() {

    abstract fun planeInfoDao(): PlaneInfoDao

    companion object {

        fun getInstance(appContext: Context) = Room
            .databaseBuilder(appContext, AviatorPurpleDatabase::class.java, "planes-db")
            .build()
    }
}