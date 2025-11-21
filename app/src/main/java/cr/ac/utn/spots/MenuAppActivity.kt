package cr.ac.utn.spots

import Data.DataManager.MemoryDataManagerPost
import Data.DataManager.MemoryDataManagerForum
import Entity.Post
import Util.Util
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.utn.spots.adapters.PostAdapter

class MenuAppActivity : AppCompatActivity() {

    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var fabCreatePost: FloatingActionButton
    private lateinit var btnMyForums: Button
    private lateinit var btnExploreForums: Button
    private lateinit var btnMyProfile: Button
    private lateinit var searchView: SearchView
    private lateinit var postAdapter: PostAdapter

    private var currentUsername: String = ""
    private var postsList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_app)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el username del usuario logueado
        currentUsername = intent.getStringExtra(Util.EXTRA_MESSAGE_USERNAME) ?: "Guest"

        // Configurar la ActionBar
        supportActionBar?.title = "Spots - Community"
        supportActionBar?.subtitle = "Welcome, $currentUsername"

        // Inicializar vistas
        initializeViews()

        // Configurar RecyclerView
        setupRecyclerView()

        // Cargar posts iniciales
        loadAllPosts()

        // Configurar listeners
        setupListeners()
    }

    private fun initializeViews() {
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts)
        fabCreatePost = findViewById(R.id.fabCreatePost)
        btnMyForums = findViewById(R.id.btnMyForums)
        btnExploreForums = findViewById(R.id.btnExploreForums)
        btnMyProfile = findViewById(R.id.btnMyProfile)
        searchView = findViewById(R.id.searchViewPosts)
    }

    private fun setupRecyclerView() {
        recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        // ✅ CONECTADO: PostAdapter
        postAdapter = PostAdapter(postsList, this)
        recyclerViewPosts.adapter = postAdapter
    }

    private fun setupListeners() {
        // Botón flotante para crear nuevo post
        fabCreatePost.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }

        // Botón Mis Foros
        btnMyForums.setOnClickListener {
            val intent = Intent(this, MyForumsActivity::class.java)
            intent.putExtra(Util.EXTRA_MESSAGE_USERNAME, currentUsername)
            startActivity(intent)
        }

        // Botón Explorar Foros
        btnExploreForums.setOnClickListener {
            val intent = Intent(this, ExploreForumsActivity::class.java)
            startActivity(intent)
        }

        // Botón Mi Perfil
        btnMyProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(Util.EXTRA_MESSAGE_USERNAME, currentUsername)
            startActivity(intent)
        }

        // SearchView para buscar posts
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    searchPosts(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadAllPosts()
                } else {
                    searchPosts(newText)
                }
                return true
            }
        })
    }

    private fun loadAllPosts() {
        try {
            postsList = MemoryDataManagerPost.getAll().toMutableList()
            updateRecyclerView()

            if (postsList.isEmpty()) {
                Toast.makeText(
                    this,
                    "No hay posts disponibles. ¡Crea el primero!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al cargar posts: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun searchPosts(query: String) {
        try {
            val allPosts = MemoryDataManagerPost.getAll()
            postsList = allPosts.filter { post ->
                post.Title.contains(query, ignoreCase = true) ||
                        post.Content.contains(query, ignoreCase = true) ||
                        post.Forum.Name.contains(query, ignoreCase = true)
            }.toMutableList()

            updateRecyclerView()

            if (postsList.isEmpty()) {
                Toast.makeText(
                    this,
                    "No se encontraron posts con: $query",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error en la búsqueda: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateRecyclerView() {
        // ✅ CORREGIDO: Actualizar adapter correctamente
        postAdapter.updatePosts(postsList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                loadAllPosts()
                Toast.makeText(this, "Posts actualizados", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_filter_by_forum -> {
                showForumFilterDialog()
                true
            }
            R.id.menu_my_posts -> {
                filterMyPosts()
                true
            }
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showForumFilterDialog() {
        val forums = MemoryDataManagerForum.getAll()

        if (forums.isEmpty()) {
            Toast.makeText(this, "No hay foros disponibles", Toast.LENGTH_SHORT).show()
            return
        }

        val forumNames = forums.map { it.Name }.toTypedArray()

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Filtrar por Foro")
        builder.setItems(forumNames) { dialog, which ->
            val selectedForum = forums[which]
            filterByForum(selectedForum.Name)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNeutralButton("Ver Todos") { dialog, _ ->
            loadAllPosts()
            dialog.dismiss()
        }
        builder.show()
    }

    private fun filterByForum(forumName: String) {
        try {
            val allPosts = MemoryDataManagerPost.getAll()
            postsList = allPosts.filter { post ->
                post.Forum.Name == forumName
            }.toMutableList()

            updateRecyclerView()

            Toast.makeText(
                this,
                "Mostrando posts de: $forumName",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al filtrar: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun filterMyPosts() {
        try {
            val allPosts = MemoryDataManagerPost.getAll()
            postsList = allPosts.filter { post ->
                post.User.Username == currentUsername
            }.toMutableList()

            updateRecyclerView()

            if (postsList.isEmpty()) {
                Toast.makeText(
                    this,
                    "No tienes posts todavía",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Mostrando tus posts (${postsList.size})",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al filtrar: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun logout() {
        Util.showDialogCondition(
            this,
            "¿Estás seguro de que deseas cerrar sesión?"
        ) {
            // Limpiar datos de sesión si es necesario
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar posts cuando se vuelve a esta actividad
        loadAllPosts()
    }
}