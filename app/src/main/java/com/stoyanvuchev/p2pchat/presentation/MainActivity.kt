package com.stoyanvuchev.p2pchat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.stoyanvuchev.p2pchat.core.ui.theme.P2PChatTheme
import com.stoyanvuchev.p2pchat.network.ChatRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var chatRepository: ChatRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start and bind the Chat Service.
        chatRepository.startService()
        chatRepository.bindService()

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            P2PChatTheme { NavigationHost(navController = navController) }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        chatRepository.unbindService()
    }

}