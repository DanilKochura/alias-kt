package lk.mzpo.alias.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lk.mzpo.alias.GameViewModel
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
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding(), start = 5.dp, end = 5.dp)
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
        }
    }
}
