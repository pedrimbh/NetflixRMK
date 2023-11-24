package br.com.jpm.netflixrmk.model

data class MovieDetail(
    val movie: Movie,
    val similars: List<Movie>
)
