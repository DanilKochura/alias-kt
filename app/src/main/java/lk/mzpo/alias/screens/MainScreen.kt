package lk.mzpo.alias.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lk.mzpo.alias.R
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onStartGame: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Scaffold(
        modifier = Modifier.background(DarkGray),
        bottomBar =
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Gold)
                    .clickable {
                        onStartGame()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Начать игру", color = Color.White, textAlign = TextAlign.Center)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(DarkGray)
        ) {
            // 1. Круг в левом верхнем углу
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset((-50).dp-animatedOffset.dp, (-50).dp+animatedOffset.dp.times(2))
                    .shadow(8.dp, CircleShape)
                    .background(Gold, shape = CircleShape)
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            // 2. Полукруг справа от текста
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (75).dp - animatedOffset.dp.times(3), y = 50.dp+animatedOffset.dp) // Нахлест на текст
                    .shadow(5.dp, CircleShape)
                    .background(Color.Gray, shape = CircleShape)
                    .align(Alignment.TopEnd)
            )

            // 3. Круг в правом нижнем углу
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .offset(50.dp-animatedOffset.dp, 50.dp+animatedOffset.dp.times(5))
                    .shadow(20.dp, CircleShape)
                    .background(Color.DarkGray, shape = CircleShape)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )

            // 4. Полукруг на левой грани чуть ниже текста
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .offset(y = 50.dp+animatedOffset.dp, x = (-75).dp) // Полукруг, чуть ниже текста
                    .shadow(10.dp, CircleShape)
                    .background(Color.LightGray, shape = CircleShape)
                    .align(Alignment.CenterStart)

            )

            // Контент в центре
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                    Text(text = "настоящий", color = Color.Gray, fontSize = 14.sp)
                    Image(
                        painter = painterResource(id = R.drawable.alias),
                        contentDescription = "logo",
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(10.dp)
                    )

                    Text(text = "by", color = Color.White, fontSize = 18.sp)
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .padding(10.dp)
                    )
            }
        }
    }
}
