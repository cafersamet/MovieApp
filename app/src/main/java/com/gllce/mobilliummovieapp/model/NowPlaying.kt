package com.gllce.mobilliummovieapp.model

data class NowPlaying(
    val dates: Dates,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)