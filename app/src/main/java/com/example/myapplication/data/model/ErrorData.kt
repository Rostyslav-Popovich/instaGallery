package com.example.myapplication.data.model

data class ErrorData(
    val code: Int,
    val fbtrace_id: String,
    val message: String,
    val type: String
)