package cr.ac.utn.spots

import Controller.UserController
import Entity.User
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity: AppCompatActivity() {
    lateinit var txtUsername: EditText
    lateinit var txtFullName: EditText
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    private lateinit var userController: UserController
    private var isEditMode: Boolean = false
    private lateinit var menuItemDelete: MenuItem

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userController = UserController(this)
        txtUsername = findViewById<EditText>(R.id.txtUsername)
        txtFullName = findViewById<EditText>(R.id.txtFullName)
        txtEmail = findViewById<EditText>(R.id.txtEmail)
        txtPassword = findViewById<EditText>(R.id.txtPassword)

        val btnSearch = findViewById<ImageButton>(R.id.btnSearchUser)
        btnSearch.setOnClickListener(View.OnClickListener{view ->
            searchUser(txtUsername.text.trim().toString())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_registry, menu)
        menuItemDelete = menu!!.findItem(R.id.mnu_delete)
        menuItemDelete.isVisible = isEditMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnu_save -> {
                if(isEditMode){
                    Util.Util.showDialogCondition(this, getString(R.string.TextSaveActionQuestion), {saveUser()})
                } else {
                    saveUser()
                }
                true
            }
            R.id.mnu_delete -> {
                Util.Util.showDialogCondition(this, getString(R.string.TextDeleteActionQuestion), {deleteUser()})
                true
            }
            R.id.mnu_cancel -> {
                cleanScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cleanScreen(){
        isEditMode = false
        txtUsername.isEnabled = true
        txtUsername.setText("")
        txtFullName.setText("")
        txtEmail.setText("")
        txtPassword.setText("")
        invalidateOptionsMenu()
    }

    private fun searchUser(username: String) {
        try {
            val user = userController.getUserByUsername(username)
            if(user != null){
                isEditMode = true
                txtUsername.setText(user.Username)
                txtUsername.isEnabled = false
                txtFullName.setText(user.FullName)
                txtPassword.setText(user.Password)
                txtEmail.setText(user.Email)
                menuItemDelete.isVisible = true
            } else {
                Toast.makeText(this, getString(R.string.errorMsgDataWasNotFound),
                    Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception){
            cleanScreen()
            Toast.makeText(this, e.message.toString(),
                Toast.LENGTH_LONG).show()
        }
    }

    fun saveUser() {
        try {
            if (isValidationData()){
                if(userController.getUserByUsername(txtUsername.text.toString().trim()) != null
                    && !isEditMode){
                    Toast.makeText(this, getString(R.string.MsgDuplicateEntry), Toast.LENGTH_LONG).show()
                } else {
                    val user = User()
                    user.Username = txtUsername.text.toString()
                    user.FullName = txtFullName.text.toString()
                    user.Email = txtEmail.text.toString()
                    user.Password = txtPassword.text.toString()
                    if(!isEditMode)
                        userController.addUser(user)
                    else
                        userController.updateUser(user)
                    cleanScreen()
                    Toast.makeText(this, getString(R.string.MsgSaveSuccess)
                        , Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "Datos incompletos", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun isValidationData(): Boolean {
        return txtUsername.text.trim().isNotEmpty() && txtEmail.text.trim().isNotEmpty() && txtPassword.text.trim().isNotEmpty()
                && txtFullName.text.trim().isNotEmpty();
    }

    fun deleteUser(): Unit{
        try {
            userController.removeUser(txtUsername.text.toString())
            cleanScreen()
            Toast.makeText(this, getString(R.string.MsgDeleteSuccess), Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}