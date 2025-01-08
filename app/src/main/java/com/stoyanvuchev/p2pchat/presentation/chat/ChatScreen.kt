package com.stoyanvuchev.p2pchat.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.p2pchat.R
import com.stoyanvuchev.p2pchat.core.ui.theme.P2PChatTheme
import com.stoyanvuchev.p2pchat.domain.model.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<Message>,
    messageText: String,
    onSetMessageText: (String) -> Unit,
    onSendMessage: () -> Unit,
    onNavigateUp: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {

            ChatScreenBottomBar(
                messageText = messageText,
                onSetMessageText = onSetMessageText,
                onSendMessage = onSendMessage
            )

        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            reverseLayout = false
        ) {

            items(messages) { message ->

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .animateItem(),
                    text = message.content
                )

            }

        }

    }

}

@Preview
@Composable
private fun ChatScreenPreview() {
    P2PChatTheme {
        ChatScreen(
            messages = emptyList(),
            messageText = "",
            onSetMessageText = {},
            onSendMessage = {},
            onNavigateUp = {}
        )
    }
}