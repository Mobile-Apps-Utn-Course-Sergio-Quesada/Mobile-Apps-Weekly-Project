package Controller

import Data.DataManager.MemoryDataManagerUser
import Data.IDataManager.IUserDataManager
import Entity.User
import android.content.Context
import cr.ac.utn.spots.R

class UserController {
    private var dataManager: IUserDataManager = MemoryDataManagerUser
    private var context: Context

    constructor(dataManager: IUserDataManager, context: Context){
        this.dataManager = dataManager
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

    fun getUserById(id: String): User {
        try{
            val result = dataManager.getById(id)
            if(result == null){
                throw Exception(context.getString(R.string.errorMsgGetById))
            }
            return result
        } catch (e: Exception){
            throw Exception(context.getString(R.string.errorMsgDataWasNotFound))
        }
    }

    fun getPersonByFullName(name: String): User {
        try{
            val result = dataManager.getByFullName(name)
            if(result == null){
                throw Exception(context.getString(R.string.errorMsgGetById))
            }
            return result
        } catch (e: Exception){
            throw Exception(context.getString(R.string.errorMsgDataWasNotFound))
        }
    }

    fun removePerson(id: String){
        try {
            val result = dataManager.getById(id)
            if(result == null){
                throw Exception(context.getString(R.string.errorMsgDataWasNotFound))
            }
            dataManager.remove(id)
        }catch (e: Exception){
            throw Exception(context.getString(R.string.errorMsgRemove))
        }
    }
}