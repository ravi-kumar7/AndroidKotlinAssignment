package com.example.androidkotlinassignment.source

import com.example.androidkotlinassignment.models.Model
import com.example.androidkotlinassignment.source.remote.IRemoteDataSource

class FakeRemoteDataSource(
    var isConnected: Boolean,
    var response: ArrayList<Model.Row>,
    var title: String
) : IRemoteDataSource {

    override suspend fun getDataFromAPI(): Result<Model.Result> {
        return if (isConnected)
            Result.Success(Model.Result(title, response))
        else
            Result.Error(Exception("Not able to connect"))
    }
}