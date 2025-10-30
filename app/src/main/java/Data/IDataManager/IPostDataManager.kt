package Data.IDataManager

import Entity.Post

interface IPostDataManager {
    fun add(post: Post)
    fun update(post: Post)
    fun remove(id: String)
    fun getById(id: String): Post?
    fun getAll(): List<Post>
    fun getByTitle(title: String): Post?
}
