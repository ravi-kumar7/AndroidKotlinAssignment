package com.example.androidkotlinassignment.source.local

import androidx.lifecycle.LiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.models.Model

interface ILocalDataSource {
    val factCategory: LiveData<FactCategory>
    val allFacts: LiveData<List<Fact>>

    /* function syncs offline DB with the received response
        * @param data: response from the network API
        */
    @ExperimentalStdlibApi
    suspend fun syncDB(data: Model.Result?): Boolean

    /* function to call repository to sync offline data using Network API
         * @param input: a input string to generate the hash.
         * @return string: SHA-1 hash value of the input string
         */
    @ExperimentalStdlibApi
    fun generateHash(input: String): String

    companion object {
        val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    }
}