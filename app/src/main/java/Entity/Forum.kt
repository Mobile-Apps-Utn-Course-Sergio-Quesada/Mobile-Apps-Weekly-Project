package Entity

class Forum {
    private var name: String = ""
    private var description: String = ""

    constructor(name: String, description: String){
        this.name = name
        this.description = description
    }

    constructor(){}

    var Name: String
        get() = this.name
        set(value) {this.name = value}

    var Description: String
        get() = this.description
        set(value) {this.description = value}
}