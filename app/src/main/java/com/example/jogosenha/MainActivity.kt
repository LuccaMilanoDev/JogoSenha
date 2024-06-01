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
    var storedValues by remember { mutableStateOf(listOf<String>()) }

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
                if (textState.value.isNotEmpty()) {
                    storedValues = storedValues + textState.value
                    textState.value = ""
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Enviar")
        }
        Column(modifier = Modifier.padding(top = 16.dp)) {
            storedValues.forEach { value ->
                Text(text = "Você enviou: $value", modifier = Modifier.padding(top = 4.dp))
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


@Preview(showBackground = true)
@Composable
fun UserInputScreenPreview() {
    JogoSenhaTheme {
        UserInputScreen(onBackClick = {})
    }
}
