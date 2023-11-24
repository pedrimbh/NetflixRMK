package br.com.jpm.netflixrmk.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.jpm.netflixrmk.model.Category
import br.com.jpm.netflixrmk.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOError
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask(private val callback: Callback) {
    private val handler = Handler(Looper.getMainLooper())
    interface Callback {
        fun onPreExcute()
        fun onResult(categories: List<Category>)
        fun onFailure(message : String)
    }

    fun execute(url: String) {
        callback.onPreExcute()
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {
                val requestURL = URL(url)
                val urlConnection = requestURL.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 2000 // 2s tempo de leitura se nao quebra
                urlConnection.connectTimeout = 2000 // tempo de conexáo

                val statusCode = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicacao com o servidor")
                }
                val stream = urlConnection.inputStream
                val jsonAsString = stream.bufferedReader().use {
                    it.readText()
                }
                Log.i("Teste", jsonAsString)
            } catch (e: IOException) {
                Log.e("Teste", e.message ?: "erro desconhecido", e)
            }

        }
    }

    fun execute2(url: String) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            var urlConnection: HttpsURLConnection? = null
            var buffer: BufferedInputStream? = null
            var stream: InputStream? = null
            try {
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000 // 2s tempo de leitura se nao quebra
                urlConnection.connectTimeout = 2000 // tempo de conexáo

                val statusCode = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicacao com o servidor")
                }
                stream = urlConnection.inputStream
                buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)
                val categories = toCategories(jsonAsString)
                handler.post {
                    // ui thrad
                    callback.onResult(categories)
                }

            } catch (e: IOException) {
                val message = "erro desconhecido"
                Log.e("Teste", e.message ?: message, e)
                handler.post {
                    callback.onFailure(message)
                }
            } finally {
                urlConnection?.disconnect()
                stream?.close()
                buffer?.close()
            }

        }
    }

    private fun toCategories(jsonAsString: String): List<Category> {
        val categories = mutableListOf<Category>()
        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories = jsonRoot.getJSONArray("category")
        for (i in 0 until jsonCategories.length()) {
            val jsonCategory = jsonCategories.getJSONObject(i)
            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")
            val movies = mutableListOf<Movie>()
            for (j in 0 until jsonMovies.length()) {
                val jsonMovie = jsonMovies.getJSONObject(j)
                val id = jsonMovie.getInt("id")
                val coverUrl = jsonMovie.getString("cover_url")
                movies.add(Movie(id, coverUrl))
            }
            categories.add(Category(title, movies))
        }
        return categories
    }

    private fun toString(stream: InputStream): String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int
        while (true) {
            read = stream.read(bytes)
            if (read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }
}