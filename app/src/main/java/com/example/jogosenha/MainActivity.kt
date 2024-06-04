package com.example.jogosenha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jogosenha.ui.theme.JogoSenhaTheme
import com.example.jogosenha.ui.theme.White
import com.example.jogosenha.ui.theme.WhitePurple
import kotlinx.coroutines.delay
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
        "home" -> HomeScreen(
            onStartClick = { currentScreen = "user_input" },
            onAllLevelsClick = { currentScreen = "all_levels" }
        )
        "all_levels" -> AllLevelsScreen(
            onBackClick = { currentScreen = "home" },
            onLevelClick = { level ->
                currentScreen = when (level) {
                    1 -> "user_input"
                    2 -> "level_2"
                    3 -> "level_3"
                    4 -> "level_4"
                    else -> "home"
                }
            }
        )
        "user_input" -> UserInputScreen(
            onBackClick = { currentScreen = "home" },
            onNextLevelClick = { currentScreen = "level_2" }
        )
        "level_2" -> Level2Screen(
            onBackClick = { currentScreen = "home" },
            onNextLevelClick = { currentScreen = "level_3" }
        )
        "level_3" -> Level3Screen(
            onBackClick = { currentScreen = "home" },
            onNextLevelClick = { currentScreen = "level_4" }
        )
        "level_4" -> Level4Screen(
            onBackClick = { currentScreen = "home" }
        )
    }
}

@Composable
fun HomeScreen(onStartClick: () -> Unit, onAllLevelsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jogo Senha", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "Bem-vindo ao Jogo Senha! Clique em Start para começar.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        CustomButton(onClick = onStartClick, text = "Start", modifier = Modifier.padding(bottom = 16.dp))
        CustomButton(onClick = onAllLevelsClick, text = "All Levels")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllLevelsScreen(onBackClick: () -> Unit, onLevelClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "All Levels", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
        for (level in 1..4) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onLevelClick(level) },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, WhitePurple)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Level $level", style = MaterialTheme.typography.bodyLarge, color = White)
                }
            }
        }
        CustomButton(onClick = onBackClick, text = "Back", modifier = Modifier.padding(top = 16.dp))
    }
}
@Composable
fun UserInputScreen(onBackClick: () -> Unit, onNextLevelClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber(1) }
    var gameWon by remember { mutableStateOf(false) }
    var waitingForInput by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Level 1",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = textState.value,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    textState.value = it
                }
            },
            label = { Text("Digite 4 dígitos", color = MaterialTheme.colorScheme.onBackground) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WhitePurple,
                unfocusedBorderColor = WhitePurple
            )
        )
        CustomButton(
            onClick = {
                if (textState.value.length == 4) {
                    val result = checkBullsAndCows(generatedNumber, textState.value)
                    storedValues = storedValues + (textState.value to result)
                    textState.value = ""
                    if (result == "4B 0C") {
                        gameWon = true
                    }
                    waitingForInput = false
                }
            }, text = "Enviar", Modifier.padding(top = 16.dp)
        )
        if (waitingForInput) {
            Text(
                text = "Esperando usuário digitar...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            storedValues.forEach { (value, result) ->
                Text(
                    text = "Você enviou: $value   $result",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        if (gameWon) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Muito bem!") },
                text = { Text(text = "Você acertou o número :)") },
                confirmButton = {
                    TextButton(onClick = onNextLevelClick) {
                        Text("Próximo Nível")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onBackClick) {
                        Text("Voltar para Home")
                    }
                }
            )
        }
        CustomButton(
            onClick = onBackClick,
            text = "Back",
            Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Resposta: $generatedNumber (Apenas para testes)",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun CustomButton(onClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, WhitePurple),
        modifier = modifier
    ) {
        Text(text = text, color = White)
    }
}
@Composable
fun Level2Screen(onBackClick: () -> Unit, onNextLevelClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber(2) }
    var gameWon by remember { mutableStateOf(false) }
    var waitingForInput by remember { mutableStateOf(true) }

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
                    waitingForInput = false
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Enviar")
        }
        if (waitingForInput) {
            Text(
                text = "Esperando usuário digitar...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            storedValues.forEach { (value, result) ->
                Text(text = "Você enviou: $value   $result", modifier = Modifier.padding(top = 4.dp))
            }
        }
        if (gameWon) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Muito bem!") },
                text = { Text(text = "Você acertou o número :)") },
                confirmButton = {
                    TextButton(onClick = onNextLevelClick) {
                        Text("Próximo Nível")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onBackClick) {
                        Text("Voltar para Home")
                    }
                }
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

@Composable
fun Level3Screen(onBackClick: () -> Unit, onNextLevelClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber(3) }
    var gameWon by remember { mutableStateOf(false) }
    var waitingForInput by remember { mutableStateOf(true) }

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
                    waitingForInput = false
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Enviar")
        }
        if (waitingForInput) {
            Text(
                text = "Esperando usuário digitar...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            storedValues.forEach { (value, result) ->
                Text(text = "Você enviou: $value   $result", modifier = Modifier.padding(top = 4.dp))
            }
        }
        if (gameWon) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Muito bem!") },
                text = { Text(text = "Você acertou o número :)") },
                confirmButton = {
                    TextButton(onClick = onNextLevelClick) {
                        Text("Próximo Nível")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onBackClick) {
                        Text("Voltar para Home")
                    }
                }
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

@Composable
fun Level4Screen(onBackClick: () -> Unit) {
    val textState = remember { mutableStateOf("") }
    var storedValues by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val generatedNumber = remember { generateRandomNumber(1) }
    var gameWon by remember { mutableStateOf(false) }
    var waitingForInput by remember { mutableStateOf(true) }
    var startTime by remember { mutableStateOf<Long?>(null) }
    var remainingTime by remember { mutableStateOf(60) }
    val isTimeOver = remember { mutableStateOf(false) }

    LaunchedEffect(startTime) {
        if (startTime != null) {
            while (remainingTime > 0 && !gameWon && !isTimeOver.value) {
                delay(1000L)
                remainingTime -= 1
                if (remainingTime <= 0) {
                    isTimeOver.value = true
                }
            }
        }
    }

    if (isTimeOver.value && !gameWon) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "Tempo Esgotado!") },
            text = { Text(text = "O tempo acabou. Você deseja tentar novamente ou voltar para a tela inicial?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Reset state for retry
                        textState.value = ""
                        storedValues = listOf()
                        gameWon = false
                        waitingForInput = true
                        startTime = null
                        remainingTime = 60
                        isTimeOver.value = false
                    }
                ) {
                    Text("Tentar Novamente")
                }
            },
            dismissButton = {
                TextButton(onClick = onBackClick) {
                    Text("Voltar para Home")
                }
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Level 4", style = MaterialTheme.typography.headlineLarge)
                Text(text = "Tempo: ${remainingTime}s", style = MaterialTheme.typography.headlineLarge)
            }
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
                        if (startTime == null) {
                            startTime = System.currentTimeMillis()
                        }
                        val result = checkBullsAndCows(generatedNumber, textState.value)
                        storedValues = storedValues + (textState.value to result)
                        textState.value = ""
                        if (result == "4B 0C") {
                            gameWon = true
                        }
                        waitingForInput = false
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Enviar")
            }
            if (waitingForInput) {
                Text(
                    text = "Esperando usuário digitar...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Column(modifier = Modifier.padding(top = 16.dp)) {
                storedValues.forEach { (value, result) ->
                    Text(text = "Você enviou: $value   $result", modifier = Modifier.padding(top = 4.dp))
                }
            }
            if (gameWon) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = "Muito bem!") },
                    text = { Text(text = "Você acertou o número :)") },
                    confirmButton = {
                        /*TextButton(onClick = onNextLevelClick) {
                            Text("Próximo Nível")
                        }*/
                    },
                    dismissButton = {
                        TextButton(onClick = onBackClick) {
                            Text("Voltar para Home")
                        }
                    }
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
}

@Composable
fun Level5Screen(onBackClick: () -> Unit) {

}

fun generateRandomNumber(level: Int): String {
    val digits = (0..9).toMutableList()
    return when (level) {
        1 -> (1..4).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
        2 -> (1..5).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
        3 -> (1..6).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
        4 -> (1..4).map { digits.removeAt(Random.nextInt(digits.size)) }.joinToString("")
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
        Level4Screen(onBackClick = {})
    }
}
