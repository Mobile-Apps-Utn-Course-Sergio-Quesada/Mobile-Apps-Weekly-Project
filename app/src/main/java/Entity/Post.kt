package Entity

import android.graphics.Bitmap

class Post {
    private var id: String = ""
    private var title: String = ""
    private var content: String = ""
    private var imageUrl: String = ""
    private var photo: Bitmap? = null
    private lateinit var user: User
    private lateinit var forum: Forum

    constructor()

    constructor(
        id: String,
        title: String,
        content: String,
        imageUrl: String,
        photo: Bitmap?,
        user: User,
        forum: Forum
    ) {
        this.id = id
        this.title = title
        this.content = content
        this.imageUrl = imageUrl
        this.photo = photo
        this.user = user
        this.forum = forum
    }

    var ID: String
        get() = this.id
        set(value) {
            this.id = value
        }

    var Title: String
        get() = this.title
        set(value) {
            this.title = value
        }

    var Content: String
        get() = this.content
        set(value) {
            this.content = value
        }

    var ImageUrl: String
        get() = this.imageUrl
        set(value) {
            this.imageUrl = value
        }

    var Photo: Bitmap?
        get() = this.photo
        set(value) {
            this.photo = value
        }

    var User: User
        get() = this.user
        set(value) {
            this.user = value
        }

    var Forum: Forum
        get() = this.forum
        set(value) {
            this.forum = value
        }
}