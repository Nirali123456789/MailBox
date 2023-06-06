package com.example.myapplication.RoomDatabase

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aiemail.superemail.feature.Models.Email
import com.aiemail.superemail.feature.Rooms.EmailDao
import com.aiemail.superemail.feature.utilis.StringArrayConverter
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Email::class], version = 1,exportSchema = false)
@TypeConverters(StringArrayConverter::class)
abstract class EmailDatabase : RoomDatabase() {

    abstract val categorydao: EmailDao


    companion object {
        @Volatile
        public var INSTANCE: EmailDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope):EmailDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmailDatabase::class.java,
                    "food_item_database"
                ).addCallback(FoodItemCallback(scope))
                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }
    private class FoodItemCallback(val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

//            INSTANCE?.let { foodItemRoomDB ->
//                scope.launch {
//                    // if you want to populate database
//                    // when RoomDatabase is created
//                    // populate here
//                    foodItemRoomDB.categorydao.insert(Category("2","100f","",""))
//                }
//            }
        }
    }
}