package Data.DataManager

import Data.IDataManager.IRuleDataManager
import Entity.Rule

object MemoryDataManagerRule : IRuleDataManager {
    private var ruleList = mutableListOf<Rule>()

    override fun add(rule: Rule) {
        ruleList.add(rule)
    }

    override fun update(rule: Rule) {
        remove(rule.Title)
        add(rule)
    }

    override fun remove(id: String) {
        ruleList.removeIf { it.Title.trim() == id.trim() }
    }

    override fun getById(id: String): Rule? {
        try {
            var result = ruleList.filter { it.Title == id }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAll(): List<Rule> = ruleList

    override fun getByTitle(title: String): Rule? {
        try {
            var result = ruleList.filter { it.Title == title }
            return if (result.any()) result[0] else null
        } catch (e: Exception) {
            throw e
        }
    }
}
