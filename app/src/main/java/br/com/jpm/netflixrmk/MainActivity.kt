package br.com.jpm.netflixrmk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jpm.netflixrmk.model.Category
import br.com.jpm.netflixrmk.model.Movie
import br.com.jpm.netflixrmk.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {
    private lateinit var progress: ProgressBar
    private lateinit var rv: RecyclerView
    private lateinit var adapter : CategoryAdapter
    private val categories = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progress = findViewById(R.id.progress_main)
//        for (j in 0 until 10) {
//            val movies = mutableListOf<Movie>()
//            for (i in 0 until 60){
//                val movie = Movie(R.drawable.movie)
//                movies.add(movie)
//            }
//            val category = Category("cat $j", movies)
//            categories.add(category)

        adapter = CategoryAdapter(categories){id ->
            val intent = Intent(this@MainActivity, MovieActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
        rv = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.adapter = adapter

        CategoryTask(this).execute2("https://api.tiagoaguiar.co/netflixapp/home?apiKey=168a7a06-d557-4f49-90dd-0a07769c8699")
    }

    override fun onPreExcute() {
        progress.visibility = View.VISIBLE
    }

    override fun onResult(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged()
        progress.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        progress.visibility = View.GONE
    }
}
