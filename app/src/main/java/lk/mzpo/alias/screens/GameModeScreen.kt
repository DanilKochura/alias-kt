package lk.mzpo.alias.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lk.mzpo.alias.Dictionary.Companion.COMPARER
import lk.mzpo.alias.ui.theme.DarkGray
import lk.mzpo.alias.ui.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameModeScreen(onModeSelected: (List<String>) -> Unit) {
    val selected = remember { mutableStateListOf<String>() }
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Выбор режима игры") }, colors = TopAppBarDefaults.topAppBarColors(
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
                        if (selected.size > 0) {
                            onModeSelected(selected)
                        } else {
                            Toast
                                .makeText(
                                    ctx,
                                    "Выберите хотя бы одну категорию!",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
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
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            COMPARER.forEach { (name, list) ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable {
                        if (selected.contains(name)) {
                            selected.remove(name)
                        } else {
                            selected.add(name)
                        }
                    }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Column {
                        Text(text = name.capitalize(), color = Color.White, fontSize = 20.sp)
                        Text(text = list.size.toString()+" слов", color = Color.Gray)
                        Text(text = list[0]+", "+list[1]+", "+list[2], color = Color.Gray, fontSize = 12.sp)
                    }
                    Checkbox(
                        checked = selected.contains(name),
                        onCheckedChange = { checked_ ->
                                          if (checked_)
                                          {
                                              selected.add(name)
                                          } else
                                          {
                                              selected.remove(name)
                                          }
                        } ,colors = CheckboxDefaults.colors(checkedColor = Gold, uncheckedColor = Color.LightGray),
                        modifier = Modifier.scale(1.3f)
                    )
                }
                Divider()
            }
        }
    }
}
