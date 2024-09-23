package lk.mzpo.alias

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import com.google.gson.Gson
import lk.mzpo.alias.Dictionary.Companion.COMPARER
import kotlin.random.Random

// Модель для хранения слов
data class WordDictionary(
    val easy: List<String>,
    val medium: List<String>,
    val hard: List<String>,
    val all_words: List<String>
)

data class Team(
    val name: String,
    var score: Int = 0
)

data class WordList(
    val word: String,
    var guessed: Boolean
)

class GameViewModel : ViewModel() {
    var _teams by mutableStateOf(listOf<Team>(Team("1", 0), Team("2", 0)))
    private var _currentTeamIndex by mutableStateOf(0) // Индекс текущей команды
    private var _currentWordIndex by mutableStateOf(0)
    private var _guessedWords by mutableStateOf(0)
    private var _skippedWords by mutableStateOf(0)
    var _remainingTime by mutableStateOf(60)
    var _currentWords: ArrayList<String> = arrayListOf() // Слова для текущего раунда
    public var winningScore by mutableStateOf(50)


    // Слова для всех категорий (Пример)
    private val easyWords = listOf("apple", "car", "dog", "tree")
    private val mediumWords = listOf("phenomenon", "lightning", "conversation", "puzzle")
    private val hardWords = listOf("antidisestablishmentarianism", "quintessential", "hyperbolic")
    private val allWords = easyWords + mediumWords + hardWords
    public val wordList = mutableListOf<WordList>()




    // Метод для установки списка слов в зависимости от режима игры
//    fun setWordsForMode(mode: String, wordCount: Int) {
//        _currentWords = when (mode) {
//            "easy" -> easyWords.shuffled().take(wordCount)
//            "medium" -> mediumWords.shuffled().take(wordCount)
//            "hard" -> hardWords.shuffled().take(wordCount)
//            "all_words" -> allWords.shuffled().take(wordCount)
//            else -> emptyList()
//        }
//    }

    fun setCommonModes(mode: List<String>, wordCount: Int = 1000) {
        val list = arrayListOf<String>()
        mode.forEach { it ->
            list+=COMPARER[it]!!.toList()
        }
        _currentWords = list
        _currentWordIndex = Random.nextInt(0 ,_currentWords.size)
    }
    var lastWord by mutableStateOf<String?>(null)  // Переменная для хранения последнего слова
    var lastWordTeamId by mutableStateOf<Int?>(null)  // Переменная для хранения последнего слова

    // Метод для обработки завершения времени
    fun onTimeEnd() {
        lastWord = getCurrentWord()  // Сохраняем последнее слово
    }

    // Метод для засчитывания последнего слова выбранной команде
    fun assignLastWordToTeam(teamIndex: Int) {
        lastWordTeamId = teamIndex
    }

    // Получаем текущую команду с проверкой на наличие команд
    fun getCurrentTeam(): Team? {
        return if (_teams.isNotEmpty()) {
            _teams[_currentTeamIndex]
        } else {
            null // Возвращаем null, если список команд пуст
        }
    }

    // Инициализируем команды
    fun initializeTeams(teamNames: List<String>) {
        _teams = teamNames.map { Team(it) }
    }

    // Получаем текущее слово
    fun getCurrentWord(): String {
        return _currentWords[_currentWordIndex]
    }

    // Переход к следующему слову
    private fun moveToNextWord() {
        _currentWords.removeAt(_currentWordIndex)
        _currentWordIndex = _currentWords.indexOf(_currentWords.random())
    }

    fun loadWordsFromJson(context: Context): Map<String, List<String>> {
        val inputStream: InputStream = context.assets.open("words.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val wordMapType = object : TypeToken<Map<String, List<String>>>() {}.type
        return Gson().fromJson(json, wordMapType)
    }

    // Обрабатываем угадывание слова
    fun onSwipeUp() {
        _guessedWords += 1
        wordList.add(WordList(this.getCurrentWord(), true))
        moveToNextWord()
    }

    // Обрабатываем пропуск слова
    fun onSwipeDown() {
        _skippedWords += 1
        wordList.add(WordList(this.getCurrentWord(), false))
        moveToNextWord()
    }

    // Переход хода к следующей команде
    fun moveToNextTeam() {
        _currentTeamIndex = (_currentTeamIndex + 1) % _teams.size
    }

    fun getCurrentTeamIndex(): Int {
        return _currentTeamIndex
    }

    fun checkWinner(): Team? {
        if (_currentTeamIndex == 0)
        {
            val max = _teams.maxBy { it.score }
            if (max.score > winningScore )
            {
                return max
            }
        }
        return null
    }

    // Получаем текущую команду


    // Сброс таймера
    fun resetTimer(time: Int) {
        _remainingTime = time
    }

    public fun getGuessed(): Int {
        return _guessedWords
    }

    fun getSkipped(): Int {
        return _skippedWords
    }

    fun getScore(): Int {
        var score = 0;
        wordList.forEach { words ->
            if (words.guessed) {
                score += 1
            } else {
                score -= 1
            }
        }
        return score
    }

    fun incrementScore(score: Int)
    {
        _teams[_currentTeamIndex].score+=score
        _guessedWords = 0
        _skippedWords = 0
    }

    fun setLastWordPoints() {
        if (lastWordTeamId !== null)
        {
            _teams[lastWordTeamId!!].score+=1
        }
        lastWord = null
        lastWordTeamId = null
    }
}

