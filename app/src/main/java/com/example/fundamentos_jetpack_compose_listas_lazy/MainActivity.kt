package com.example.fundamentos_jetpack_compose_listas_lazy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundamentos_jetpack_compose_listas_lazy.components.GameCard
import com.example.fundamentos_jetpack_compose_listas_lazy.repository.getAllGames
import com.example.fundamentos_jetpack_compose_listas_lazy.ui.theme.FundamentosjetpackcomposelistaslazyTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundamentosjetpackcomposelistaslazyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GamesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun GamesScreen(modifier: Modifier) {
    val searchTextState = remember { mutableStateOf("") }

    val allGames = remember { getAllGames() }

    // Lista de estúdios únicos
    val studios = allGames.map { it.studio }.distinct()

    // Lista filtrada
    val filteredGames = if (searchTextState.value.isEmpty()) {
        allGames
    } else {
        allGames.filter { game ->
            game.studio.contains(searchTextState.value, ignoreCase = true) ||
                    game.title.contains(searchTextState.value, ignoreCase = true)
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Meus jogos favoritos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de busca
        OutlinedTextField(
            value = searchTextState.value,
            onValueChange = { searchTextState.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Nome do estúdio") },
            trailingIcon = {
                IconButton(onClick = { /* opcional: acionar busca manual */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar"
                    )
                }
            }
        )

        // LazyRow com os estúdios
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow {
            items(studios) { studio ->
                Card(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            searchTextState.value = studio
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.ui.graphics.Color.LightGray
                    )
                ) {
                    Text(
                        text = studio,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Exibe o botão "Limpar filtro" se houver texto digitado
        if (searchTextState.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Button(
                onClick = { searchTextState.value = "" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Limpar filtro")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de jogos
        LazyColumn {
            items(filteredGames) { game ->
                GameCard(game = game)
            }
        }
    }
}


