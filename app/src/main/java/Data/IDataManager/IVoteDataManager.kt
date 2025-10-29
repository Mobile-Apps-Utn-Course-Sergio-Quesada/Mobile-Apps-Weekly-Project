package Data.IDataManager

import Entity.Vote

interface IVoteDataManager {
    fun add(vote: Vote)
    fun update(vote: Vote)
    fun remove(id: String)
    fun getById(id: String): Vote?
    fun getAll(): List<Vote>
    fun getByValue(value: Int): Vote?
}
