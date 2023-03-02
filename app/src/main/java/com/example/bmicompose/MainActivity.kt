package com.example.bmicompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.estimateAnimationDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmicompose.ui.theme.BMIComposeTheme
import com.example.bmicompose.utils.bmiCalculate
import com.example.bmicompose.utils.bmiColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMIComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BMICalculator()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BMICalculator() {

    var weightState by rememberSaveable() {
        mutableStateOf("")
    }
    var heightState by rememberSaveable() {
        mutableStateOf("")
    }

    var expandState by remember {
        mutableStateOf(false)
    }

    var bmiScoreState by remember {
        mutableStateOf(0.0)
    }

    var weightFocusRequester = FocusRequester()

    var isWeightError by remember {
        mutableStateOf(false)
    }

    var isHeightError by remember {
        mutableStateOf(false)
    }



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bmi),
                contentDescription = "Application Icon",
                modifier = Modifier.size(110.dp)
            )
            Text(
                text = stringResource(id = R.string.app_title),
                color = Color.Blue,
                fontSize = 24.sp,
                letterSpacing = 4.sp
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 24.dp)
        ) {
            Text(text = stringResource(id = R.string.weight))
            OutlinedTextField(
                value = weightState,
                onValueChange = { newWeight ->
                    val lastChar =
                        if (newWeight.length == 0){
                            isWeightError = true
                            newWeight
                        } else {
                            newWeight.get(newWeight.length - 1)
                            isWeightError = false
                        }
                    var newValue =
                        if (lastChar == '.' || lastChar == ',') newWeight.dropLast(1) else newWeight
                    weightState = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(weightFocusRequester)
                    .padding(top = 8.dp),
                trailingIcon = {
                    if (isWeightError)
                        Icon(imageVector = Icons.Outlined.Error, contentDescription = "")
                    else
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                },
                isError = isWeightError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            if (isWeightError){
                Text(
                    text = stringResource(id = R.string.weight_error),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.height))
            OutlinedTextField(
                value = heightState,
                onValueChange = { newHeight ->
                    val lastChar =
                        if (newHeight.length == 0){
                            isHeightError = true
                            newHeight
                        }
                        else{
                            newHeight.get(newHeight.length - 1)
                            isHeightError = false
                        }
                    var newValue =
                        if (lastChar == '.' || lastChar == ',')
                            newHeight.dropLast(1)
                        else
                            newHeight

                    heightState = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                trailingIcon = {
                    if (isHeightError)
                        Icon(imageVector = Icons.Default.Error, contentDescription = "")
                    else
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                },
                isError = isHeightError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            if (isHeightError){
                Text(
                    text = stringResource(id = R.string.height_error),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.End
                )
            }
            Button(
                onClick = {
                    isWeightError = weightState.length == 0
                    isHeightError = heightState.length == 0
                    if (isHeightError != true && isWeightError != true) {
                        bmiScoreState = bmiCalculate(weightState.toInt(), heightState.toDouble())
                        expandState = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(10, 170, 10))
            ) {
                Text(
                    text = stringResource(id = R.string.button_calculate),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight(800)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        Column() {
            AnimatedVisibility(
                visible = expandState,
                enter = slideIn(tween(durationMillis = 200)) {
                    IntOffset(it.width / 2, 0)
                },
                exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)

            ) {
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    backgroundColor = bmiColor(bmiScoreState)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.your_score),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format("%.2f", bmiScoreState),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Congratulations! Your BMI is ideal!",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row() {
                            Button(
                                onClick = {
                                    expandState = false
                                    weightState = ""
                                    heightState = ""
                                    weightFocusRequester.requestFocus()
                                },
                                colors = ButtonDefaults.buttonColors(Color(130, 100, 255))
                            ) {
                                Text(
                                    text = stringResource(id = R.string.reset_button),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.buttonColors(Color(130, 100, 255))
                            ) {
                                Text(
                                    text = stringResource(id = R.string.share_button),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun BMICalculatorPreview() {
    BMICalculator()
}