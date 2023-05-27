package com.ayushsinghal.notesapp

class DataModel() {
    var title: String? = null
    var description: String? = null
    var userID: String? = null
    var noteID: String? = null

    constructor(title: String, description: String, userID: String, noteID: String) : this() {
        this.title = title
        this.description = description
        this.userID = userID
        this.noteID = noteID
    }

}