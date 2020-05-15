package com.example.androidkotlinassignment.source

import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.source.local.ILocalDataSource
import com.example.androidkotlinassignment.source.remote.IRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FactsDataRepository(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFactsDataRepository {

    override val status: MutableLiveData<String> = MutableLiveData()
    override val factCategory = localDataSource.factCategory
    override val allFacts = localDataSource.allFacts
    override val isDataSynced: MutableLiveData<Boolean> = MutableLiveData(false)


    override fun setStatus(status: String?) {
        this.status.postValue(status)
    }

    @ExperimentalStdlibApi
    override suspend fun updateDataFromAPIServer() {
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