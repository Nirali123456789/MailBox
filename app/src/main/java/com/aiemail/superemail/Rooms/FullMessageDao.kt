package com.aiemail.superemail.Rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Pin

@Dao
interface FullMessageDao {
    @Query("SELECT * FROM FullMessage")
     fun getAllMessage(): LiveData<List<FullMessage>>

    @Query("SELECT * FROM FullMessage WHERE sender_id =:sender_id")
    fun getMessage(sender_id:String): LiveData<FullMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPin(fullMessage: FullMessage)

    @Update
    suspend fun updatePin(fullMessage: FullMessage)

    @Delete
    suspend fun deletePin(fullMessage: FullMessage)


}
