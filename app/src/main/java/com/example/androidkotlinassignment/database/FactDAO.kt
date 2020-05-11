package com.example.androidkotlinassignment.database

import androidx.room.*
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategoryDB

@Dao
interface FactDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFact(Fact: Fact)

    @Query("select * from facts where sectionId=:id order by title")
    fun allFacts(id:String): List<Fact>

    @Query("select title from Facts where title=:Fact order by title")
    fun findFact(Fact:String):String

    @Update
    fun update(Fact: Fact)

    @Delete
    fun delete(Fact: Fact)

    @Query("select id, title from sections")
    fun getSection(): FactCategoryDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSection(factCategoryDB: FactCategoryDB)

    @Update
    fun update(factCategoryDB: FactCategoryDB)

    @Delete
    fun delete(factCategoryDB: FactCategoryDB)
}