package com.pocket.contactappsjetpackcompose


import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pocket.contactappsjetpackcompose.database.TodoDatabase
import com.pocket.contactappsjetpackcompose.database.TodoRepository
import com.pocket.contactappsjetpackcompose.model.TodoModel
import com.pocket.contactappsjetpackcompose.ui.theme.ContactAppsJetpackComposeTheme
import com.pocket.contactappsjetpackcompose.viewmodel.ToDoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ContactAppsJetpackComposeTheme {


                Scaffold(floatingActionButton = { FloatingActionButton(navController = navController) }) {

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {


                        NavGraph(navController)
                    }

                }


            }
        }

    }


}

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val todoDao = TodoDatabase.getInstance(context.applicationContext as Application).todoDao()
    val repository = TodoRepository(todoDao)
    val mTodoViewModel: ToDoViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "todolist"
    ) {
        composable(route = "todolist") {
            TodoScreen(mTodoViewModel, navController)
        }

        composable(route = "create-todo") {
            AddTodo(mTodoViewModel, navController)
        }

        composable(route = "edit-todo/{todoId}") { backStackEntry ->
            EditTodo(mTodoViewModel, navController, backStackEntry.arguments?.getString("todoId"))
        }

    }
}

@Composable
fun TodoScreen(toDoViewModel: ToDoViewModel, navController: NavHostController) {

    var list = toDoViewModel.todoList
    Log.d("###", "list size" + list.size)
    Column {
        TodoList(todoList = list, navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoList(todoList: List<TodoModel>, navController: NavHostController) {

    if (todoList.isEmpty()) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "No To do Items, Press + to add todo", fontSize = 18.sp)
        }
    } else {
        LazyColumn(Modifier.padding(16.dp)) {

            itemsIndexed(todoList) { _, item ->

                Card(
                    onClick = {
                        navController.navigate("edit-todo/${item.todId}") {
                            launchSingleTop = true
                        }

                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()

                ) {
                    Column(
                        Modifier.padding(8.dp)

                    ) {
                        Text(text = item.todoDescription)
                        Text(text = item.todoResponsible)
                        Text(text = item.todoPriority)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodo(toDoViewModel: ToDoViewModel, navController: NavHostController) {
    val todoDescription = remember { mutableStateOf("") }
    val todoResponsible = remember { mutableStateOf("") }
    val todoPriority = remember { mutableStateOf("") }
    val todoCompleted = remember { mutableStateOf(false) }

    val mExpanded = remember { mutableStateOf(false) }
    val priorities = listOf("Low", "Medium", "High")
    val mTextFieldSize = remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TextField(
            value = todoDescription.value,
            onValueChange = { todoDescription.value = it },
            placeholder = { Text(text = "Enter Todo Description") },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif
            ),
        )

        TextField(
            value = todoResponsible.value,
            onValueChange = { todoResponsible.value = it },
            placeholder = { Text(text = "Enter Todo Responsible") },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif
            ),
        )
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = todoPriority.value,
                onValueChange = { todoPriority.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        mTextFieldSize.value = coordinates.size.toSize()
                    },
                label = { Text("Priority") },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { mExpanded.value = !mExpanded.value })
                }
            )

            DropdownMenu(
                expanded = mExpanded.value,
                onDismissRequest = { mExpanded.value = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { mTextFieldSize.value.width.toDp() })
            ) {
                priorities.forEach { label ->
                    DropdownMenuItem({ Text(text = label) }, onClick = {
                        todoPriority.value = label
                        mExpanded.value = false
                    })
                }
            }
        }

        Button(
            onClick = {
                val todoModel = TodoModel(
                    todoDescription = todoDescription.value,
                    todoResponsible = todoResponsible.value,
                    todoPriority = todoPriority.value,
                    todoCompleted = todoCompleted.value
                )
                toDoViewModel.addTodo(todoModel)
                navController.navigate("todolist")
            },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(text = "Create Todo")
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodo(toDoViewModel: ToDoViewModel, navController: NavHostController, todoId: String?) {
    todoId?.let { toDoViewModel.getById(it.toInt()) }

    val todoModel = toDoViewModel.todoModelObserver.value
    if (todoModel != null) {

        val todoDescription = remember { mutableStateOf(todoModel.todoDescription) }
        val todoResponsible = remember { mutableStateOf(todoModel.todoResponsible) }
        val todoPriority = remember { mutableStateOf(todoModel.todoPriority) }
        val todoCompleted = remember { mutableStateOf(todoModel.todoCompleted) }
        val onSwitchedChange: (Boolean) -> Unit = { todoCompleted.value = it }

        var mExpanded = remember { mutableStateOf(false) }
        val priorities = listOf("Low", "Medium", "High")
        var mTextFieldSize = remember { mutableStateOf(Size.Zero) }

        // Up Icon when expanded and down icon when collapsed
        val icon = if (mExpanded.value)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            TextField(
                value = todoDescription?.value.toString(),
                onValueChange = { todoDescription?.value = it },
                placeholder = { Text(text = "Enter Todo Description") },
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif
                ),
            )

            TextField(
                value = todoResponsible.value,
                onValueChange = { todoResponsible.value = it },
                placeholder = { Text(text = "Enter Todo Responsible") },
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif
                ),
            )
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = todoPriority.value,
                    onValueChange = { todoPriority.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // This value is used to assign to
                            // the DropDown the same width
                            mTextFieldSize.value = coordinates.size.toSize()
                        },
                    label = { Text("Priority") },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { mExpanded.value = !mExpanded.value })
                    }
                )

                DropdownMenu(
                    expanded = mExpanded.value,
                    onDismissRequest = { mExpanded.value = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { mTextFieldSize.value.width.toDp() })
                ) {
                    priorities.forEach { label ->
                        DropdownMenuItem({ Text(text = label) }, onClick = {
                            if (todoPriority != null) {
                                todoPriority.value = label
                            }
                            mExpanded.value = false
                        })
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Completed", modifier = Modifier.padding(0.dp, 12.dp, 0.dp, 0.dp))
                Checkbox(checked = todoCompleted.value, onCheckedChange = onSwitchedChange)


            }

            Button(
                onClick = {
                    val todoModel = TodoModel(
                        todId = todoId?.toLong() ?: 0,
                        todoDescription = todoDescription.value,
                        todoResponsible = todoResponsible.value,
                        todoPriority = todoPriority.value,
                        todoCompleted = todoCompleted.value
                    )
                    if (todoModel != null) {
                        toDoViewModel.updateTodo(todoModel)
                    }
                    navController.navigate("todolist")
                },
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Update Todo")
            }

        }
    }
}

@Composable
fun FloatingActionButton(navController: NavHostController) {
    FloatingActionButton(

        onClick = {
            navController.navigate("create-todo") {
                popUpTo("todolist")
                launchSingleTop = true

            }


        },
        shape = CircleShape,
        contentColor = Color.White,
        modifier = Modifier.padding(16.dp)


    ) {
        Icon(Icons.Filled.Add, "")

    }

}








