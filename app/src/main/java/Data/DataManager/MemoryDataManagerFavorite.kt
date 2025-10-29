package Data.DataManager

import Data.IDataManager.IFavoriteDataManager
import Entity.Favorite
import Entity.User

object MemoryDataManagerFavorite : IFavoriteDataManager {
    private var favoriteList = mutableListOf<Favorite>()

    override fun add(favorite: Favorite) {
        favoriteList.add(favorite)
    }

    override fun update(favorite: Favorite) {
        remove(favorite.User.Username)
        add(favorite)
    }

    override fun remove(id: String) {
        favoriteList.removeIf { it.User.Username.trim() == id.trim() }
    }

    override fun getById(id: String): Favorite? {
        try {
            var result = favoriteList.filter { it.User.Username == id }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
    override fun getAll(): List<Favorite> = favoriteList

    override fun getByUser(user: User): Favorite? {
        try {
            var result = favoriteList.filter { it.User.Username == user.Username }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}
