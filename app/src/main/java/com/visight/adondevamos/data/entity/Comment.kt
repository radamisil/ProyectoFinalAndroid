package com.visight.adondevamos.data.entity

class Comment {
    var user: User? = null
    var ratingReported: Float? = null
    var comment: String? = null
    var dateReported: String? = null

    constructor(user: User, ratingReported: Float, comment: String, dateReported: String){
        this.user = user
        this.ratingReported = ratingReported
        this.comment = comment
        this.dateReported = dateReported
    }
}