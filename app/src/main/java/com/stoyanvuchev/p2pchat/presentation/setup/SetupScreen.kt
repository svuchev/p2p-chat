package com.stoyanvuchev.p2pchat.presentation.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.p2pchat.R
import com.stoyanvuchev.p2pchat.core.ui.theme.P2PChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    serverStatus: String,
    ipText: String,
    portText: String,
    onStartServer: () -> Unit,
    onStopServer: () -> Unit,
    onConnectToServer: () -> Unit,
    onSetIP: (String) -> Unit,
    onSetPort: (String) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) }
            )

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = serverStatus)

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onStartServer) { Text(text = "Start") }
                Button(onClick = onStopServer) { Text(text = "Stop") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "----- or -----"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = ipText,
                    onValueChange = { onSetIP(it) },
                    placeholder = { Text("e.g. 127.0.0.1") }
                )

                Text(text = ":")

                OutlinedTextField(
                    modifier = Modifier.weight(.67f),
                    value = portText,
                    onValueChange = { onSetPort(it) },
                    placeholder = { Text("e.g. 8484") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }

            Spacer(modifier = Modifier.height(0.dp))

            Button(onClick = onConnectToServer) { Text(text = "Connect") }

        }

    }

}

@Preview
@Composable
private fun SetupScreenPreview() {
    P2PChatTheme {
        SetupScreen(
            serverStatus = "Server not running.",
            ipText = "127.0.0.1",
            portText = "8484",
            onSetPort = {},
            onSetIP = {},
            onStartServer = {},
            onStopServer = {},
            onConnectToServer = {}
        )
    }
}