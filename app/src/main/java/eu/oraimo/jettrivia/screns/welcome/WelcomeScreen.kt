package eu.oraimo.jettrivia.screns.welcome

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import eu.oraimo.jettrivia.navigation.QuizScreens
import eu.oraimo.jettrivia.screns.viewModels.SharedViewModel

//@Preview
@Composable
fun WelcomeScreen (navController: NavController, sharedviewModel : SharedViewModel ){

    Surface(modifier = Modifier.fillMaxSize() ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            Text(text = "QUIZ-R", color = MaterialTheme.colorScheme.onBackground, fontSize = 60.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Serif)
            Text(text = "4185 Questions Avaliable")
            Spacer(modifier = Modifier.height(50.dp))
            QuizInitilizer(){numQuestions,randomize ->
                sharedviewModel.setQuestions(numQuestions,randomize)
                navController.navigate(QuizScreens.Quiz.name)
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { /*TODO*/ }) {
                Row(modifier = Modifier.padding(4.dp)) {
                    Icon(imageVector = Icons.Rounded.List, contentDescription = "histoty of Quizes")
                    Text(text = "History")
                }
            }


        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizInitilizer(onClick : (numQuestion : Int, randomize : Boolean) -> Unit){

    val context = LocalContext.current

    val questionState = remember(){
        mutableStateOf("")
    }
    var randomize by remember {
        mutableStateOf(true)
    }
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp),
    shape = CircleShape.copy(all = CornerSize(10.dp)),
    color = Color.Transparent,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.secondary)
    ) {
       Column(modifier = Modifier.padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
           Row(modifier = Modifier.padding(3.dp)) {
               Icon(imageVector = Icons.Rounded.Settings, contentDescription = "Setting icon")
               Text(text = "Quiz settings")
           }
           Column(horizontalAlignment = Alignment.CenterHorizontally) {
               Text(text = "amount of questions", textAlign = TextAlign.Center)
               var inputIsValid by remember(questionState.value) {
                   mutableStateOf(questionState.value.trim().isNotEmpty())
               }
               OutlinedTextField(value = questionState.value,
                   onValueChange = {questionState.value = it.trim() },
                   modifier = Modifier.padding(5.dp),
                   label = { Text(text = "Questions")},
                   keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                   keyboardActions = KeyboardActions {
                                                     if (!inputIsValid) return@KeyboardActions

                   },
                   enabled = true,
                   singleLine = true
               )
           }

           Row(verticalAlignment = Alignment.CenterVertically, ) {
               RadioButton(selected = randomize, onClick = {
                   randomize = !randomize
               })
               Text(text = "Randomize Questions")
           }


           Button(onClick = {
               if (questionState.value.isNotEmpty() && questionState.value.toInt() > 0 && questionState.value.toInt() < 4184 ){
                   onClick(questionState.value.toInt(),randomize)
               } else {
                   Toast.makeText(context, "invalid input", Toast.LENGTH_SHORT).show()
               }
                            } ) {
               Text(text = "Start")
           }
       }



    }
}

