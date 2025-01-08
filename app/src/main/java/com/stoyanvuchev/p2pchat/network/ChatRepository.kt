package com.stoyanvuchev.p2pchat.network

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.stoyanvuchev.p2pchat.domain.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val context: Context
) {

    private var chatService: ChatService? = null
    private var isBound = false

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ChatService.ChatServiceBinder
            chatService = binder.service
            isBound = true
            chatService?.messages?.onEach { serializedMsg ->
                val message = Json.decodeFromString<Message>(serializedMsg)
                _messages.value += message
            }?.launchIn(CoroutineScope(Dispatchers.IO))
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            chatService = null
            isBound = false
        }

    }

    fun startService() {
        if (!isBound) {
            val intent = Intent(context, ChatService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }

    fun bindService() {
        if (!isBound) {
            val intent = Intent(context, ChatService::class.java)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService() {
        context.unbindService(serviceConnection)
        isBound = false
    }

    fun startServer(ipAddress: String, port: Int) {
       if (isBound) {
           chatService?.startServer(ipAddress, port)
       }
    }

    fun stopServer() {
        chatService?.stopServer()
    }

    suspend fun connectToServer(serverIP: String, port: Int) {
        chatService?.connectToServer(serverIP, port)
    }

    suspend fun sendMessage(message: Message) {
        val serializedMsg = Json.encodeToString(message)
        chatService?.sendMessage(serializedMsg)
    }

}