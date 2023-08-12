package eu.oraimo.jettrivia.network

import eu.oraimo.jettrivia.models.Question
import retrofit2.http.GET

interface QuestionApi {
    @GET("world.json")
    suspend fun  getAllQuestions(): Question


}