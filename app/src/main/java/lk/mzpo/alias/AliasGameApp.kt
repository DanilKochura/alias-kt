package lk.mzpo.alias

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lk.mzpo.alias.screens.GameModeScreen
import lk.mzpo.alias.screens.GameSetupScreen
import lk.mzpo.alias.screens.MainScreen
import lk.mzpo.alias.screens.StartScreen
import lk.mzpo.alias.screens.TeamSetupScreen
import lk.mzpo.alias.screens.TeamTurnScreen
import lk.mzpo.alias.screens.TurnResultScreen
import lk.mzpo.alias.screens.WinnerScreen

@Composable
fun AliasGameApp(gameViewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "mainScreen"
    ) {
        composable("mainScreen") {
            MainScreen(
                onStartGame = {
                    navController.navigate("teamSetup")
                }
            )
        }

        composable("teamSetup") {
            TeamSetupScreen(
                viewModel = gameViewModel,
                onAddTeam = { /* Добавляем команду */ },
                onNext = { navController.navigate("gameSetup") }
            )
        }

        composable("gameSetup") {
            GameSetupScreen(
                roundTime = 60,
                winningScore = 50,
                onRoundTimeChange = { gameViewModel.resetTimer(it) },
                onWinningScoreChange = { gameViewModel.winningScore = it },
                onNext = { navController.navigate("gameMode") },
                gameViewModel
            )
        }

        composable("gameMode") {
            GameModeScreen(onModeSelected = { mode ->
                // Устанавливаем слова для выбранного режима игры
                gameViewModel.setCommonModes(mode)
                navController.navigate("startScreen")
            })
        }

        composable("startScreen") {
            StartScreen(
                teams = gameViewModel._teams,
                winningScore = 50,
                viewModel = gameViewModel,
                onStartTurn = {
                    navController.navigate("teamTurn")
                },
                onWinner = {
                    navController.navigate("winner")
                }
            )
        }

        composable("teamTurn") {
            val currentWords = gameViewModel._currentWords
            if (currentWords.isNotEmpty()) {
                TeamTurnScreen(
                    viewModel = gameViewModel,
                    initialTime = gameViewModel._remainingTime,
                    onTimeEnd = {
                        // Переходим к следующей команде
                        navController.navigate("turnResult")
                    }
                )
            }
        }


        composable("turnResult") {
            TurnResultScreen(
                viewModel = gameViewModel,
                onConfirm = { score ->
                    gameViewModel.incrementScore(score = score)
                    gameViewModel.setLastWordPoints()
                    gameViewModel.wordList.clear()
                    gameViewModel.moveToNextTeam()
                    navController.navigate("startScreen") }
            )
        }

        composable("winner") {
            WinnerScreen(
                viewModel = gameViewModel,
                onConfirm = {
//                    gameViewModel.reset()
                    navController.navigate("mainScreen")
                }
            )
        }
    }
}

