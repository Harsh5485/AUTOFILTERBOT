package com.harshtg.streaming.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.dpadFocusable(
    onFocus: () -> Unit = {},
    scaleFocused: Float = 1.08f,
    cornerRadius: Dp = 12.dp
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) scaleFocused else 1.0f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )

    return this
        .scale(scale)
        .onFocusChanged { state ->
            isFocused = state.isFocused
            if (state.isFocused) onFocus()
        }
        .border(
            width = if (isFocused) 3.dp else 0.dp,
            color = if (isFocused) MaterialTheme.colorScheme.secondary else Color.Transparent,
            shape = RoundedCornerShape(cornerRadius)
        )
        .focusable()
}
