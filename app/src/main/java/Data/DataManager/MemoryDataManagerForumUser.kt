package Data.DataManager

import Data.IDataManager.IForumUserDataManager
import Entity.Forum_user

object MemoryDataManagerForumUser : IForumUserDataManager {
    private var forumUserList = mutableListOf<Forum_user>()

    override fun add(forumUser: Forum_user) {
        forumUserList.add(forumUser)
    }

    override fun update(forumUser: Forum_user) {
        remove(forumUser.User.Username)
        add(forumUser)
    }

    override fun remove(id: String) {
        forumUserList.removeIf { it.User.Username.trim() == id.trim() }
    }

    override fun getById(id: String): Forum_user? {
        try {
            var result = forumUserList.filter { it.User.Username == id }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAll(): List<Forum_user> = forumUserList

    override fun getByRole(role: String): Forum_user? {
        try {
            var result = forumUserList.filter { it.Role == role }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}
