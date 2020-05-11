package com.example.androidkotlinassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class FactCategoryDB(@PrimaryKey var id:String, var title:String) {
}