package Data.DataManager

import Data.IDataManager.IPostDataManager
import Entity.Post

object MemoryDataManagerPost : IPostDataManager {
    private var postList = mutableListOf<Post>()

    override fun add(post: Post) {
        postList.add(post)
    }

    override fun update(post: Post) {
        remove(post.ID)
        add(post)
    }

    override fun remove(id: String) {
        postList.removeIf { it.ID.trim() == id.trim() }
    }

    override fun getById(id: String): Post? {
        return try {
            val result = postList.filter { it.ID.trim() == id.trim() }
            if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAll(): List<Post> = postList

    override fun getByTitle(title: String): Post? {
        return try {
            val result = postList.filter { it.Title.trim() == title.trim() }
            if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}