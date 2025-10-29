package Data.IDataManager

import Entity.User

interface IUserDataManager {
    fun add(user: User)
    fun update(user: User)
    fun remove(id: String)
    fun getById(id: String): User?
    fun getAll(): List<User>
    fun getByFullName(fullName: String): User?
}
