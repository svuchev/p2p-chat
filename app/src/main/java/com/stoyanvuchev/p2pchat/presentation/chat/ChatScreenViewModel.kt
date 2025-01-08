package com.stoyanvuchev.p2pchat.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.p2pchat.core.util.IPAddressUtil
import com.stoyanvuchev.p2pchat.domain.model.Message
import com.stoyanvuchev.p2pchat.network.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    private val _clientIP = MutableStateFlow("")
    val clientIP = _clientIP.asStateFlow()

    val messages: StateFlow<List<Message>> = chatRepository.messages
        .map { it.distinct() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            chatRepository.messages.value
        )

    init {
        initialize()
    }

    fun setMessageText(newText: String) {
        _messageText.update { newText }
    }

    fun sendMessage() {
        val msgText = _messageText.value
        if (msgText.isNotBlank()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _clientIP.value.let {
                        val message = Message(
                            content = msgText,
                            senderIpAddress = it,
                            timestamp = System.currentTimeMillis()
                        )
                        chatRepository.sendMessage(message)
                        withContext(Dispatchers.Main) { setMessageText("") }
                    }
                }
            }
        }
    }

    private fun initialize() {
        val clientIP = IPAddressUtil.getLocalIPv4Address()
        val serverIP = savedStateHandle.get<String>("serverIP")
        val port = savedStateHandle.get<Int>("port")
        if (clientIP != null && serverIP != null && port != null) {
            _clientIP.update { clientIP }
            connectToServer(serverIP, port)
        }
    }

    private fun connectToServer(serverIP: String, port: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatRepository.connectToServer(serverIP, port)
                println("Connected to server: $serverIP:$port")
            }
        }
    }

}