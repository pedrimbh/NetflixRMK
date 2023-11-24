package br.com.jpm.netflixrmk

import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jpm.netflixrmk.model.Movie
import br.com.jpm.netflixrmk.model.MovieDetail
import br.com.jpm.netflixrmk.util.MovieTask
import java.lang.IllegalStateException

class MovieActivity : AppCompatActivity(), MovieTask.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val txtTitle: TextView = findViewById(R.id.movie_txt_title)
        val txtDesc: TextView = findViewById(R.id.movie_txt_desc)
        val txtCast: TextView = findViewById(R.id.movie_txt_cast)
        val rv: RecyclerView = findViewById(R.id.movie_rv_similar)
        val movies = mutableListOf<Movie>()
//        for (i in 0 until 60) {
//            val movie = Movie(R.drawable.movie)
//            movies.add(movie)
//        }

        val id = intent?.getIntExtra("id",0) ?: throw IllegalStateException("ID nao foi encontrado")

        val url = "https://api.tiagoaguiar.co/netflixapp/movie/$id?apiKey=168a7a06-d557-4f49-90dd-0a07769c8699"

        MovieTask(this).execute(url)


        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = MovieAdapter(movies, R.layout.movie_item_similar)
        txtTitle.text = "Batman Begins"
        txtDesc.text = "Essa e uma descricao dp filme Batman"
        txtCast.text = getString(R.string.cast, "Ator x y zeta")

        val toolBar: Toolbar = findViewById(R.id.movie_tool_bar)
        setSupportActionBar(toolBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        val layerDrawable: LayerDrawable =
            ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable
        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4)
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
        val coverImg: ImageView = findViewById(R.id.movie_img)
        coverImg.setImageDrawable(layerDrawable)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreExcute() {
        TODO("Not yet implemented")
    }

    override fun onResult(movieDetail: MovieDetail) {
        Log.i("Teste", movieDetail.toString())
    }

    override fun onFailure(message: String) {
        TODO("Not yet implemented")
    }
}