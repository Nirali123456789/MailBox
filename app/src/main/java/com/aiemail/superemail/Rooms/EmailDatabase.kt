package com.example.myapplication.RoomDatabase

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aiemail.superemail.Models.Draft
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.Important
import com.aiemail.superemail.Models.Sent
import com.aiemail.superemail.Models.Spam
import com.aiemail.superemail.Models.Trash
import com.aiemail.superemail.Rooms.DraftDao
import com.aiemail.superemail.Rooms.EmailDao
import com.aiemail.superemail.Rooms.ImportantDao
import com.aiemail.superemail.Rooms.SentDao
import com.aiemail.superemail.Rooms.SpamDao
import com.aiemail.superemail.Rooms.TrashDao
import com.aiemail.superemail.TypeConverters.StringArrayConverter
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [Email::class, Spam::class, Sent::class, Draft::class, Important::class,Trash::class],
    version = 1
)
@TypeConverters(StringArrayConverter::class)
public abstract class EmailDatabase : RoomDatabase() {

    abstract val categorydao: EmailDao
    abstract val sentdao: SentDao
    abstract val draftDao: DraftDao
    abstract val spamDao: SpamDao
    abstract val importantDao: ImportantDao
    abstract val trashDao: TrashDao


    companion object {
        @Volatile
        public var INSTANCE: EmailDatabase? = null

       public fun getDatabase(context: Context, scope: CoroutineScope):EmailDatabase{
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