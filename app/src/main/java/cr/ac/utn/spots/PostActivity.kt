package cr.ac.utn.spots

import Controller.PostController
import Data.DataManager.MemoryDataManagerPost
import Entity.Forum
import Entity.Post
import Entity.User
import Util.Util
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PostActivity : AppCompatActivity() {

    private lateinit var edtPostId: EditText
    private lateinit var edtPostTitle: EditText
    private lateinit var edtPostContent: EditText
    private lateinit var edtUserName: EditText
    private lateinit var edtForumName: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var postController: PostController
    private var isEditMode: Boolean = false
    private var imageUri: Uri? = null
    private lateinit var menuItemDelete: MenuItem

    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        postController = PostController(MemoryDataManagerPost, this)

        edtPostId = findViewById(R.id.edtPostId)
        edtPostTitle = findViewById(R.id.edtPostTitle)
        edtPostContent = findViewById(R.id.edtPostContent)
        edtUserName = findViewById(R.id.edtUserName)
        edtForumName = findViewById(R.id.edtForumName)
        imgPreview = findViewById(R.id.imgPreview)

        initializeLaunchers()


        val postId = intent.getStringExtra(Util.EXTRA_MESSAGE_POSTID)
        if (postId != null && postId.trim().isNotEmpty()) {
            searchPost(postId)
        }

        val btnSearchId = findViewById<ImageButton>(R.id.btnSearchId_post)
        btnSearchId.setOnClickListener(View.OnClickListener { view ->
            searchPost(edtPostId.text.trim().toString())
        })

        val btnTakePhoto = findViewById<ImageButton>(R.id.btnTakePhoto)
        btnTakePhoto.setOnClickListener(View.OnClickListener { view ->
            takePhoto()
        })

        val btnSelectFromGallery = findViewById<ImageButton>(R.id.btnSelectFromGallery)
        btnSelectFromGallery.setOnClickListener(View.OnClickListener { view ->
            selectPhoto()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_registry, menu)
        menuItemDelete = menu!!.findItem(R.id.mnu_delete)
        menuItemDelete.isVisible = isEditMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                if (isEditMode) {
                    Util.showDialogCondition(
                        this,
                        getString(R.string.TextSaveActionQuestion),
                        { savePost() }
                    )
                } else {
                    savePost()
                }
                return true
            }
            R.id.mnu_delete -> {
                Util.showDialogCondition(
                    this,
                    getString(R.string.TextDeleteActionQuestion),
                    { deletePost() }
                )
                return true
            }
            R.id.mnu_cancel -> {
                cleanScreen()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeLaunchers() {
        // Camera launcher (TakePicturePreview)
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->
            if (bitmap != null) {
                imgPreview.setImageBitmap(bitmap)
                imageUri = saveBitmapToGallery(bitmap)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.ErrorMsgCameraCapture),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                imgPreview.setImageURI(uri)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.ErrorMsgGallerySelection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun searchPost(id: String) {
        try {
            val post = postController.getById(id)
            if (post != null) {
                isEditMode = true
                edtPostId.setText(post.ID)
                edtPostId.isEnabled = false
                edtPostTitle.setText(post.Title)
                edtPostContent.setText(post.Content)

                try {
                    edtUserName.setText(post.User.Username)
                } catch (e: Exception) {
                    edtUserName.setText("")
                }

                try {
                    edtForumName.setText(post.Forum.Name)
                } catch (e: Exception) {
                    edtForumName.setText("")
                }

                if (post.Photo != null) {
                    imgPreview.setImageBitmap(post.Photo)
                } else if (post.ImageUrl.isNotEmpty()) {
                    imageUri = Uri.parse(post.ImageUrl)
                    imgPreview.setImageURI(imageUri)
                }

                invalidateOptionsMenu()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.MsgDataNoFound),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            cleanScreen()
            Toast.makeText(
                this,
                e.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isValidationData(): Boolean {
        return edtPostId.text.trim().isNotEmpty() &&
                edtPostTitle.text.trim().isNotEmpty() &&
                edtPostContent.text.trim().isNotEmpty() &&
                edtUserName.text.trim().isNotEmpty() &&
                edtForumName.text.trim().isNotEmpty()
    }

    private fun cleanScreen() {
        isEditMode = false
        edtPostId.isEnabled = true
        edtPostId.setText("")
        edtPostTitle.setText("")
        edtPostContent.setText("")
        edtUserName.setText("")
        edtForumName.setText("")
        imgPreview.setImageBitmap(null)
        imageUri = null
        invalidateOptionsMenu()
    }

    private fun savePost() {
        try {
            if (isValidationData()) {
                if (postController.getById(edtPostId.text.toString().trim()) != null
                    && !isEditMode
                ) {
                    Toast.makeText(
                        this,
                        getString(R.string.MsgDuplicateDate),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val user = User()
                    user.Username = edtUserName.text.toString()

                    val forum = Forum()
                    forum.Name = edtForumName.text.toString()

                    val post = Post()
                    post.ID = edtPostId.text.toString()
                    post.Title = edtPostTitle.text.toString()
                    post.Content = edtPostContent.text.toString()
                    post.ImageUrl = imageUri?.toString() ?: ""

                    // Intentar obtener el bitmap de la imagen
                    try {
                        post.Photo = (imgPreview.drawable as? BitmapDrawable)?.bitmap
                    } catch (e: Exception) {
                        post.Photo = null
                    }

                    post.User = user
                    post.Forum = forum

                    if (!isEditMode) {
                        postController.addPost(post)
                    } else {
                        postController.updatePost(post)
                    }

                    cleanScreen()

                    Toast.makeText(
                        this,
                        getString(R.string.MsgSaveSuccess),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.MsgIncompleteData),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                e.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deletePost() {
        try {
            postController.removePost(edtPostId.text.toString())
            cleanScreen()
            Toast.makeText(
                this,
                getString(R.string.MsgDeleteSuccess),
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                e.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun takePhoto() {
        cameraLauncher.launch(null)
    }

    private fun selectPhoto() {
        galleryLauncher.launch("image/*")
    }

    private fun saveBitmapToGallery(bitmap: Bitmap): Uri? {
        return try {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "post_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }

            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )

            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }

            uri
        } catch (e: Exception) {
            Toast.makeText(
                this,
                getString(R.string.ErrorMsgSavingImage),
                Toast.LENGTH_SHORT
            ).show()
            null
        }
    }
}