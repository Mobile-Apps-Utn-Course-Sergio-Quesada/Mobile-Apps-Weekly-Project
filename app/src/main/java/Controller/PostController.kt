package Controller

import Data.DataManager.MemoryDataManagerPost
import Data.IDataManager.IPostDataManager
import Entity.Post
import android.content.Context
import cr.ac.utn.spots.R

class PostController {
    private var context: Context
    private var dataManager: IPostDataManager = MemoryDataManagerPost

    constructor(dataManager: IPostDataManager, context: Context) {
        this.dataManager = dataManager
        this.context = context
    }

    constructor(context: Context) {
        this.context = context
    }

    fun addPost(post: Post) {
        try {
            dataManager.add(post)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.errorMsgAdd))
        }
    }

    fun updatePost(post: Post) {
        try {
            dataManager.update(post)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.errorMsgUpdate))
        }
    }

    fun getAllPost(): List<Post> {
        try {
            return dataManager.getAll()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.errorMsgGetAll))
        }
    }

    fun getById(id: String): Post? {
        try {
            return dataManager.getById(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun getPostByTitle(title: String): Post? {
        try {
            return dataManager.getByTitle(title)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun removePost(id: String) {
        try {
            val result = dataManager.getById(id)
            if (result == null) {
                throw Exception(context.getString(R.string.errorMsgDataWasNotFound))
            }
            dataManager.remove(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.errorMsgRemove))
        }
    }
}