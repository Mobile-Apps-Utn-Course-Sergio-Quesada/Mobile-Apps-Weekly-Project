package Data.IDataManager

import Entity.Favorite
import Entity.User

interface IFavoriteDataManager {
    fun add(favorite: Favorite)
    fun update(favorite: Favorite)
    fun remove(id: String)
    fun getById(id: String): Favorite?
    fun getAll(): List<Favorite>
    fun getByUser(user: User): Favorite?
}
