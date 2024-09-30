package lk.mzpo.alias.screens

import android.media.MediaPlayer
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lk.mzpo.alias.GameViewModel
import lk.mzpo.alias.R
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.DarkGreen
import lk.mzpo.alias.ui.theme.FloatingBar
import lk.mzpo.alias.ui.theme.GoldLight
import lk.mzpo.alias.ui.theme.Test
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearMaterialApi::class)
@Composable
fun TeamTurnScreen(
    viewModel: GameViewModel,
    initialTime: Int,
    onTimeEnd: () -> Unit
) {
    val ctx = LocalContext.current
    val currentTeam = viewModel.getCurrentTeam() // Текущая команда
    var isSwiping by remember { mutableStateOf(false) }
    val currentWord = viewModel.getCurrentWord()
    val alpha by animateFloatAsState(targetValue = if (isSwiping) 0f else 1f)
    val scale by animateFloatAsState(targetValue = if (isSwiping) 0.5f else 1f)
    val ofset by animateFloatAsState(targetValue = if (isSwiping) 150f else 0f)
    // Делаем переменную remainingTime изменяемой
    var remainingTime by remember { mutableStateOf(initialTime) }
    var showDialog by remember { mutableStateOf(false) }
    var ofsetDirection by remember {
        mutableStateOf(-1)
    }
    // Таймер
    LaunchedEffect(remainingTime) {
        if (remainingTime > 0) {
            delay(1000L) // Задержка в 1 секунду
            remainingTime -= 1 // Уменьшаем значение времени
        } else {
            viewModel.playSound(ctx, R.raw.end)
            viewModel.onTimeEnd() // Время закончилось
        }
    }
    var isProcessingSwipe by remember { mutableStateOf(false) } // Флаг для блокировки многократных свайпов
    val coroutineScope = rememberCoroutineScope()
    var size_circle by remember { mutableStateOf(0) }
    var x by remember { mutableStateOf(0) }
    var y by remember { mutableStateOf(0) }
    LaunchedEffect(key1 = "") {
        x = Random.nextInt(-100, 200)
        y = Random.nextInt(-10, 310)
        size_circle = Random.nextInt(40, 89)
    }
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

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { /* Окно не закроется, пока не выбрана команда */ },

                    title = { Text(text = "Кому засчитать последнее слово?") },
                    text = { Text(text = "Выберите команду, чтобы засчитать последнее слово.") },
                    confirmButton = {
                        Column {
                            // Отображаем список команд для выбора
                            viewModel._teams.forEachIndexed { index, team ->
                                OutlinedButton(
                                    onClick = {
                                        viewModel.assignLastWordToTeam(index)  // Засчитываем слово команде
                                        showDialog = false  // Закрываем диалог после выбора
                                        onTimeEnd()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = team.name)
                                }
                            }
                        }
                    },
                    dismissButton = {
                        // Если нужно, можно добавить кнопку для отмены
                    }
                )
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
                    val offset = 0f // Смещение на 10 пикселей вниз
                    val path = Path().apply {
                        moveTo(0f, 0f) // Начало пути
                        lineTo(0f, height - 100f + offset) // Левая нижняя часть блока
                        quadraticBezierTo(
                            width / 2, height + 100f + offset, // Центральная точка дуги
                            width, height - 100f + offset  // Правая нижняя часть дуги
                        )
                        lineTo(width, 0f) // Правая верхняя часть блока
                        close() // Закрываем путь
                    }
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFF1D343),
                                Color(0xFFBEA949)
                            )
                        ),
//                        color = Color(0xFFD5C058)
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.getCurrentTeam()!!.name,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = viewModel.getGuessed().toString(),
                        fontSize = 50.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            }

            Box(modifier = Modifier.fillMaxWidth())
            {
//                for (i in 0 until 5)
//                {
//                    FloatingBar(x = Random.nextInt(-100, 200), y = Random.nextInt(-10, 310), size = Random.nextInt(40, 89))
//                }
//                    FloatingBar(x = x, y = y, size = size_circle)


                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
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
                                            ofsetDirection = -1
                                            isSwiping = true
                                            viewModel.playSound(ctx, R.raw.up)
                                            kotlinx.coroutines.delay(200)
                                            if (remainingTime > 0) {
                                                viewModel.onSwipeUp() // Угадано, увеличиваем счет
                                            } else {
                                                showDialog =
                                                    true  // Показываем диалог для выбора команды
                                            }

                                            isSwiping = false
                                        } else if (dragAmount > 5) {
                                            ofsetDirection = 1
                                            isSwiping = true
                                            viewModel.playSound(ctx, R.raw.down)
                                            kotlinx.coroutines.delay(200)
                                            // Свайп вниз (не угадано)

                                            viewModel.onSwipeDown() // Пропущено, увеличиваем счет
                                            if (remainingTime == 0) {
                                                onTimeEnd()
                                            }
                                            isSwiping = false
                                        }
                                    }
                                }
                            }
                        }
                        .offset(y = ofset.times(ofsetDirection).dp)
                        .alpha(alpha)
                        .scale(scale)
                        .size(250.dp)
                        .clip(CircleShape)
                        .shadow(20.dp, CircleShape, clip = true)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color.LightGray,
                                    Color(0xFF7A7A7A)
                                )
                            ), CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentWord.capitalize(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )

                }
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
                        moveTo(0f, 100f) // Начало пути (левый край)
                        quadraticBezierTo(
                            width / 2, -100f, // Центральная точка для вогнутой дуги
                            width, 100f  // Конечная точка (правый край)
                        )
                        lineTo(width, height) // Правая часть блока
                        lineTo(0f, height) // Левая часть блока
                        close() // Закрываем путь
                    }
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFCC8B8B),
                                Color(0xFFDBB8B8)
                            )
                        ),
//                        color = Color(0xFFDBB8B8),
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.getSkipped().toString(),
                        fontSize = 50.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = if (remainingTime > 0) "0:" + remainingTime.toString() else "последнее слово",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .border(
                                1.dp, DarkGray, RoundedCornerShape(10f)
                            )
                            .padding(5.dp)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun Circle3d() {
    Column(Modifier.background(DarkGray)) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
//                .border(1.dp, Color.Black, CircleShape)
                .shadow(20.dp, CircleShape, clip = true)
                .background(
                    Brush.radialGradient(listOf(Color.LightGray, Color.White)),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Test", textAlign = TextAlign.Center, fontSize = 20.sp)
        }
    }
}


