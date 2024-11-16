package com.javix.wordflipster

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun WordChainOnboardingWrapper() {
    Box(modifier = Modifier
        .padding(8.dp)
        .padding(WindowInsets.ime.asPaddingValues())) {
            val context = LocalContext.current
            val dataStoreManager = remember { DataStoreManager(context) }
            val coroutineScope = rememberCoroutineScope()
            MinutesButton(dataStoreManager = dataStoreManager, coroutineScope = coroutineScope)
        }
}