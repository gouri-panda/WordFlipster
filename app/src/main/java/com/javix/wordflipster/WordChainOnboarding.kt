package com.javix.wordflipster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.javix.wordflipster.ui.theme.WordFlipsterTheme

class WordChainOnboarding: ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordFlipsterTheme {
                val context = LocalContext.current
                val dataStoreManager = remember {DataStoreManager(context)}
                val coroutineScope = rememberCoroutineScope()
                MinutesButton(dataStoreManager =dataStoreManager , coroutineScope = coroutineScope)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun WordChainOnboardingWrapper() {
        WordFlipsterTheme {
            val context = LocalContext.current
            val dataStoreManager = remember {DataStoreManager(context)}
            val coroutineScope = rememberCoroutineScope()
            MinutesButton(dataStoreManager =dataStoreManager , coroutineScope = coroutineScope)
        }
}