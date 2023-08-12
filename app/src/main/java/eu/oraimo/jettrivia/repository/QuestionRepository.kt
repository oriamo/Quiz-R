package eu.oraimo.jettrivia.repository

import android.content.ContentValues.TAG
import android.util.Log
import eu.oraimo.jettrivia.data.DataOrException
import eu.oraimo.jettrivia.models.QuestionItem
import eu.oraimo.jettrivia.network.QuestionApi
import java.lang.Exception
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val dataOrException = DataOrException< ArrayList<QuestionItem>, Boolean, Exception>()
    suspend fun  getAllQuestions() : DataOrException< ArrayList<QuestionItem>, Boolean, Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if (dataOrException.toString().isNotEmpty()) dataOrException.loading = false


        }catch (exception : Exception){
            dataOrException.e = exception
            Log.d(TAG, "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }

}