package lk.mzpo.alias.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lk.mzpo.alias.GameViewModel
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TurnResultScreen(
    viewModel: GameViewModel,
    onConfirm: (score: Int) -> Unit
) {
    var score by remember {
        mutableIntStateOf(0)
    }
    score = viewModel.getScore()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceBetween){
                        Text("Результат хода")
                        Text(text = score.toString(), fontSize = 30.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGray,
                    titleContentColor = Color.White,
                ),
            )
        },

        bottomBar =
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Gold)
                    .clickable {
                        onConfirm(score)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Подтвердить",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGray)
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = 10.dp,
                    end = 10.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            viewModel.wordList.forEach { word ->
                var guessed by remember {
                    mutableStateOf(word.guessed)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = word.word, color = Color.White)
                    IconButton(onClick = {
                        guessed = !guessed
                        if (guessed)
                        {
                            score+=2
                        } else
                        {
                            score-=2
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "",
                            tint = if (guessed) Gold else Color.LightGray)

                    }
                }
                Divider()
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
