package lk.mzpo.alias.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lk.mzpo.alias.GameViewModel
import lk.mzpo.alias.Team
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.Gold
import lk.mzpo.alias.ui.theme.GoldLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    teams: List<Team>,
    winningScore: Int,
    viewModel: GameViewModel,
    onStartTurn: () -> Unit,
    onWinner: () -> Unit
) {

    LaunchedEffect(key1 = "") {
        if (viewModel.getCurrentTeamIndex() == 0)
        {
            if (viewModel.checkWinner() !== null)
            {
                onWinner()
            }
        }
    }
    Scaffold(
        bottomBar =
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Gold)
                    .clickable {
                        onStartTurn()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Начать",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGray)
                .padding(it)
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
                        val offset = 60f // Смещение на 10 пикселей вниз
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
                            color = GoldLight
                        )
                    }
                Column(
                    modifier = Modifier
                        .background(GoldLight)
                        .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 10.dp)
                ) 
                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Счет", fontSize = 50.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "")
                            Text(text = viewModel.winningScore.toString(), fontSize = 30.sp)
                        }
                    }
                    teams.forEach { team ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = team.name, fontSize = 20.sp)
                            Text(text = team.score.toString(), fontSize = 20.sp)

                        }
                    }
                }
            }
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = viewModel.getCurrentTeam()!!.name, color = Color.White, fontSize = 30.sp)
            }

        }
    }
}


