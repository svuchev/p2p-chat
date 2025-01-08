package com.stoyanvuchev.p2pchat.domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Message(
    val content: String,
    val senderIpAddress: String,
    val timestamp: Long
)