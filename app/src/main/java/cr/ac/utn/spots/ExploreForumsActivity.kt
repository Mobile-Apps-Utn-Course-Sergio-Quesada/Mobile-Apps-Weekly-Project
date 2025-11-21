package cr.ac.utn.spots

import Data.DataManager.MemoryDataManagerForum
import Entity.Forum
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExploreForumsActivity : AppCompatActivity() {

    private lateinit var recyclerViewForums: RecyclerView
    private lateinit var searchViewForums: SearchView
    private lateinit var layoutEmptyState: View
    private lateinit var txtEmptyMessage: TextView

    private var forumsList = mutableListOf<Forum>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_explore_forums)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar ActionBar
        supportActionBar?.title = "Explorar Foros"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar vistas
        initializeViews()

        // Configurar RecyclerView con Grid (2 columnas)
        setupRecyclerView()

        // Cargar todos los foros
        loadAllForums()

        // Configurar búsqueda
        setupSearch()
    }

    private fun initializeViews() {
        recyclerViewForums = findViewById(R.id.recyclerViewExploreForums)
        searchViewForums = findViewById(R.id.searchViewForums)
        layoutEmptyState = findViewById(R.id.layoutEmptyStateExplore)
        txtEmptyMessage = findViewById(R.id.txtEmptyExploreMessage)
    }

    private fun setupRecyclerView() {
        // Grid de 2 columnas para mejor visualización
        recyclerViewForums.layoutManager = GridLayoutManager(this, 2)
        // Descomentar cuando tengas ForumAdapter implementado
        // recyclerViewForums.adapter = ForumAdapter(forumsList, this, false)
    }

    private fun setupSearch() {
        searchViewForums.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    searchForums(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadAllForums()
                } else {
                    searchForums(newText)
                }
                return true
            }
        })
    }

    private fun loadAllForums() {
        try {
            forumsList = MemoryDataManagerForum.getAll().toMutableList()
            updateUI()

            if (forumsList.isEmpty()) {
                txtEmptyMessage.text = getString(R.string.empty_explore_forums_message)
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al cargar foros: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun searchForums(query: String) {
        try {
            val allForums = MemoryDataManagerForum.getAll()
            forumsList = allForums.filter { forum ->
                forum.Name.contains(query, ignoreCase = true) ||
                        forum.Description.contains(query, ignoreCase = true)
            }.toMutableList()

            updateUI()

            if (forumsList.isEmpty()) {
                txtEmptyMessage.text = "No se encontraron foros con: $query"
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error en la búsqueda: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUI() {
        if (forumsList.isEmpty()) {
            recyclerViewForums.visibility = View.GONE
            layoutEmptyState.visibility = View.VISIBLE
        } else {
            recyclerViewForums.visibility = View.VISIBLE
            layoutEmptyState.visibility = View.GONE
            recyclerViewForums.adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_explore_forums, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_refresh_explore -> {
                loadAllForums()
                Toast.makeText(this, "Foros actualizados", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_sort_by_name -> {
                sortForumsByName()
                true
            }
            R.id.menu_sort_by_members -> {
                Toast.makeText(this, "Ordenar por miembros (próximamente)", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sortForumsByName() {
        forumsList.sortBy { it.Name }
        recyclerViewForums.adapter?.notifyDataSetChanged()
        Toast.makeText(this, "Ordenado por nombre", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadAllForums()
    }
}