package eu.oraimo.jettrivia.screns

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import eu.oraimo.jettrivia.data.DataOrException
import eu.oraimo.jettrivia.models.QuestionItem
import eu.oraimo.jettrivia.repository.QuestionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuestionViewmodel @Inject constructor( private val repository : QuestionRepository  ) : ViewModel(){
     val data: MutableState<DataOrException<ArrayList<QuestionItem>,
            Boolean,
            Exception>> = mutableStateOf(DataOrException(null,true, Exception("")))

    init {
        getAllQuestions()
    }
    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllQuestions()
            if (data.value.data.toString().isNotEmpty()){
                data.value.loading = false
            }
        }
    }

    fun getTotalQuestionCount(): Int {
        return data.value.data?.toMutableList()?.size!!
    }


}