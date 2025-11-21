package Data.IDataManager

import Entity.User

interface IUserDataManager {
    fun add(user: User)
    fun update(user: User)
    fun remove(username: String)
    fun getAll(): List<User>
    fun getByUsername(username: String): User?
}