package com.example.ejercicio2

data class Book(
    val title: String,
    val author: String,
    val key: String // clave única del libro
)

data class BookResponse(
    val docs: List<Book>
)
