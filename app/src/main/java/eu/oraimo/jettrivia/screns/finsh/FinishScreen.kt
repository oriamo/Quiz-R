package eu.oraimo.jettrivia.screns.finsh

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import eu.oraimo.jettrivia.models.FinishSettings
import eu.oraimo.jettrivia.navigation.QuizScreens
import eu.oraimo.jettrivia.screns.viewModels.SharedViewModel
import eu.oraimo.jettrivia.util.AppColors

@Composable
fun FinishScreen(navcontroller : NavHostController, sharedViewModel : SharedViewModel){
    val correct : FinishSettings = sharedViewModel.FinshSettings
    val correctQuestions : Int = correct.correct
    val totalQuestions : Int = correct.numQuestion
    val flCorrectQuestions = correctQuestions.toFloat()
    val flTotalQuestions = totalQuestions.toFloat()
    val fltCorrectQuestions : Float = (flCorrectQuestions/flTotalQuestions)

    Surface(modifier = Modifier.fillMaxSize(), color = AppColors.mDarkPurple) {
        Column(modifier = Modifier
            .padding(4.dp)
            .padding(top = 45.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = "check mark ", tint = AppColors.mLightBlue,
            modifier = Modifier.size(100.dp))
            Text(text = "Congratulations", color = Color.White, fontSize = 26.sp, )
            Spacer(modifier = Modifier.height(40.dp))
            Results(fltCorrectQuestions,totalQuestions)
            Spacer(modifier = Modifier.height(20.dp))

            Row() {
                Button(onClick = { navcontroller.navigate(QuizScreens.Welcome.name) },modifier = Modifier.padding(4.dp), colors = ButtonDefaults.buttonColors(containerColor = AppColors.mLightBlue)) {
                    Text(text = "START NEW")
                }
            }

        }



    }
}
@Composable
fun Results(score : Float, totalQuestions : Int){
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(), border = BorderStroke(2.dp, color = AppColors.mLightGray), colors = CardDefaults.cardColors(
        Color.Transparent)) {
            Column(modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "SCORE", textAlign = TextAlign.Center, color = AppColors.mOffWhite, fontSize = 20.sp, modifier = Modifier.padding(5.dp) )
               CircularProgressBar(percentage = score, totalQestions = totalQuestions, color = AppColors.mLightBlue, modifier = Modifier.padding(40.dp))

            }
    }
}

@Composable
fun CircularProgressBar(percentage :Float,
                        totalQestions : Int,
                        modifier: Modifier = Modifier,
                        fontSize: TextUnit = 28.sp,
                        radius: Dp = 50.dp,
                        color: Color,
                        strokeWidth: Dp = 8.dp,
                        animDuration: Int = 1000,
                        animDelay : Int = 0

){

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val curPercentage = animateFloatAsState(
        targetValue = if(animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )

    LaunchedEffect(key1 = true){
        animationPlayed = true
    }


    Box(
       contentAlignment = Alignment.Center,
       modifier = modifier.size(radius * 2f)
   ) {
        Canvas(modifier = Modifier.size(radius * 2f )){
            drawArc(
                color = color,
                -90f,
                sweepAngle = 360 * curPercentage.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(text = buildAnnotatedString { withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp)
            ){
                append("${String.format("%.0f", (curPercentage.value * totalQestions))}/")
                withStyle(style = SpanStyle(color = AppColors.mLightGray,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
                ){
                    append("$totalQestions")
                }
            }
        } })
   }





}