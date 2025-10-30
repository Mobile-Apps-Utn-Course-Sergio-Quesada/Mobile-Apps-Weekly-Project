package Data.DataManager

import Data.IDataManager.ICommentDataManager
import Entity.Comment

object MemoryDataManagerComment : ICommentDataManager {
    private var commentList = mutableListOf<Comment>()

    override fun add(comment: Comment) {
        commentList.add(comment)
    }

    override fun update(comment: Comment) {
        remove(comment.Content)
        add(comment)
    }

    override fun remove(id: String) {
        commentList.removeIf { it.Content.trim() == id.trim() }
    }

    override fun getById(id: String): Comment? {
        try {
            var result = commentList.filter { it.Content == id }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAll(): List<Comment> = commentList

    override fun getByContent(content: String): Comment? {
        try {
            var result = commentList.filter { it.Content == content }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}

