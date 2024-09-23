package lk.mzpo.alias.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lk.mzpo.alias.GameViewModel
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.DarkGreen
import lk.mzpo.alias.ui.theme.GoldLight

@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearMaterialApi::class)
@Composable
fun TeamTurnScreen(
    viewModel: GameViewModel,
    initialTime: Int,
    onTimeEnd: () -> Unit
) {
    val currentTeam = viewModel.getCurrentTeam() // Текущая команда
    var isSwiping by remember { mutableStateOf(false) }
    val currentWord = viewModel.getCurrentWord()
    val alpha by animateFloatAsState(targetValue = if (isSwiping) 0f else 1f)
    val scale by animateFloatAsState(targetValue = if (isSwiping) 0.5f else 1f)
    // Делаем переменную remainingTime изменяемой
    var remainingTime by remember { mutableStateOf(initialTime) }

    // Таймер
    LaunchedEffect(remainingTime) {
        if (remainingTime > 0) {
            delay(1000L) // Задержка в 1 секунду
            remainingTime -= 1 // Уменьшаем значение времени
        } else {
            onTimeEnd() // Время закончилось
        }
    }
    var isProcessingSwipe by remember { mutableStateOf(false) } // Флаг для блокировки многократных свайпов
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
//        topBar = { TopAppBar(title = { Text( currentTeam!!.name) }) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGray)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.fillMaxWidth())
            {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val offset = 0f // Смещение на 10 пикселей вниз
                    val path = Path().apply {
                        moveTo(0f, 0f) // Начало пути
                        lineTo(0f, height - 50f +offset) // Левая нижняя часть блока
                        quadraticBezierTo(
                            width / 2, height + 50f+offset, // Центральная точка дуги
                            width, height - 50f+offset  // Правая нижняя часть дуги
                        )
                        lineTo(width, 0f) // Правая верхняя часть блока
                        close() // Закрываем путь
                    }
                    drawPath(
                        path = path,
                        color = DarkGreen
                    )
                }
                Column (modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Text(text = viewModel.getCurrentTeam()!!.name, fontSize = 25.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Text(text = viewModel.getGuessed().toString(), fontSize = 50.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                }
            }
            Box(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                isProcessingSwipe =
                                    false // Разблокируем свайп после завершения жеста
                            }
                        ) { _, dragAmount ->
                            if (!isProcessingSwipe) {
                                coroutineScope.launch {
                                    isProcessingSwipe = true // Блокируем новые свайпы

                                    if (dragAmount < -5) {
                                        // Свайп вверх (угадано)
                                        isSwiping = true
                                        kotlinx.coroutines.delay(200)
                                        viewModel.onSwipeUp() // Угадано, увеличиваем счет
                                        isSwiping = false
                                    } else if (dragAmount > 5) {
                                        isSwiping = true
                                        kotlinx.coroutines.delay(200)
                                        // Свайп вниз (не угадано)
                                        viewModel.onSwipeDown() // Пропущено, увеличиваем счет
                                        isSwiping = false
                                    }
                                }
                            }
                        }
                    }
                    .alpha(alpha)
                    .scale(scale)
                    .size(250.dp)
                    .clip(CircleShape)
                    .shadow(10.dp, CircleShape)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = currentWord.capitalize(), textAlign = TextAlign.Center, fontSize = 20.sp)
            }

            Box(modifier = Modifier.fillMaxWidth())
            {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val path = Path().apply {
                        moveTo(0f, 50f) // Начало пути (левый край)
                        quadraticBezierTo(
                            width / 2, -50f, // Центральная точка для вогнутой дуги
                            width, 50f  // Конечная точка (правый край)
                        )
                        lineTo(width, height) // Правая часть блока
                        lineTo(0f, height) // Левая часть блока
                        close() // Закрываем путь
                    }
                    drawPath(
                        path = path,
                        color = Color.Gray,
                    )
                }
                Column (modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Text(text = viewModel.getSkipped().toString(), fontSize = 50.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Text(text = "0:"+remainingTime.toString(), fontSize = 20.sp, modifier = Modifier.border(
                        1.dp, DarkGray, RoundedCornerShape(10f)
                    ).padding(5.dp))
                }
            }
        }
    }
}

