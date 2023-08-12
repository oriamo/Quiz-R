package eu.oraimo.jettrivia.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.oraimo.jettrivia.screns.finsh.FinishScreen
import eu.oraimo.jettrivia.screns.quiz.TriviaHome
import eu.oraimo.jettrivia.screns.viewModels.SharedViewModel
import eu.oraimo.jettrivia.screns.welcome.WelcomeScreen

@Composable
fun Navigation(navController : NavHostController ){

    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(navController = navController, startDestination = QuizScreens.Welcome.name )
    {
        composable(QuizScreens.Welcome.name){
            //where this route leads too
            WelcomeScreen(navController = navController, sharedViewModel)
        }

        composable(QuizScreens.Quiz.name){
            TriviaHome(sharedViewModel = sharedViewModel, navController = navController)
        }
        composable(QuizScreens.Finish.name){
            FinishScreen(navcontroller = navController, sharedViewModel = sharedViewModel)
        }
    }

}