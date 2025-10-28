package Entity

class Rule {
    private lateinit var forum: Forum
    private var title: String = ""
    private var description: String = ""

    constructor(forum: Forum, title: String, description: String){
        this.forum = forum
        this.description = description
        this.title = title
    }

    var Forum: Forum
        get() = this.forum
        set(value) {this.forum = value}

    var Title: String
        get() = this.title
        set(value) {this.title = value}

    var Description: String
        get() = this.description
        set(value) {this.description = value}
}