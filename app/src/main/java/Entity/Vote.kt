package Entity

class Vote {
    private lateinit var user: User
    private lateinit var post: Post
    private var value: Int = 0

    constructor(user: User, post: Post, value: Int){
        this.user = user
        this.post = post
        this.value = value
    }

    constructor(){}

    var User: User
        get() = this.user
        set(value) {this.user = value}

    var Post: Post
        get() = this.post
        set(value) {this.post = value}

    var Value: Int
        get() = this.value
        set(value) {this.value = value}
}