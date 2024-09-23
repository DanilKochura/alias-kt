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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lk.mzpo.alias.GameViewModel
import lk.mzpo.alias.R
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinnerScreen(
    viewModel: GameViewModel,
    onConfirm: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text("Конец игры")
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
                        onConfirm()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Новая игра",
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
                    start = 20.dp,
                    end = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val teams = viewModel._teams.sortedByDescending { it.score }
            Icon(painter = painterResource(id = R.drawable.baseline_military_tech_24), contentDescription = "", tint = Gold, modifier = Modifier.size(100.dp))
            Text(text = "Победитель", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Row (modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(bottom = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = teams[0].name, fontSize = 25.sp, color = Color.White)
                Text(text = teams[0].score.toString(), fontSize = 25.sp, color = Color.White)
            }
            for (i in 1 until teams.size)
            {
                Row (modifier = Modifier.fillMaxWidth(0.6f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = teams[i].name, color = Color.White)
                    Text(text = teams[i].score.toString(), color = Color.White)
                }
            }
        }
    }
}
