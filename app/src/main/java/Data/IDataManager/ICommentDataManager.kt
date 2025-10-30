package Data.IDataManager

import Entity.Comment

interface ICommentDataManager {
    fun add(comment: Comment)
    fun update(comment: Comment)
    fun remove(id: String)
    fun getById(id: String): Comment?
    fun getAll(): List<Comment>
    fun getByContent(content: String): Comment?
}
