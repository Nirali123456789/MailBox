package com.aiemail.superemail.Rooms

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aiemail.superemail.Models.Pin

@Dao
interface PinDao {
    @Query("SELECT * FROM Pin")
     fun getAllPins(): LiveData<List<Pin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPin(pin: Pin)

    @Update
    suspend fun updatePin(pin: Pin)

    @Delete
    suspend fun deletePin(pin: Pin)

    @Query("SELECT * FROM Pin WHERE is_pinned = 1")
    fun getPinnedItems(): List<Pin>

    @Query("SELECT * FROM Pin WHERE is_archived = 1")
    fun getArchivedItems(): LiveData<List<Pin>>
}
