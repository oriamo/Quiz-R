package eu.oraimo.jettrivia.screns.quiz

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import eu.oraimo.jettrivia.components.Questions
import eu.oraimo.jettrivia.screns.QuestionViewmodel
import eu.oraimo.jettrivia.screns.viewModels.SharedViewModel
import javax.inject.Inject

@Composable
fun TriviaHome (viewModel: QuestionViewmodel = hiltViewModel(), sharedViewModel: SharedViewModel, navController : NavHostController){
    Questions(viewmodel = viewModel, sharedViewModel, navController)
}