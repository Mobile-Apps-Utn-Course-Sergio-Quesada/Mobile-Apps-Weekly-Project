package Data.DataManager

import Data.IDataManager.IForumDataManager
import Entity.Forum

object MemoryDataManagerForum : IForumDataManager {
    private var forumList = mutableListOf<Forum>()

    override fun add(forum: Forum) {
        forumList.add(forum)
    }

    override fun update(forum: Forum) {
        remove(forum.Name)
        add(forum)
    }

    override fun remove(id: String) {
        forumList.removeIf { it.Name.trim() == id.trim() }
    }

    override fun getById(id: String): Forum? {
        try {
            var result = forumList.filter { it.Name == id }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAll(): List<Forum> = forumList

    override fun getByName(name: String): Forum? {
        try {
            var result = forumList.filter { it.Name == name }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}
