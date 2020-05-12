package com.example.androidkotlinassignment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidkotlinassignment.R
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory

@Database(entities = [Fact::class, FactCategory::class], version = 1)
abstract class FactsDataBase: RoomDatabase() {
    abstract fun factDao(): FactDAO

    companion object {
        @Volatile
        private var factsDatabaseInstance: FactsDataBase? = null

        internal fun getDatabase(context: Context): FactsDataBase? {
            if(factsDatabaseInstance ==null)
            {
                synchronized(FactsDataBase::class.java){
                    if(factsDatabaseInstance ==null) {
                        factsDatabaseInstance = Room.databaseBuilder(context.applicationContext, FactsDataBase::class.java, context.getString(
                            R.string.repository))
                        .build()
                    }
                }
            }
            return factsDatabaseInstance
        }
    }
}