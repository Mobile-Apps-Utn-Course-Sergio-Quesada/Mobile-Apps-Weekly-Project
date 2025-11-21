package cr.ac.utn.spots.adapters

import Entity.Post
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.spots.PostActivity
import cr.ac.utn.spots.R
import Util.Util

class PostAdapter(
    private var posts: MutableList<Post>,
    private val context: Context
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtPostUsername: TextView = itemView.findViewById(R.id.txtPostUsername)
        val txtPostForum: TextView = itemView.findViewById(R.id.txtPostForum)
        val txtPostTitle: TextView = itemView.findViewById(R.id.txtPostTitle)
        val txtPostContent: TextView = itemView.findViewById(R.id.txtPostContent)
        val imgPostImage: ImageView = itemView.findViewById(R.id.imgPostImage)
        val txtVoteCount: TextView = itemView.findViewById(R.id.txtVoteCount)
        val txtCommentCount: TextView = itemView.findViewById(R.id.txtCommentCount)
        val btnUpvote: ImageButton = itemView.findViewById(R.id.btnUpvote)
        val btnDownvote: ImageButton = itemView.findViewById(R.id.btnDownvote)
        val btnComment: ImageButton = itemView.findViewById(R.id.btnComment)
        val btnShare: ImageButton = itemView.findViewById(R.id.btnShare)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        // Establecer información básica
        holder.txtPostUsername.text = "@${post.User.Username}"
        holder.txtPostForum.text = "in ${post.Forum.Name}"
        holder.txtPostTitle.text = post.Title
        holder.txtPostContent.text = post.Content

        // Mostrar imagen si existe
        if (post.Photo != null) {
            holder.imgPostImage.visibility = View.VISIBLE
            holder.imgPostImage.setImageBitmap(post.Photo)
        } else if (post.ImageUrl.isNotEmpty()) {
            holder.imgPostImage.visibility = View.VISIBLE
            // Aquí puedes usar una librería como Glide o Picasso para cargar desde URL
            // Por ahora solo mostramos un placeholder
        } else {
            holder.imgPostImage.visibility = View.GONE
        }

        // Configurar contadores (placeholder por ahora)
        holder.txtVoteCount.text = "0" // Aquí irían los votos reales
        holder.txtCommentCount.text = "0" // Aquí irían los comentarios reales

        // Click en el card completo para ver detalles
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra(Util.EXTRA_MESSAGE_POSTID, post.ID)
            context.startActivity(intent)
        }

        // Botón Upvote
        holder.btnUpvote.setOnClickListener {
            Toast.makeText(context, "Upvote en: ${post.Title}", Toast.LENGTH_SHORT).show()
            // Aquí implementarías la lógica de votar
            val currentVotes = holder.txtVoteCount.text.toString().toIntOrNull() ?: 0
            holder.txtVoteCount.text = (currentVotes + 1).toString()
        }

        // Botón Downvote
        holder.btnDownvote.setOnClickListener {
            Toast.makeText(context, "Downvote en: ${post.Title}", Toast.LENGTH_SHORT).show()
            // Aquí implementarías la lógica de votar
            val currentVotes = holder.txtVoteCount.text.toString().toIntOrNull() ?: 0
            holder.txtVoteCount.text = (currentVotes - 1).toString()
        }

        // Botón Comentar
        holder.btnComment.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra(Util.EXTRA_MESSAGE_POSTID, post.ID)
            intent.putExtra("FOCUS_COMMENT", true)
            context.startActivity(intent)
        }

        // Botón Compartir
        holder.btnShare.setOnClickListener {
            val shareText = "${post.Title}\n\n${post.Content}\n\n- Compartido desde Spots"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartir post"))
        }

        // Botón Favorito
        var isFavorite = false
        holder.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                holder.btnFavorite.setImageResource(android.R.drawable.star_big_on)
                Toast.makeText(context, "Añadido a favoritos", Toast.LENGTH_SHORT).show()
            } else {
                holder.btnFavorite.setImageResource(android.R.drawable.star_big_off)
                Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }
            // Aquí implementarías la lógica de favoritos
        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: MutableList<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}