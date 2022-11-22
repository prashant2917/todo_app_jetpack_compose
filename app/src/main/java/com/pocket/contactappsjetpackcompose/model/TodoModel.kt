package com.pocket.contactappsjetpackcompose.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_list")
data class TodoModel(
    @PrimaryKey(autoGenerate = true)
    var todId: Long = 0L,
    @ColumnInfo(name = "todo_description")
    var todoDescription: String,
    @ColumnInfo(name = "todo_responsible")
    var todoResponsible: String,
    @ColumnInfo(name = "todo_priority")
    var todoPriority: String,
    @ColumnInfo(name = "todo_completed")
    var todoCompleted: Boolean

)
