package Entity

class Post {
    private var title: String = ""
    private var content: String = ""
    private var imageUrl: String = ""
    private lateinit var user: User
    private lateinit var forum: Forum

    constructor(title: String, content: String, imageUrl: String, user: User, forum: Forum){
        this.content = content
        this.title = title
        this.imageUrl = imageUrl
        this.user = user
        this.forum = forum
    }

    var Title: String
        get() = this.Title
        set(value) {this.Title = value}

    var Content: String
        get() = this.content
        set(value) {this.content = value}

    var ImageUrl: String
        get() = this.imageUrl
        set(value) {this.imageUrl = value}

    var User: User
        get() = this.user
        set(value) {this.user = value}

    var Forum: Forum
        get() = this.forum
        set(value) {this.forum = value}
}