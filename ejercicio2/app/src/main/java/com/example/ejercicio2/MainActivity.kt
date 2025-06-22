package com.example.ejercicio2


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ejercicio2.BookAdapter
import com.example.ejercicio2.databinding.ActivityMainBinding
import com.example.ejercicio2.Book
import com.example.ejercicio2.BookResponse
import com.example.ejercicio2.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.ejercicio2.db.BookDatabase
import androidx.lifecycle.lifecycleScope
import com.example.ejercicio2.db.toEntity
import com.example.ejercicio2.db.toBook
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import android.widget.Toast



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private var bookList: MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(bookList) { book ->
            // Mostrar diálogo de confirmación para guardar
            AlertDialog.Builder(this)
                .setTitle("Guardar libro")
                .setMessage("¿Deseas guardar \"${book.title}\" para verlo sin conexión?")
                .setPositiveButton("Sí") { _, _ ->
                    saveBooksToDatabase(listOf(book))
                    Toast.makeText(this, "Libro guardado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }

        binding.recyclerView.adapter = bookAdapter

        fetchBooks("android") // Cargar libros por defecto
    }

    private fun fetchBooks(query: String) {
        RetrofitClient.instance.searchBooks(query).enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val books = response.body()?.docs ?: emptyList()

                    Toast.makeText(this@MainActivity, "Libros encontrados: ${books.size}", Toast.LENGTH_SHORT).show()
                    bookList.clear()
                    bookList.addAll(books)
                    bookAdapter.notifyDataSetChanged()

                    // Guardar localmente
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                // Si hay fallo de red, cargar desde base de datos
                loadBooksFromDatabase()
            }
        })
    }

    private fun saveBooksToDatabase(books: List<Book>) {
        val dao = BookDatabase.getDatabase(this).bookDao()
        lifecycleScope.launch {
            // Solo agrega, no borra
            dao.insertBooks(books.map { it.toEntity() })
        }
    }


    private fun loadBooksFromDatabase() {
        val dao = BookDatabase.getDatabase(this).bookDao()
        lifecycleScope.launch {
            val localBooks = dao.getAllBooks().map { it.toBook() }
            bookList.clear()
            bookList.addAll(localBooks)
            bookAdapter.notifyDataSetChanged()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        binding.bottomNavigation.selectedItemId = R.id.nav_buscar

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { fetchBooks(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_buscar -> {
                    fetchBooks("android") // o última búsqueda
                    binding.toolbar.title = "Buscar libros"
                    true
                }
                R.id.nav_guardados -> {
                    loadBooksFromDatabase()
                    binding.toolbar.title = "Libros guardados"
                    true
                }
                else -> false
            }
        }


        return true
    }
}
