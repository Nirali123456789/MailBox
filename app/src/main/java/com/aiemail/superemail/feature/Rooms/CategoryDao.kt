package com.aiemail.superemail.feature.Rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aiemail.superemail.feature.Models.Article
import com.aiemail.superemail.feature.Models.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(Courses: ArrayList<Article>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(foodItem: Article)

    @Query("SELECT * FROM Article")
    fun getAllMail(): LiveData<List<Article>>
      
}