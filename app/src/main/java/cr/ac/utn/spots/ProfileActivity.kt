package cr.ac.utn.spots

import Data.DataManager.MemoryDataManagerPost
import Data.DataManager.MemoryDataManagerForumUser
import Data.DataManager.MemoryDataManagerUser
import Entity.User
import Util.Util
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {

    private lateinit var txtProfileUsername: TextView
    private lateinit var txtProfileEmail: TextView
    private lateinit var txtProfileFullName: TextView
    private lateinit var txtPostCount: TextView
    private lateinit var txtForumCount: TextView
    private lateinit var txtMemberSince: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnMyPosts: Button
    private lateinit var btnMyForums: Button

    private var currentUsername: String = ""
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentUsername = intent.getStringExtra(Util.EXTRA_MESSAGE_USERNAME) ?: "Guest"

        supportActionBar?.title = "Mi Perfil"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeViews()
        loadUserProfile()
        setupListeners()
    }

    private fun initializeViews() {
        txtProfileUsername = findViewById(R.id.txtProfileUsername)
        txtProfileEmail = findViewById(R.id.txtProfileEmail)
        txtProfileFullName = findViewById(R.id.txtProfileFullName)
        txtPostCount = findViewById(R.id.txtPostCount)
        txtForumCount = findViewById(R.id.txtForumCount)
        txtMemberSince = findViewById(R.id.txtMemberSince)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnMyPosts = findViewById(R.id.btnMyPosts)
        btnMyForums = findViewById(R.id.btnMyForums)
    }

    private fun setupListeners() {
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra(Util.EXTRA_MESSAGE_USERNAME, currentUsername)
            startActivity(intent)
        }

        btnMyPosts.setOnClickListener {
            // Volver al MenuAppActivity y filtrar por posts del usuario
            val intent = Intent(this, MenuAppActivity::class.java)
            intent.putExtra(Util.EXTRA_MESSAGE_USERNAME, currentUsername)
            intent.putExtra("FILTER_MY_POSTS", true)
            startActivity(intent)
        }

        btnMyForums.setOnClickListener {
            val intent = Intent(this, MyForumsActivity::class.java)
            intent.putExtra(Util.EXTRA_MESSAGE_USERNAME, currentUsername)
            startActivity(intent)
        }
    }

    private fun loadUserProfile() {
        try {
            currentUser = MemoryDataManagerUser.getByUsername(currentUsername)

            if (currentUser != null) {
                txtProfileUsername.text = "@${currentUser!!.Username}"
                txtProfileEmail.text = currentUser!!.Email
                txtProfileFullName.text = currentUser!!.FullName

                calculateStatistics()
            } else {
                // Si no se encuentra el usuario, mostrar información básica
                txtProfileUsername.text = "@$currentUsername"
                txtProfileEmail.text = "No disponible"
                txtProfileFullName.text = currentUsername
                txtMemberSince.text = "Miembro desde: Hoy"
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al cargar perfil: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()

            txtProfileUsername.text = "@$currentUsername"
            txtProfileEmail.text = "Error al cargar"
            txtProfileFullName.text = currentUsername
        }
    }

    private fun calculateStatistics() {
        try {
            val allPosts = MemoryDataManagerPost.getAll()
            val userPostCount = allPosts.count { it.User.Username == currentUsername }
            txtPostCount.text = "$userPostCount posts"

            val allForumUsers = MemoryDataManagerForumUser.getAll()
            val userForumCount = allForumUsers.count { it.User.Username == currentUsername }
            txtForumCount.text = "$userForumCount foros"

            txtMemberSince.text = "Miembro desde: 2024"
        } catch (e: Exception) {
            txtPostCount.text = "0 posts"
            txtForumCount.text = "0 foros"
            txtMemberSince.text = "Miembro desde: Hoy"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_refresh_profile -> {
                loadUserProfile()
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_settings -> {
                Toast.makeText(this, "Configuración (próximamente)", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_logout_profile -> {
                Util.showDialogCondition(
                    this,
                    "¿Estás seguro de que deseas cerrar sesión?"
                ) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }
}