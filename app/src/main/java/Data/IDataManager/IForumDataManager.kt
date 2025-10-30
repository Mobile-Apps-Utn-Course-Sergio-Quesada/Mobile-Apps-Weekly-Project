package Data.IDataManager

import Entity.Forum

interface IForumDataManager {
    fun add(forum: Forum)
    fun update(forum: Forum)
    fun remove(id: String)
    fun getById(id: String): Forum?
    fun getAll(): List<Forum>
    fun getByName(name: String): Forum?
}
