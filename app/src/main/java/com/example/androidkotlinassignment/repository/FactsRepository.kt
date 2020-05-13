package com.example.androidkotlinassignment.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.R
import com.example.androidkotlinassignment.database.FactDAO
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.models.Model
import com.example.androidkotlinassignment.network.FactAPIService
import com.example.androidkotlinassignment.utility.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.CoroutineContext


class FactsRepository(private val context: Context, private val factDAO: FactDAO) :
    Callback<Model.Result>, CoroutineScope {

    private val mJob: Job = Job()

    private val status: MutableLiveData<String> = MutableLiveData()
    private val section: LiveData<FactCategory> = factDAO.getFactCategory()
    private val allFacts = factDAO.allFacts()
    private val isDataSynced: MutableLiveData<Boolean> = MutableLiveData(false)

    override val coroutineContext: CoroutineContext get() = mJob + Dispatchers.Main
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://${context.getString(R.string.api_server)}")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getFactCategory(): LiveData<FactCategory> {
        return section
    }

    fun getFacts(): LiveData<List<Fact>> {
        return allFacts
    }

    fun getStatus(): MutableLiveData<String> {
        return status
    }

    fun setStatus(status: String?) {
        this.status.postValue(status)
    }

    fun isDataSynced(): MutableLiveData<Boolean> {
        return isDataSynced
    }

    /* function to create new network API request
     */
    fun createAPIRequestForDataSync() {
        if (!NetworkUtility.isAPIAvailable(context)) {
            setStatus(context.getString(R.string.serverIssue))
            return
        }
        val factAPIService = retrofit.create(FactAPIService::class.java)
        val factsCallObject = factAPIService.getFactsCallObject()
        factsCallObject.enqueue(this)
    }

    override fun onFailure(call: Call<Model.Result>, t: Throwable) {
        setStatus(t.message)
    }

    @ExperimentalStdlibApi
    override fun onResponse(call: Call<Model.Result>, response: Response<Model.Result>) {
        if (response.code() == HttpsURLConnection.HTTP_OK) {
            syncDB(response.body())
        } else {
            setStatus(response.message())
        }
    }


    /* function syncs offline DB with the received response
     * @param data: response from the network API
     */
    @ExperimentalStdlibApi
    private fun syncDB(data: Model.Result?) {
        val factCategoryId = UUID.randomUUID().toString()
        val factCategoryTitle = data!!.title
        val factCategoryDB = FactCategory(factCategoryId, factCategoryTitle)
        launch(Dispatchers.IO) {
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
            // status.postValue(context.getString(R.string.dataSyncSuccess))
            isDataSynced.postValue(true)
        }
        Toast.makeText(context, context.getString(R.string.dataSyncSuccess), Toast.LENGTH_LONG)
            .show()
    }


    /* function to call repository to sync offline data using Network API
     * @param input: a input string to generate the hash.
     * @return string: SHA-1 hash value of the input string
     */
    @ExperimentalStdlibApi
    private fun generateHash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        val result = StringBuilder(bytes.size)
        bytes.forEach { byte ->
            val i = byte.toInt()
            result.append(HEX_CHARS[i shr 4 and 0xF])
            result.append(HEX_CHARS[i and 0XF])
        }

        return bytes.decodeToString()
    }

    companion object {
        val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    }
}