package eu.oraimo.jettrivia.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import eu.oraimo.jettrivia.models.QuestionItem
import eu.oraimo.jettrivia.models.QuizSettings
import eu.oraimo.jettrivia.navigation.QuizScreens
import eu.oraimo.jettrivia.screns.QuestionViewmodel
import eu.oraimo.jettrivia.screns.viewModels.SharedViewModel
import eu.oraimo.jettrivia.util.AppColors
import java.lang.Exception
import java.util.Random

@Composable
fun Questions( viewmodel: QuestionViewmodel, sharedViewModel: SharedViewModel, navController : NavHostController){
    /** this variable represents the user setting from the welcome for the quiz */
    val settings : QuizSettings = sharedViewModel.Quizsetting
    /** this variable represents the data from the API */
    val questions = viewmodel.data.value.data?.toMutableList()
    /** this variable represents the start value of the quiz its random if the user selected the random option in the welcome screen and zero if the user dosen't  */
    val startValue : Int = setQuizValues(settings.randomize)
    /** this variable represent the amount of Questions to be displayed based on the settings */
    val numQuestions : Int = settings.numQuestions

    /** this variable is a list of questions to be displayed based on the user selection in the welcome screen  */
    val setQuestions = questions?.subList(startValue,startValue + settings.numQuestions )
    /** a mutable state to keep track off o fthe current index an increse in the index chnages the question being displayed */
    val questionIndex = remember {
        mutableStateOf(0)
    }
    //checking if the data from the application is still running in the background
    if ( viewmodel.data.value.loading == true){

        CircularProgressIndicator()

    }else {
        val question = try {
            setQuestions?.get(questionIndex.value)
        }catch (ex : Exception){
            null
        }
       if (questions != null){
           QuestionDisplay(Question = question!!, QuestionIndex = questionIndex, numQuestions = numQuestions){ next,correct ->
               if (next){
                   //move to the next question
                   questionIndex.value =  questionIndex.value +1
               }else {
                   //move to the finish screen
                    sharedViewModel.setFinish(correct = correct, settings.numQuestions)
                    navController.navigate(QuizScreens.Finish.name)
               }


           }

       }
    }



}
fun setQuizValues(randomize : Boolean) : Int{
    if (randomize){
        val rand : Random = Random()
        val start = rand.nextInt(4000)
        return start
    }else {
        return 0
    }



}
//@Preview
@Composable
fun QuestionDisplay(Question : QuestionItem,
                    QuestionIndex: MutableState<Int>,
                    numQuestions : Int,
                    onclicked : (Boolean, Int) -> Unit ){
    val context = LocalContext.current

    val choicesState = remember(Question){
        Question.choices.toMutableList()
    }
    /** a state that updates to show the amount of Questions the user got right */
    val correctAnswerCount = remember {
        mutableStateOf(0)
    }
    /** a state that updates to show the  user current selection  */
    val answerState = remember(Question) {
        mutableStateOf<Int?>(null)
    }
    /** a state that updates to show if the user has currently selected the correct option */
    val correctAnswerState = remember(Question) {
        mutableStateOf<Boolean?>(null)
    }
    /**
     *updates the value of the current user choice and checks to see if the choice is the correct option
     */
    val updateAnswer : (Int) -> Unit = remember(Question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == Question.answer
        }
    }
    val pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f,10f,0f))
    Surface(modifier = Modifier
        .fillMaxSize()
        , color = AppColors.mDarkPurple) {

        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
            if (QuestionIndex.value > 0) ShowProgress(score = QuestionIndex.value, numQuestions = numQuestions)
            QuestionTracker(counter = QuestionIndex.value, outof = numQuestions )
            DrawDottedLine(pathEffect = pathEffect)

            Column() {
                Text(text = Question.question, fontSize = 17.sp,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite)

                //choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mLightPurple,
                                        AppColors.mLightPurple,
                                        AppColors.mLightPurple
                                    )
                                ), shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )

                            )
                            .background(
                                if (index == answerState.value) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Transparent
                                }
                            )
                            .clickable { updateAnswer(index) }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = answerState.value == index,
                            onClick = { updateAnswer(index) },
                        modifier = Modifier.padding(start = 16.dp),
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Green)) // end rb
                        val anotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                                        color =  AppColors.mOffWhite ,
                                                        fontSize = 17.sp)){
                                append(answerText)
                            }
                        }
                        Text(text = anotatedString, modifier = Modifier.padding(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                                    if (correctAnswerState.value == true){
                                        correctAnswerCount.value++
                                    }
                                    if (answerState.value != null){
                                        onclicked(QuestionIndex.value < numQuestions - 1, correctAnswerCount.value)
                                    }else {
                                        Toast.makeText(context, "make a valid selection", Toast.LENGTH_SHORT).show()
                                    }

                                 }, modifier = Modifier
                    .padding(3.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.mLightBlue
                )) {
                    Text(text = "Next",
                    modifier = Modifier.padding(4.dp),
                    color = AppColors.mOffWhite,
                    fontSize = 17.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))
                Button(onClick = {
                    onclicked(false, correctAnswerCount.value)
                }, modifier = Modifier
                    .padding(3.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.mLightPurple
                    )) {
                    Text(text = "SUBMIT",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp)
                }



            }

        }

    }
}

@Composable
fun DrawDottedLine(pathEffect : androidx.compose.ui.graphics.PathEffect){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp) ){
    drawLine(color = AppColors.mLightGray,
            start = Offset(0f,0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect)
    }
}

@Composable
fun QuestionTracker(counter: Int = 10, outof : Int){
    Text(text = buildAnnotatedString { withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
        withStyle(style = SpanStyle(color = AppColors.mLightGray,
        fontWeight = FontWeight.Bold,
        fontSize = 27.sp)){
            append("Question $counter/")
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
            fontWeight = FontWeight.Light,
                fontSize = 14.sp
           )){
                append("$outof")
            }
        }
    } },
        modifier = Modifier.padding(20.dp)
    )
}


@Composable
fun ShowProgress( score: Int , numQuestions: Int ){
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075), Color(0xFFBE6BE5)))
    val fScore = score.toFloat()
    val fNumQuestions = numQuestions.toFloat()
    val progressFactor = remember(score) {
        mutableStateOf((fScore/fNumQuestions))

    }
    Row(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    AppColors.mLightPurple, AppColors.mLightPurple
                )
            ), shape = RoundedCornerShape(34.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 50,
                bottomEndPercent = 50,
                bottomStartPercent = 50
            )
        )
        .background(Color.Transparent), verticalAlignment = Alignment.CenterVertically) {
        Button(contentPadding = PaddingValues(1.dp),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(progressFactor.value)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent ) ) {
           Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
               Text(
                   text = ("${String.format("%.0f", (progressFactor.value * 100))} %").toString(),
                   modifier = Modifier
                       .clip(shape = RoundedCornerShape(23.dp))
                       .padding(6.dp)
                       ,
                   color = AppColors.mOffWhite,
                   textAlign = TextAlign.Center
               )
           }

        }

    }

}