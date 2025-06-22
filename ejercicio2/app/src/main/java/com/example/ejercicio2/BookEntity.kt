package com.example.ejercicio2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val key: String,
    val title: String?,
    val author: String?
)
