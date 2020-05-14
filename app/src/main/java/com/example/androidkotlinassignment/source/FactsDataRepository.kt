package com.example.androidkotlinassignment.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.source.local.ILocalDataSource
import com.example.androidkotlinassignment.source.remote.IRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FactsDataRepository(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val status: MutableLiveData<String> = MutableLiveData()
    private val section: LiveData<FactCategory> = localDataSource.factCategory
    private val allFacts = localDataSource.allFacts
    private val isDataSynced: MutableLiveData<Boolean> = MutableLiveData(false)


    fun getFactCategory(): LiveData<FactCategory> {
        return section
    }

    fun getFacts(): LiveData<List<Fact>> {
        return allFacts
    }

    fun getStatus(): MutableLiveData<String> {
        return status
    }

    fun getIsDataSynced(): MutableLiveData<Boolean> {
        return isDataSynced
    }

    fun setStatus(status: String?) {
        this.status.postValue(status)
    }

    @ExperimentalStdlibApi
    suspend fun updateDataFromAPIServer() {
        withContext(ioDispatcher) {
            val result = remoteDataSource.getDataFromAPI()
            if (result is Result.Success) {
                val dbSyncResult = localDataSource.syncDB(result.data)
                isDataSynced.postValue(dbSyncResult)
                status.postValue("Data Synced with the Server")
            } else {
                status.postValue(result.toString())
                isDataSynced.postValue(false)
            }
        }
    }


}