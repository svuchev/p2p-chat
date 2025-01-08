package com.stoyanvuchev.p2pchat.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stoyanvuchev.p2pchat.presentation.chat.ChatScreen
import com.stoyanvuchev.p2pchat.presentation.chat.ChatScreenViewModel
import com.stoyanvuchev.p2pchat.presentation.setup.SetupScreen
import com.stoyanvuchev.p2pchat.presentation.setup.SetupScreenViewModel

@Composable
fun NavigationHost(navController: NavHostController) {

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = NavDestinations.SetupScreen
    ) {

        composable<NavDestinations.SetupScreen> {

            val viewModel = hiltViewModel<SetupScreenViewModel>()
            val serverStatus by viewModel.serverStatus.collectAsStateWithLifecycle()
            val ipText by viewModel.ipText.collectAsStateWithLifecycle()
            val portText by viewModel.portText.collectAsStateWithLifecycle()

            SetupScreen(
                serverStatus = serverStatus,
                ipText = ipText,
                portText = portText,
                onSetPort = viewModel::setPortText,
                onSetIP = viewModel::setIpText,
                onStartServer = viewModel::startServer,
                onStopServer = viewModel::stopServer,
                onConnectToServer = {
                    if (ipText.isNotBlank() && portText.isNotBlank()) {
                        navController.navigate(
                            NavDestinations.ChatScreen(
                                serverIP = ipText,
                                port = portText.toInt()
                            )
                        )
                    }
                }
            )

        }

        composable<NavDestinations.ChatScreen> {

            val viewModel = hiltViewModel<ChatScreenViewModel>()
            val messages by viewModel.messages.collectAsStateWithLifecycle()
            val messageText by viewModel.messageText.collectAsStateWithLifecycle()

            ChatScreen(
                messages = messages,
                messageText = messageText,
                onSetMessageText = viewModel::setMessageText,
                onSendMessage = viewModel::sendMessage,
                onNavigateUp = { navController.navigateUp() }
            )

        }

    }

}