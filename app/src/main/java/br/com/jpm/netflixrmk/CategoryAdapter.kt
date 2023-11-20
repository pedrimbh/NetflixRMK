package br.com.jpm.netflixrmk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jpm.netflixrmk.model.Category
import br.com.jpm.netflixrmk.model.Movie

class CategoryAdapter(private val category:List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val movie = category[position]
        holder.bind(movie)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category){
            val categoryName : TextView = itemView.findViewById(R.id.text_title)
            val rvCategory : RecyclerView = itemView.findViewById(R.id.rv_category)
            categoryName.text = category.name
            rvCategory.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL,false)
            rvCategory.adapter = MovieAdapter(category.movie,R.layout.movie_item)
        }
    }

}