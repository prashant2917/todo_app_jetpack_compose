package com.pocket.contactappsjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocket.contactappsjetpackcompose.database.TodoRepository
import com.pocket.contactappsjetpackcompose.model.TodoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    lateinit var todoList: List<TodoModel>

    init {
        fetchTodoList()
    }

    private fun fetchTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            todoList = repository.getTodoList()
        }
    }

    fun addTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todoModel)
        }
    }

    fun updateTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todoModel = todoModel)
        }
    }

    fun deleteTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todoModel = todoModel)
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTodos()
        }
    }
}

