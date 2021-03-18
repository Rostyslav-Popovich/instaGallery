package com.example.myapplication.data.model

data class Paging(
    val cursors: Cursors,
    val next: String,
    val previous: String
)