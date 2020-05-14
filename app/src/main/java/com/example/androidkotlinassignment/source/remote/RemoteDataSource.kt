package com.example.androidkotlinassignment.source.remote

import android.content.Context
import com.example.androidkotlinassignment.R
import com.example.androidkotlinassignment.models.Model
import com.example.androidkotlinassignment.source.Result
import com.example.androidkotlinassignment.utility.NetworkUtility
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HttpsURLConnection

class RemoteDataSource(val context: Context) : IRemoteDataSource {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://${context.getString(R.string.api_server)}")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /* function to create new network API request
   */
    override suspend fun getDataFromAPI(): Result<Model.Result> {
        if (!NetworkUtility.isAPIAvailable(context)) {
            //setStatus(context.getString(R.string.serverIssue))
            return Result.Error(Exception(context.getString(R.string.serverIssue)))
        }
        val factAPIService = retrofit.create(FactAPIService::class.java)
        val factsCallObject = factAPIService.getFactsCallObject()
        val result = factsCallObject.execute()
        return if (result.code() == HttpsURLConnection.HTTP_OK)
            Result.Success(result.body()!!)
        else
            Result.Error(Exception(result.message()))
    }


}