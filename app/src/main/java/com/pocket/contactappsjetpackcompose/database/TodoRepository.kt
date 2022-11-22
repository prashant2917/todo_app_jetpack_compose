package com.pocket.contactappsjetpackcompose.database


import com.pocket.contactappsjetpackcompose.model.TodoModel
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDatabaseDao: TodoDatabaseDao) {

    suspend fun getTodoList(): List<TodoModel> {
        return todoDatabaseDao.getAll()
    }

    suspend fun addTodo(todoModel: TodoModel) {
        todoDatabaseDao.insert(todoModel)
    }

    suspend fun updateTodo(todoModel: TodoModel) {
        todoDatabaseDao.update(todoModel)
    }

    suspend fun deleteTodo(todoModel: TodoModel) {
        todoDatabaseDao.delete(todoModel)
    }

    suspend fun deleteAllTodos() {
        todoDatabaseDao.deleteAllTodos()
    }
}