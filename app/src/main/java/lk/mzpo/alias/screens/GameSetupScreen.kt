package lk.mzpo.alias.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lk.mzpo.alias.GameViewModel
import lk.mzpo.alias.R
import lk.mzpo.alias.Team
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(
    roundTime: Int,
    winningScore: Int,
    onRoundTimeChange: (Int) -> Unit,
    onWinningScoreChange: (Int) -> Unit,
    onNext: () -> Unit,
    gameViewModel: GameViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Настройка игры") }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkGray,
                titleContentColor = Color.White,
            ),)
        },
        bottomBar =
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Gold)
                    .clickable {
                        onNext()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Далее",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier)
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGray)
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = 5.dp,
                    end = 5.dp
                )
        ) {
            Text(text = "Время раунда: " + gameViewModel._remainingTime.toString() + " секунд", color = Color.White)
            Slider(
                value = gameViewModel._remainingTime.toFloat(),
                onValueChange = { gameViewModel.resetTimer(it.toInt()) },
                valueRange = 10f..180f,
                colors = SliderDefaults.colors(thumbColor = Gold, activeTrackColor = Color.White, inactiveTrackColor = Color.Gray)
            )
            Text(text = "Очки для победы: " + gameViewModel.winningScore.toString(), color = Color.White)
            Slider(
                value = gameViewModel.winningScore.toFloat(),
                onValueChange = { gameViewModel.winningScore = it.toInt() },
                valueRange = 10f..100f,
                colors = SliderDefaults.colors(thumbColor = Gold, activeTrackColor = Color.White, inactiveTrackColor = Color.Gray)
            )


            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val ctx = LocalContext.current
                VerticalSlider(
                    label = "Последнее слово",
                    value = gameViewModel.EndSound.value,
                    onValueChange = {
                        gameViewModel.EndSound.value = it
                        gameViewModel.playSound(ctx, R.raw.end_of_round, it)
                    }
                )
                VerticalSlider(
                    label = "Свайп вверх",
                    value = gameViewModel.TopSwipeSound.value,
                    onValueChange = {
                        gameViewModel.TopSwipeSound.value = it
                        gameViewModel.playSound(ctx, R.raw.up, it)

                    }
                )
                VerticalSlider(
                    label = "Свайп вниз",
                    value = gameViewModel.DownSwipeSound.value,
                    onValueChange = {
                        gameViewModel.DownSwipeSound.value = it
                        gameViewModel.playSound(ctx, R.raw.down, it)

                    }
                )
            }

        }
    }
}
@Composable
fun VerticalSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = label, fontSize = 12.sp, color =  Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(100.dp)    // Ширина слайдера после поворота
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .height(100.dp)
                    .rotate(-90f),
                valueRange = valueRange,
                steps = 10,
                colors = SliderDefaults.colors(thumbColor = Gold, activeTrackColor = Color.LightGray, inactiveTrackColor = DarkGray)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "${(value * 100).toInt()}%", fontSize = 14.sp, color = Color.LightGray)
    }
}