package com.visight.adondevamos.data.remote.requests

class GetPublicPlacesRequest(_input: String) {
    var output: String = ""
    var key: String = ""
    var input: String = ""
    var inputtype: String = ""

    init {
        output = "json"
        key = "AIzaSyBMfHFvJTPHMgD5zBbRbuJdOjIOJ_HdL4o"
        input = _input
        inputtype = "textquery"
    }
}