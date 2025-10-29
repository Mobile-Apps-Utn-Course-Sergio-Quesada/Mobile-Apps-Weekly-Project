package Data.IDataManager

import Entity.Rule

interface IRuleDataManager {
    fun add(rule: Rule)
    fun update(rule: Rule)
    fun remove(id: String)
    fun getById(id: String): Rule?
    fun getAll(): List<Rule>
    fun getByTitle(title: String): Rule?
}
