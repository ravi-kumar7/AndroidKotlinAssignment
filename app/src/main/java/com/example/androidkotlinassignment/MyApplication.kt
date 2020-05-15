package com.example.androidkotlinassignment

import android.app.Application
import com.example.androidkotlinassignment.source.IFactsDataRepository

class MyApplication : Application() {

    val factsDataRepository: IFactsDataRepository
        get() = ServiceLocator.provideFactsRepository(this)
}