package Entity

class Favorite {
    private lateinit var user: User
    private lateinit var post: Post

    constructor(user: User, post: Post){
        this.post = post
        this.user = user
    }

    constructor(){}

    var Post: Post
        get() = this.post
        set(value) {this.post = value}

    var User: User
        get() = this.user
        set(value) {this.user = value}
}