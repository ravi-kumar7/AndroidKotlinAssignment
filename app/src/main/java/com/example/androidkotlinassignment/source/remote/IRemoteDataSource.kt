package com.example.androidkotlinassignment.source.remote

import com.example.androidkotlinassignment.models.Model
import com.example.androidkotlinassignment.source.Result

interface IRemoteDataSource {
    /* function to create new network API request
      */
    suspend fun getDataFromAPI(): Result<Model.Result>
}