package com.pocket.contactappsjetpackcompose.model

data class TodoModel(
    var _id : String,
    var todo_description : String,
    var todo_responsible : String,
    var todo_priority : String,
    var todo_completed: Boolean

)