package Data.DataManager

import Data.IDataManager.IVoteDataManager
import Entity.Vote

object MemoryDataManagerVote : IVoteDataManager {
    private var voteList = mutableListOf<Vote>()

    override fun add(vote: Vote) {
        voteList.add(vote)
    }

    override fun update(vote: Vote) {
        remove(vote.User.Username)
        add(vote)
    }

    override fun remove(id: String) {
        voteList.removeIf { it.User.Username.trim() == id.trim() }
    }

    override fun getById(id: String): Vote? {
        try {
            var result = voteList.filter { it.User.Username == id }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAll(): List<Vote> = voteList

    override fun getByValue(value: Int): Vote? {
        try {
            var result = voteList.filter { it.Value == value }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}
