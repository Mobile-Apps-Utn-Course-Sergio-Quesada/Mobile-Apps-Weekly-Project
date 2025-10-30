package Data.IDataManager

import Entity.Forum_user

interface IForumUserDataManager {
    fun add(forumUser: Forum_user)
    fun update(forumUser: Forum_user)
    fun remove(id: String)
    fun getById(id: String): Forum_user?
    fun getAll(): List<Forum_user>
    fun getByRole(role: String): Forum_user?
}
