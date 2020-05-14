package com.example.androidkotlinassignment.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.source.FactsDataRepository
import com.example.androidkotlinassignment.source.local.FactsDataBase
import com.example.androidkotlinassignment.source.local.LocalDataSource
import com.example.androidkotlinassignment.source.remote.RemoteDataSource
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val factDataRepository: FactsDataRepository
    private val factCategory: LiveData<FactCategory>
    private val facts: LiveData<List<Fact>>
    private val statusMsg: MutableLiveData<String>
    private val isDataSynced: MutableLiveData<Boolean>

    init {
        val factDAO = FactsDataBase.getDatabase(application)!!.factDao()
        val localDataSource = LocalDataSource(factDAO)
        val remoteDataSource = RemoteDataSource(application.applicationContext)
        factDataRepository = FactsDataRepository(localDataSource, remoteDataSource)
        factCategory = factDataRepository.getFactCategory()
        facts = factDataRepository.getFacts()
        statusMsg = factDataRepository.getStatus()
        isDataSynced = factDataRepository.getIsDataSynced()
    }

    fun getFactCategory(): LiveData<FactCategory> {
        return factCategory
    }

    fun getFacts(): LiveData<List<Fact>> {
        return facts
    }

    fun getStatusMsg(): MutableLiveData<String> {
        return statusMsg
    }

    fun isDataSynced(): MutableLiveData<Boolean> {
        return isDataSynced
    }



    /* function to call repository to sync offline data using Network API
      * @param swiped: a Boolean value. True if user has swiped down to refresh the list, otherwise false.
      */
    @ExperimentalStdlibApi
    fun syncDataFromAPI(swiped: Boolean = false) {
        if (!isDataSynced.value!! || swiped) {
            viewModelScope.launch {
                factDataRepository.updateDataFromAPIServer()
            }
        }
    }

    fun setStatusMsg(status: String) {
        factDataRepository.setStatus(status)
    }

}
