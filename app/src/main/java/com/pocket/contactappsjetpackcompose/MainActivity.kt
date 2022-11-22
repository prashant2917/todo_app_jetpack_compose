package com.pocket.contactappsjetpackcompose


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pocket.contactappsjetpackcompose.model.TodoModel
import com.pocket.contactappsjetpackcompose.ui.theme.ContactAppsJetpackComposeTheme


class MainActivity : ComponentActivity() {
    lateinit var todoList: List<TodoModel>

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactAppsJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(floatingActionButton = { FloatingActionButton() }) {

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        TodoList(context = applicationContext)
                    }

                }


            }
        }

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TodoList(context: Context) {

        todoList = ArrayList<TodoModel>()
        todoList = todoList + TodoModel("1", "my first todo", "Prashant", "Low", false)
        todoList = todoList + TodoModel("2", "Learn Jetpack Compose", "Prashant", "High", false)
        todoList = todoList + TodoModel("3", "Set Watch", "Pallavi", "Medium", false)
        todoList = todoList + TodoModel("4", "Check RBL", "Prashant", "Medium", false)
        todoList = todoList + TodoModel("5", "Check PAN Card Status", "Prashant", "Medium", false)

        LazyColumn(Modifier.padding(16.dp)) {

            itemsIndexed(todoList) { _, item ->

                Card(
                    onClick = {
                        Toast.makeText(
                            context,
                            item.todo_description + " Clicked ..",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()

                ) {
                    Column(
                        Modifier.padding(8.dp)

                    ) {
                        Text(text = item.todo_description)
                        Text(text = item.todo_responsible)
                        Text(text = item.todo_priority)
                    }
                }
            }
        }

    }

    @Composable
    fun FloatingActionButton() {
        FloatingActionButton(
            // on below line we are
            // adding on click for our fab
            onClick = {
                Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT).show()
            },
            // on below line we are
            // specifying shape for our button
            shape = CircleShape,


            // on below line we are
            // adding background color.

            // on below line we are
            // adding content color.
            contentColor = Color.White,
            modifier = Modifier.padding(16.dp),

            ) {
            // on below line we are
            // adding icon for fab.
            Icon(Icons.Filled.Add, "")

        }

    }

}




