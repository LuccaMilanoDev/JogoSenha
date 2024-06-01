package com.example.jogosenha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jogosenha.ui.theme.JogoSenhaTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JogoSenhaTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(onStartClick = { currentScreen = "user_input" })
        "user_input" -> UserInputScreen(onBackClick = { currentScreen = "home" }, onNextLevelClick = { currentScreen = "level_2" })
        "level_2" -> Level2Screen(onBackClick = { currentScreen = "home" }, onNextLevelClick = { currentScreen = "level_3" })
        "level_3" -> Level3Screen(onBackClick = { currentScreen = "home" })
    }
}

@Composable
fun HomeScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Jogo Senha", style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "Bem-vindo ao Jogo Senha! Clique em Start para começar.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )
        Button(onClick = onStartClick) {
            Text(text = "Start")
        }
    }
}

@Composable
fun UserInputScreen(onBackClick: () -> Unit, onNextLevelClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber(1) }
    var gameWon by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(35.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Level 1", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(
            value = textState.value,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    textState.value = it
                }
            },
            label = { Text("Digite 4 dígitos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                if (textState.value.length == 4) {
                    val result = checkBullsAndCows(generatedNumber, textState.value)
                    storedValues = storedValues + (textState.value to result)
                    textState.value = ""
                    if (result == "4B 0C") {
                        gameWon = true
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Enviar")
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            storedValues.forEach { (value, result) ->
                Text(text = "Você enviou: $value   $result", modifier = Modifier.padding(top = 4.dp))
            }
        }
        if (gameWon) {
            Text(
                text = "Parabéns, você ganhou!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                onClick = onNextLevelClick,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Próximo Nível")
            }
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Back")
        }
        Text(
            text = "Resposta: $generatedNumber (Apenas para testes)",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun Level2Screen(onBackClick: () -> Unit, onNextLevelClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber(2) }
    var gameWon by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Level 2", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(
            value = textState.value,
            onValueChange = {
                if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                    textState.value = it
                }
            },
            label = { Text("Digite 5 dígitos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                if (textState.value.length == 5) {
                    val result = checkBullsAndCows(generatedNumber, textState.value)
                    storedValues = storedValues + (textState.value to result)
                    textState.value = ""
                    if (result == "5B 0C") {
                        gameWon = true
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Enviar")
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            storedValues.forEach { (value, result) ->
                Text(text = "Você enviou: $value   $result", modifier = Modifier.padding(top = 4.dp))
            }
        }
        if (gameWon) {
            Text(
                text = "Parabéns, você ganhou!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                onClick = onNextLevelClick,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Próximo Nível")
            }
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Back")
        }
        Text(
            text = "Resposta: $generatedNumber (Apenas para testes)",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun Level3Screen(onBackClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber(3) }
    var gameWon by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Level 3", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(
            value = textState.value,
            onValueChange = {
                if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                    textState.value = it
                }
            },
            label = { Text("Digite 6 dígitos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                if (textState.value.length == 6) {
                    val result = checkBullsAndCows(generatedNumber, textState.value)
                    storedValues = storedValues + (textState.value to result)
                    textState.value = ""
                    if (result == "6B 0C") {
                        gameWon = true
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Enviar")
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            storedValues.forEach { (value, result) ->
                Text(text = "Você enviou: $value   $result", modifier = Modifier.padding(top = 4.dp))
            }
        }
        if (gameWon) {
            Text(
                text = "Parabéns, você ganhou!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Back")
        }
        Text(
            text = "Resposta: $generatedNumber (Apenas para testes)",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

fun generateRandomNumber(level: Int): String {
    val digits = (0..9).toMutableList()
    return when (level) {
        1 -> (1..4).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
        2 -> (1..5).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
        3 -> (1..6).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
        else -> (1..4).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
    }
}

fun checkBullsAndCows(secret: String, guess: String): String {
    var bulls = 0
    var cows = 0
    val secretMap = mutableMapOf<Char, Int>()
    val guessMap = mutableMapOf<Char, Int>()

    for (i in secret.indices) {
        if (secret[i] == guess[i]) {
            bulls++
        } else {
            secretMap[secret[i]] = secretMap.getOrDefault(secret[i], 0) + 1
            guessMap[guess[i]] = guessMap.getOrDefault(guess[i], 0) + 1
        }
    }

    for ((key, count) in guessMap) {
        if (secretMap.containsKey(key)) {
            cows += minOf(count, secretMap[key]!!)
        }
    }

    return "${bulls}B ${cows}C"
}

@Preview(showBackground = true)
@Composable
fun UserInputScreenPreview() {
    JogoSenhaTheme {
        Level3Screen(onBackClick = {})
    }
}
