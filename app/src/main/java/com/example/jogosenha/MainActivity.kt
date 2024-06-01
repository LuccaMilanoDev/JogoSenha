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
        "user_input" -> UserInputScreen(onBackClick = { currentScreen = "home" })
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
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )
        Button(onClick = onStartClick) {
            Text(text = "Start")
        }
    }
}

@Composable
fun UserInputScreen(onBackClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber() }

    Column(
        modifier = Modifier
            .padding(16.dp, top = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
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
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Back")
        }
    }
}

fun generateRandomNumber(): String {
    val digits = (0..9).toMutableList()
    return (1..4).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
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
        UserInputScreen(onBackClick = {})
    }
}
