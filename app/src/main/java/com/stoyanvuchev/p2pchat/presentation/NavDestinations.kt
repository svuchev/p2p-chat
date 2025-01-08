package com.stoyanvuchev.p2pchat.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavDestinations {

    @Serializable
    data object SetupScreen : NavDestinations

    @Serializable
    data class ChatScreen(
        val serverIP: String,
        val port: Int
    ) : NavDestinations

}