package br.com.jpm.netflixrmk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jpm.netflixrmk.model.Movie

class MainActivity : AppCompatActivity() {
    private lateinit var rv : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movies = mutableListOf<Movie>()
        for (i in 0 until 60){
            val movie = Movie(R.drawable.movie)
            movies.add(movie)
        }
        val adapter = MainAdapter(movies)
        rv = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        rv.adapter = adapter
    }


}