package com.aiemail.superemail.feature.Rooms

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aiemail.superemail.feature.Models.Email


@Dao
interface EmailDao {



    @Insert()
     fun insertAll(Courses: ArrayList<Email>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(foodItem: Email)

    @Query("SELECT * FROM Article ")
    fun getAllMail(): LiveData<List<Email>>
    @Query("Delete  FROM Article")
    fun DeleteMails()
      
}