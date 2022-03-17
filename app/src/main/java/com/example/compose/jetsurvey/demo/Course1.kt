package com.example.compose.jetsurvey.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello!",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = "test",
            onValueChange = { },
            label = { Text("Name") }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HelloScreen() {
    //状态提升
    var name by remember { mutableStateOf("") }
    HelloContent2(name = name, onValueChange = { name = it })
}




@Composable
fun HelloContent2(name: String, onValueChange: (String) -> Unit) {
    //有状态版本对于不关心状态的调用方来说很方便,不利于复用
    Column(modifier = Modifier.padding(16.dp)) {
        if (name.isNotEmpty()) {
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = onValueChange,
            label = { Text("Name") }
        )
    }
}

