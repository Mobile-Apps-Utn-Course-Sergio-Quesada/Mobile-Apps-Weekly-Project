package Entity

class User {
    private var username: String = ""
    private var email: String = ""
    private var fullName: String = ""
    private var password: String = ""

    constructor()

    constructor(username: String, email: String, fullName: String, password: String) {
        this.username = username
        this.email = email
        this.fullName = fullName
        this.password = password
    }

    var Username: String
        get() = this.username
        set(value) {
            this.username = value
        }

    var Email: String
        get() = this.email
        set(value) {
            this.email = value
        }

    var FullName: String
        get() = this.fullName
        set(value) {
            this.fullName = value
        }

    var Password: String
        get() = this.password
        set(value) {
            this.password = value
        }
}