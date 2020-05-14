package com.example.androidkotlinassignment.source.local

import androidx.lifecycle.LiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.models.Model
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.*

class LocalDataSource(
    private val factDAO: FactDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ILocalDataSource {

    override val factCategory: LiveData<FactCategory> = factDAO.getFactCategory()
    override val allFacts = factDAO.allFacts()


    /* function syncs offline DB with the received response
     * @param data: response from the network API
     */
    @ExperimentalStdlibApi
    override suspend fun syncDB(data: Model.Result?) = withContext(ioDispatcher) {
        val factCategoryId = UUID.randomUUID().toString()
        val factCategoryTitle = data!!.title
        val factCategoryDB = FactCategory(factCategoryId, factCategoryTitle)
        factDAO.insertSection(factCategoryDB)
        for (row in data.rows) {
            if (row.title != null) {
                val fact = Fact(
                    generateHash(row.title),
                    row.title,
                    row.description,
                    row.imageHref,
                    factCategoryId
                )
                factDAO.insertFact(fact)
            }
        }
        return@withContext true
    }

    @ExperimentalStdlibApi
    override fun generateHash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        val result = StringBuilder(bytes.size)
        bytes.forEach { byte ->
            val i = byte.toInt()
            result.append(ILocalDataSource.HEX_CHARS[i shr 4 and 0xF])
            result.append(ILocalDataSource.HEX_CHARS[i and 0XF])
        }

        return bytes.decodeToString()
    }


    companion object {
        val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    }
}