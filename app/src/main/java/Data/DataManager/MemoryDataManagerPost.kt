package Data.DataManager

import Data.IDataManager.IPostDataManager
import Entity.Post

object MemoryDataManagerPost : IPostDataManager {
    private var postList = mutableListOf<Post>()

    override fun add(post: Post) {
        postList.add(post)
    }

    override fun update(post: Post) {
        remove(post.Title)
        add(post)
    }

    override fun remove(id: String) {
        postList.removeIf { it.Title.trim() == id.trim() }
    }

    override fun getById(id: String): Post? {
        try {
            var result = postList.filter { it.Title == id }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAll(): List<Post> = postList

    override fun getByTitle(title: String): Post? {
        try {
            var result = postList.filter { it.Title == title }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}
