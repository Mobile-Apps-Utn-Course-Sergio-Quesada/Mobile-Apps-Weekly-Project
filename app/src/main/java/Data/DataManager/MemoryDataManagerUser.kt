package Data.DataManager

import Data.IDataManager.IUserDataManager
import Entity.User

object MemoryDataManagerUser : IUserDataManager {
    private var userList = mutableListOf<User>()

    override fun add(user: User) {
        userList.add(user)
    }

    override fun update(user: User) {
        remove(user.Username)
        add(user)
    }

    override fun remove(id: String) {
        userList.removeIf { it.Username.trim() == id.trim() }
    }

    override fun getAll(): List<User> = userList

    override fun getByUsername(fullName: String): User? {
        try {
            var result = userList.filter { it.FullName == fullName }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}
