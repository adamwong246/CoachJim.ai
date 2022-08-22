package com.example.coachjim

import android.app.ListActivity
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import com.example.coachjim.ui.theme.CoachJimTheme
//import kotlinx.android.parcel.Parcelize
import androidx.navigation.compose.*
import com.example.coachjim.datastore.UserPreferences
import com.example.coachjim.TasksViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.sql.Timestamp
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.dataStore
import com.example.coachjim.datastore.UserPreferences.SortOrder
import com.example.coachjim.data.UserPreferencesSerializer
import com.example.coachjim.TasksAdapter
import com.example.coachjim.databinding.SettingsActivityBinding

import java.util.prefs.Preferences

data class Exercise(val name: String) {}
data class ExerciseGoal(val exercise: Exercise, val reps: Int, val sets: Int, val weight: Int, val timestamp: Timestamp) {}
data class WorkoutPlan(val name: String, val exercises: List<Exercise>) {}

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val SORT_ORDER_KEY = "sort_order"

// Build the DataStore
private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer,
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context,
                USER_PREFERENCES_NAME
            ) { sharedPrefs: SharedPreferencesView, currentData: UserPreferences ->
                // Define the mapping from SharedPreferences to UserPreferences
                if (currentData.sortOrder == SortOrder.UNSPECIFIED) {
                    currentData.toBuilder().setSortOrder(
                        SortOrder.valueOf(
                            sharedPrefs.getString(SORT_ORDER_KEY, SortOrder.NONE.name)!!
                        )
                    ).build()
                } else {
                    currentData
                }
            }
        )
    }
)

class MainActivity : ComponentActivity() {

//    private lateinit var binding: ActivityTasksBinding
    private val adapter = TasksAdapter()
    private lateinit var viewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoachJimTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val context = LocalContext.current
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        Column{
                                Button(onClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.close()
                                    }
                                    context.startActivity(Intent(context, TasksActivity::class.java))

//                                    navController.navigate("records") {
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//
//
//
//
//
//
//                                        ;
//                                    }
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

                                context.startActivity(Intent(context, SettingsActivity::class.java))
//                                navController.navigate("settings") {
//                                    popUpTo(navController.graph.findStartDestination().id) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
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

