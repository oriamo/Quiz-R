package eu.oraimo.jettrivia.navigation

enum class QuizScreens {
    Welcome,
    Quiz,
    Finish;
    companion object {
        fun fromRoute(route : String? ) : QuizScreens = when(route?.substringBefore("/")){
            Welcome.name -> Welcome
            Quiz.name -> Quiz
            Finish.name -> Finish
            null -> Welcome
            else -> throw IllegalArgumentException("Route $route is not recognize")
        }
    }



}