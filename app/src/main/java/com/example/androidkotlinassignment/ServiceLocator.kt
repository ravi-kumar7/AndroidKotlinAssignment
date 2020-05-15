package com.example.androidkotlinassignment

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.androidkotlinassignment.source.FactsDataRepository
import com.example.androidkotlinassignment.source.IFactsDataRepository
import com.example.androidkotlinassignment.source.local.FactsDataBase
import com.example.androidkotlinassignment.source.local.LocalDataSource
import com.example.androidkotlinassignment.source.remote.RemoteDataSource

object ServiceLocator {
    private var database: FactsDataBase? = null

    @Volatile
    var factsDataRepository: IFactsDataRepository? = null
        @VisibleForTesting set

    private val lock = Any()


    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {

            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            factsDataRepository = null
        }
    }

    fun provideFactsRepository(context: Context): IFactsDataRepository {
        synchronized(this) {
            return factsDataRepository ?: createFactsRepository(context)
        }
    }

    private fun createFactsRepository(context: Context): FactsDataRepository {
        val newRepo = FactsDataRepository(createLocalDataSource(context), RemoteDataSource(context))
        factsDataRepository = newRepo
        return newRepo
    }


    private fun createLocalDataSource(context: Context): LocalDataSource {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.factDao())
    }

    private fun createDataBase(context: Context): FactsDataBase {
        val result = Room.databaseBuilder(
            context.applicationContext, FactsDataBase::class.java, context.getString(
                R.string.repository
            )
        ).build()
        database = result
        return result
    }
}