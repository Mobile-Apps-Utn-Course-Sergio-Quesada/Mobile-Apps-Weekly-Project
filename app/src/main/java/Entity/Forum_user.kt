package Entity

class Forum_user {
    private lateinit var user: User
    private lateinit var forum: Forum
    private var role: String = ""

    constructor(user: User, forum: Forum, role:String){
        this.user = user
        this.forum = forum
        this.role = role
    }

    constructor(){}

    var User: User
        get() = this.user
        set(value) {this.user = value}

    var Forum: Forum
        get() = this.forum
        set(value) {this.forum = value}

    var Role: String
        get() = this.role
        set(value) {this.role = value}
}