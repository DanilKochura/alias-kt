package lk.mzpo.alias.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lk.mzpo.alias.GameViewModel
import lk.mzpo.alias.Team
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSetupScreen(
    viewModel: GameViewModel,
    onAddTeam: () -> Unit,
    onNext: () -> Unit
) {
    val teams_dict = arrayListOf(
        "Фанатики",
        "Задроты",
        "Мишки Гамми",
        "Луноход М29",
        "Бетон",
        "Пчела",
        "Фруктовые",
        "Эксперты",
        "Академики",
        "Пшено",
        "Летчики",
        "Карандаш",
        "Волшебники",
        "Том Круз",
        "Марионетки",
        "Бункер",
        "Шляпа",
        "Мракобесы"
    )
    val teams_user = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(key1 = "") {
        val first = teams_dict.random()
        teams_user.add(first)
        teams_dict.remove(first)
        val second = teams_dict.random()
        teams_user.add(second)
        teams_dict.remove(second)

    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Настройка команд") }, colors = TopAppBarDefaults.topAppBarColors(
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
                        val _teams = arrayListOf<Team>()
                        for (team in teams_user) {
                            _teams.add(Team(team, 0))
                        }
                        viewModel._teams = _teams
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
                .padding(it)
        ) {
            LazyColumn {
                itemsIndexed(teams_user) { index, team ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = team,
                            color = Color.White,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        teams_user[index]=teams_dict.random()
                                    }
                                )
                            })
                        if (index > 1)
                        {
                            IconButton(onClick = { teams_user.remove(team) }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "", tint = Color.White)
                            }
                        }
                    }
                    Divider()
                }
            }
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button(onClick = {
                    teams_user.add(teams_dict.random())
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "AddBtn")
                    Text("Добавить команду")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
