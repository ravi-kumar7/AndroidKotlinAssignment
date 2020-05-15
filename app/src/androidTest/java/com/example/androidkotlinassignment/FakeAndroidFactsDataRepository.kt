package com.example.androidkotlinassignment

import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.source.IFactsDataRepository

class FakeAndroidFactsDataRepository : IFactsDataRepository {
    override val status: MutableLiveData<String> = MutableLiveData()
    override val factCategory: MutableLiveData<FactCategory> = MutableLiveData()
    override val allFacts: MutableLiveData<List<Fact>> = MutableLiveData()
    override val isDataSynced: MutableLiveData<Boolean> = MutableLiveData()


    fun setupData(
        status: String,
        factCategory: FactCategory,
        facts: ArrayList<Fact>,
        isSynced: Boolean
    ) {
        this.status.postValue(status)
        this.factCategory.postValue(factCategory)
        this.allFacts.postValue(facts)
        this.isDataSynced.postValue(isSynced)
    }

    override fun setStatus(status: String?) {
        this.status.postValue(status)
    }

    @ExperimentalStdlibApi
    override suspend fun updateDataFromAPIServer() {

    }
}