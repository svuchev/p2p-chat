package com.stoyanvuchev.p2pchat.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoyanvuchev.p2pchat.core.util.IPAddressUtil
import com.stoyanvuchev.p2pchat.network.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SetupScreenViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _serverStatus = MutableStateFlow("Server not running.")
    val serverStatus = _serverStatus.asStateFlow()

    private val _ipText = MutableStateFlow("127.0.0.1")
    val ipText = _ipText.asStateFlow()

    private val _portText = MutableStateFlow("1243")
    val portText = _portText.asStateFlow()

    init {
        initialize()
    }

    fun setIpText(newText: String) {
        _ipText.update { newText }
    }

    fun setPortText(newText: String) {
        _portText.update { newText }
    }

    fun startServer() {

        val ipAddress = IPAddressUtil.getLocalIPv4Address()
        val port = _portText.value.toInt()

        ipAddress?.let { ip ->
            chatRepository.startServer(ip, port)
            _serverStatus.update { "$ip : $port" }
        }

    }

    fun stopServer() {
        chatRepository.stopServer()
        _serverStatus.update { "Server not running." }
    }

    private fun initialize() {
        viewModelScope.launch {
            val clientIP = withContext(Dispatchers.IO) { IPAddressUtil.getLocalIPv4Address() }
            if (clientIP != null) {
                _ipText.update { clientIP }
            }
        }
    }

}