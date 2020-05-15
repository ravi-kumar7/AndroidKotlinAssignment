package com.example.androidkotlinassignment.source

import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory

class FakeFactsDataRepository : IFactsDataRepository {
    override val status: MutableLiveData<String> = MutableLiveData()
    override val factCategory: MutableLiveData<FactCategory> = MutableLiveData()
    override val allFacts: MutableLiveData<List<Fact>> = MutableLiveData()
    override val isDataSynced: MutableLiveData<Boolean> = MutableLiveData()


    override fun setStatus(status: String?) {
        this.status.postValue(status)
    }

    @ExperimentalStdlibApi
    override suspend fun updateDataFromAPIServer() {

    }
}