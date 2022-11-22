package com.pocket.contactappsjetpackcompose.database


import androidx.room.*
import com.pocket.contactappsjetpackcompose.model.TodoModel

@Dao
interface TodoDatabaseDao {
    @Query("SELECT * from todo_list")
    suspend fun getAll(): List<TodoModel>

    @Query("SELECT * from todo_list where todId = :id")
    suspend fun getById(id: Int): TodoModel?

    @Insert
    suspend fun insert(item: TodoModel)

    @Update
    suspend fun update(item: TodoModel)

    @Delete
    suspend fun delete(item: TodoModel)

    @Query("DELETE FROM todo_list")
    suspend
    fun deleteAllTodos()
}