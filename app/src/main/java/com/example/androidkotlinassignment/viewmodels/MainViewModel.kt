package com.example.androidkotlinassignment.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.database.FactsDataBase
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.repository.FactsRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val factsRepository: FactsRepository
    var section: LiveData<FactCategory>
    var articles: LiveData<List<Fact>>
    var statusMsg: MutableLiveData<String>
    private var isDataSynced: MutableLiveData<Boolean>

    init {
        val articleDAO = FactsDataBase.getDatabase(application)!!.factDao()
        factsRepository = FactsRepository(application, articleDAO)
        section = factsRepository.getFactCategory()
        articles = factsRepository.getFacts()
        statusMsg = factsRepository.getStatus()
        isDataSynced = factsRepository.isDataSynced()
    }


    /* function to call repository to sync offline data using Network API
      * @param swiped: a Boolean value. True if user has swiped down to refresh the list, otherwise false.
      */
    fun syncDataFromAPI(swiped: Boolean = false) {
        if (!isDataSynced.value!! || swiped)
            factsRepository.createAPIRequestForDataSync()
    }

    fun setStatusMsg(status: String) {
        factsRepository.setStatus(status)
    }

}
