package com.stoyanvuchev.p2pchat.network

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.stoyanvuchev.p2pchat.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.ServerSocket
import java.net.Socket

@AndroidEntryPoint
class ChatService : Service() {

    private val logTag = "ChatServiceTag"
    private val binder = ChatServiceBinder()

    private var serverSocket: ServerSocket? = null
    private val clientSockets = mutableListOf<Socket>()

    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    override fun onBind(intent: Intent?): IBinder = binder
    inner class ChatServiceBinder : Binder() {
        val service: ChatService
            get() = this@ChatService
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    fun startServer(ipAddress: String, port: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inetAddress = Inet4Address.getByName(ipAddress)
                serverSocket = ServerSocket(port, 50, inetAddress)
                println("$logTag: Server started on: ${serverSocket?.inetAddress?.hostAddress} : $port")
                while (isActive) {
                    val socket = serverSocket?.accept()
                    socket?.let {
                        synchronized(clientSockets) {
                            clientSockets.add(it)
                        }
                        println("$logTag: New Client connected! ${it.inetAddress.hostAddress}")
                        handleClient(it)
                    } ?: continue
                }
            } catch (e: Exception) {
                Log.e(logTag, ": Error: ${e.message}")
            }
        }
    }

    suspend fun connectToServer(serverIP: String, port: Int) = withContext(Dispatchers.IO) {
        val socket = Socket(serverIP, port)
        synchronized(clientSockets) {
            clientSockets.add(socket)
            launch { handleClient(socket) }
        }
    }

    suspend fun sendMessage(message: String) = withContext(Dispatchers.IO) {
        synchronized(clientSockets) {
            clientSockets.forEach { socket ->
                try {
                    val output = socket.getOutputStream()
                    output.write("$message\n".toByteArray())
                    output.flush()
                    println("$logTag: Sent: $message")
                } catch (e: Exception) {
                    e.printStackTrace()
                    synchronized(clientSockets) {
                        clientSockets.remove(socket)
                    }
                }
            }
        }
    }

    private suspend fun handleClient(socket: Socket) = withContext(Dispatchers.IO) {
        try {
            val input = socket.getInputStream().bufferedReader()
            input.forEachLine { line ->
                println("$logTag: Received: $line")
                launch { _messages.emit(line) }
            }
        } catch (e: Exception) {
            Log.e(logTag, ": Error: ${e.message}")
        }
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("P2P Chat Service Running")
            .setContentText("Messages will be received in the background.")
            .setSmallIcon(R.drawable.round_circle_notifications_24)
            .build()
        startForeground(FOREGROUND_ID, notification)
    }

    fun stopServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                serverSocket?.close()
                serverSocket = null

                synchronized(clientSockets) {
                    clientSockets.forEach { it.close() }
                    clientSockets.clear()
                }

            } catch (e: Exception) {
                Log.e(logTag, ": Error stopping server: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopServer()
    }

    companion object {
        const val CHANNEL_ID = "P2P Chat Service channel"
        const val FOREGROUND_ID = 8484
    }

}