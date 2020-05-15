package com.example.androidkotlinassignment.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory

interface IFactsDataRepository {
    val status: MutableLiveData<String>
    val factCategory: LiveData<FactCategory>
    val allFacts: LiveData<List<Fact>>
    val isDataSynced: MutableLiveData<Boolean>
    fun setStatus(status: String?)

    @ExperimentalStdlibApi
    suspend fun updateDataFromAPIServer()
}