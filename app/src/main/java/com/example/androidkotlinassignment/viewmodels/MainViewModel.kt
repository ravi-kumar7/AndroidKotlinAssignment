package com.example.androidkotlinassignment.viewmodels

import androidx.lifecycle.*
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.source.IFactsDataRepository
import kotlinx.coroutines.launch

class MainViewModel(private val factsDataRepository: IFactsDataRepository) : ViewModel() {

    private val factCategory: LiveData<FactCategory> = factsDataRepository.factCategory
    private val facts: LiveData<List<Fact>> = factsDataRepository.allFacts
    private val statusMsg: MutableLiveData<String> = factsDataRepository.status
    private val isDataSynced: MutableLiveData<Boolean> = factsDataRepository.isDataSynced

    init {
//        val factDAO = FactsDataBase.getDatabase(application)!!.factDao()
//        val localDataSource = LocalDataSource(factDAO)
//        val remoteDataSource = RemoteDataSource(application.applicationContext)
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
                factsDataRepository.updateDataFromAPIServer()
            }
        }
    }

    fun setStatusMsg(status: String) {
        factsDataRepository.setStatus(status)
    }


    @Suppress("UNCHECKED_CAST")
    class FactsViewModelFactory(
        private val factsDataRepository: IFactsDataRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            (MainViewModel(factsDataRepository) as T)
    }
}
