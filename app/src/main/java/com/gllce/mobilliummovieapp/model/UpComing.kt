package com.gllce.mobilliummovieapp.model

data class UpComing(
    val dates: Dates,
    val page: Int,
    val results: MutableList<Movie>,
    val total_pages: Int,
    val total_results: Int
)