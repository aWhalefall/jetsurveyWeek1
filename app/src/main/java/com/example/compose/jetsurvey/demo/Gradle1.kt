package com.example.compose.jetsurvey.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.example.compose.jetsurvey.R


@Preview(showSystemUi = true)
@Composable
fun RecomposePreView() {
    Gradle1()
}

@Composable
fun Gradle1() {
    Box {
        var imageHeightPx by remember { mutableStateOf(0) }

        Image(
            painter = painterResource(R.drawable.bug_of_chaos),
            contentDescription = "I'm above the text",
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    // Don't do this
                    imageHeightPx = size.height
                }
        )

        Text(
            text = "I'm below the image",
            modifier = Modifier.padding(
                top = with(LocalDensity.current) { imageHeightPx.toDp() }
            ).align(Alignment.BottomCenter).offset { IntOffset(0,-10) }
        )

        print("渲染了 ${System.currentTimeMillis()}")

    }
}



