package Entity

class Comment {
    private var content: String = ""
    private lateinit var post_id: Post
    private lateinit var user_id: User

    constructor(content: String, post_id: Post, user_id: User){
        this.user_id = user_id
        this.post_id = post_id
        this.content = content
    }

    constructor(){}

    var Content: String
        get() = this.content
        set(value) {this.Content = value}

    var Post_id: Post
        get() = this.post_id
        set(value) {this.post_id = value}

    var User_id: User
        get() = this.user_id
        set(value) {this.user_id = value}
}