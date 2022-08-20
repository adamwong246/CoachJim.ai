package com.example.coachjim

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import com.example.coachjim.ui.theme.CoachJimTheme
import kotlinx.android.parcel.Parcelize
import androidx.navigation.compose.*
import kotlinx.coroutines.launch
import java.sql.Timestamp

data class Exercise(val name: String) {}
data class ExerciseGoal(val exercise: Exercise, val reps: Int, val sets: Int, val weight: Int, val timestamp: Timestamp) {}
data class WorkoutPlan(val name: String, val exercises: List<Exercise>) {}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoachJimTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        Column{
                                Button(onClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.close()
                                    }

                                    navController.navigate("records") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true






                                        ;
                                    }
                                }
                                ){
                                    Text("Records")
                                }

                            Button(onClick = {

                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }

                                navController.navigate("workouts") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            ){
                                Text("Workout Plans")
                            }
                            Button(onClick = {

                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }

                                navController.navigate("settings") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            ){
                                Text("Settings")
                            }

                            Text("CoachJim.ai is your virtual gym coach. ")
                        }

                                    },
                    topBar = {
                        TopAppBar(
                            title = { Text("CoachJim.ai") },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        scope.launch { scaffoldState.drawerState.open() }
                                    }
                                ) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                                }
                            }
                        )
                    },
//                    floatingActionButtonPosition = FabPosition.End,
//                    floatingActionButton = {
//                        ExtendedFloatingActionButton(
//                            text = { Text("Inc") },
//                            onClick = { /* fab click handler */ }
//                        )
//                    },
                    content = { innerPadding ->

                        // A surface container using the 'background' color from the theme
                        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

//                    NavGraph(navController = navController)
                            NavHost(navController = navController, startDestination = "startScreen") {
                                composable("startScreen") { Greeting("fella") }

                                composable("records") { Text("records go here") }
                                composable("workouts") { Text("workouts go here") }
                                composable("settings") { Text("settings go here") }

                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}



@Composable
fun SimpleFilledTextFieldSample() {
    var text by remember { mutableStateOf("adam") }

    Row{
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("your name here") }
        )      
        Button(onClick = { /*TODO*/ } ) {
            Text(text = "ok")
        }
    }

}

@Composable
fun ChatArea() {
    Text(text = "My name is Coach Jim! I'm your personal fitness AI and I'm here to help you meet your fitness goals. But first things first- whats' your name?")
    SimpleFilledTextFieldSample()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoachJimTheme {
        Text(text = "My name is Coach Jim, your personal fitness AI.")
    }
}

