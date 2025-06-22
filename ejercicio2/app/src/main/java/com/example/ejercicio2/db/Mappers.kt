package com.example.ejercicio2.db

import com.example.ejercicio2.Book

fun Book.toEntity(): BookEntity {
    return BookEntity(
        key = this.key,      // ya es String, no nullable
        title = this.title,
        author = this.author
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        title = this.title ?: "Sin título",
        author = this.author ?: "Desconocido",
        key = this.key
    )
}

