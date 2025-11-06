package Controller

import Data.DataManager.MemoryDataManagerUser
import Data.IDataManager.IUserDataManager
import Entity.User
import android.content.Context
import cr.ac.utn.spots.R

class UserController {

    private var context: Context
    private var dataManager: IUserDataManager = MemoryDataManagerUser


    constructor(dataManager: IUserDataManager, context: Context){
        this.dataManager = dataManager
        this.context = context
    }

    constructor(context: Context) {
        this.context = context
    }

    fun addUser(user: User){
        try {
            dataManager.add(user)
        } catch (e: Exception){
            throw Exception(context.getString(R.string.errorMsgAdd))
        }
    }

    fun updateUser(user: User){
        try {
            dataManager.update(user)
        } catch (e: Exception){
            throw Exception(context.getString(R.string.errorMsgUpdate))
        }
    }

    fun getAllUsers(): List<User>{
        try {
            return dataManager.getAll()
        } catch (e: Exception){
            throw Exception(context.getString(R.string.errorMsgGetAll))
        }
    }

    fun getUserByUsername(username: String): User? {
        try{
            return dataManager.getByUsername(username)
        } catch (e: Exception){
            throw Exception("Error al obtener dato específico")
        }
    }

    fun removeUser(username: String){
        try {
            val result = dataManager.getByUsername(username)
            if(result == null){
                throw Exception(context.getString(R.string.errorMsgDataWasNotFound))
            }
            dataManager.remove(username)
        }catch (e: Exception){
            throw Exception(context.getString(R.string.errorMsgRemove))
        }
    }
}