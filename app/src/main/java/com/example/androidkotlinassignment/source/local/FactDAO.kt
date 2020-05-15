package com.example.androidkotlinassignment.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory

@Dao
interface FactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFact(Fact: Fact)

    @Query("select * from facts ")
    fun allFacts(): LiveData<List<Fact>>

    @Query("select id, title from categories")
    fun getFactCategory(): LiveData<FactCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSection(factCategory: FactCategory)
}