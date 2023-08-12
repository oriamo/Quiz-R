package eu.oraimo.jettrivia.screns.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import eu.oraimo.jettrivia.models.QuizSettings
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import eu.oraimo.jettrivia.models.FinishSettings


class SharedViewModel : ViewModel() {

    var Quizsetting by mutableStateOf<QuizSettings>(QuizSettings(0,true))
        private set
    var FinshSettings by mutableStateOf<FinishSettings>(FinishSettings(0,0))
        private set
    fun setQuestions(questions : Int, randomize : Boolean){
        Quizsetting = QuizSettings(questions, randomize)
    }

    fun setFinish(correct : Int, max : Int){
        FinshSettings = FinishSettings(correct = correct, max)
    }

}