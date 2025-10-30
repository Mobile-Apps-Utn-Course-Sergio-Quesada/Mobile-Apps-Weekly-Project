package Entity

class User {
    private var username: String = ""
    private var password: String = ""
    private var email: String = ""
    private var fullName: String = ""

    constructor(username: String, email: String, fullName: String, password: String){
        this.fullName = fullName
        this.email = email
        this.username = username
        this.password = password
    }

    constructor(){}

    var Username: String
        get() = this.username
        set(value) {this.username = value}

    var Email: String
        get() = this.email
        set(value) {this.email = value}

    var FullName: String
        get() = this.fullName
        set(value) {this.fullName = value}

    var Password: String
        get() = this.password
        set(value) {this.password = value}
}