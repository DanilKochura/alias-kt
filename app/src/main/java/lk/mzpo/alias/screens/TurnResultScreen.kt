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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
    var showDialog by remember { mutableStateOf(false) }

    var score by remember {
        mutableIntStateOf(0)
    }

    score = viewModel.getScore()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Результат хода")

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

            Column (modifier = Modifier.padding(horizontal = 20.dp)) {
                var score_for = score
                if (viewModel.lastWordTeamId !== null && viewModel.lastWordTeamId == viewModel.getCurrentTeamIndex())
                {
                    score_for+=1
                }
                Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = viewModel.getCurrentTeam()!!.name.toString(), fontSize = 20.sp, color = Color.White)
                    Text(text = score_for.toString(), fontSize = 30.sp, color = Color.White)
                }
                if (viewModel.lastWordTeamId !== null && viewModel.lastWordTeamId != viewModel.getCurrentTeamIndex())
                {
                    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Text(text = viewModel._teams[viewModel.lastWordTeamId!!].name.toString(), fontSize = 20.sp, color = Color.White)
                        Text(text = "1", fontSize = 30.sp, color = Color.White)
                    }
                }
            }
            Column (modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())){
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
               if (viewModel.lastWordTeamId !== null) {
                   Row(modifier = Modifier
                       .fillMaxWidth()
                       .padding(vertical = 5.dp, horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                   ) {
                       Text(text = viewModel.lastWord.toString(), color = Color.White)
                       IconButton(onClick = {
                           showDialog = true
                       }) {
                           Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "",
                               tint = if (viewModel.lastWordTeamId !== null) Gold else Color.LightGray)

                       }
                   }

               }
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
            }


        }
    }
}
