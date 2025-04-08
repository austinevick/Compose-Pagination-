package com.austinevick.imagesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.austinevick.imagesearchapp.presentation.HomeScreen
import com.austinevick.imagesearchapp.ui.theme.ImageSearchAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageSearchAppTheme {
                HomeScreen()
            }
        }
    }
}
