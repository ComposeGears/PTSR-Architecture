package composegears.vts.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun Surface(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier.background(Theme.Colors.surface)) {
        content()
    }
}

@Composable
fun Text(
    text: String,
    size: TextUnit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = Theme.Colors.onSurface,
            fontSize = size,
            textAlign = textAlign,
        )
    )
}

@Composable
fun TextTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
) {
    Text(
        text = text,
        size = Theme.Typography.title,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun TextLabel(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
) {
    Text(
        text = text,
        size = Theme.Typography.label,
        modifier = modifier,
        textAlign = textAlign,
    )
}
@Composable
fun TextHint(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
) {
    Text(
        text = text,
        size = Theme.Typography.hint,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun TextButton(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
) {
    Text(
        text = text,
        size = Theme.Typography.button,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(2.dp, Theme.Colors.onSurface)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        content()
    }
}

@Composable
fun IconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        imageVector = imageVector,
        contentDescription = null,
        contentScale = ContentScale.Inside,
        modifier = modifier.size(48.dp).clickable(onClick = onClick)
    )
}

@Composable
fun Divider(height: Dp = Dp.Hairline, width: Dp = Dp.Hairline) {
    Box(
        Modifier
            .size(width = width, height = height)
            .background(Theme.Colors.onSurface)
    )
}

@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(modifier.background(Theme.Colors.onSurface))
}

@Composable
fun Loader() {
    var symbol by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val symbols = listOf(".", "..", "...")
        var index = 0
        while (isActive) {
            symbol = symbols[index++ % symbols.size]
            delay(300L)
        }
    }
    Box(Modifier.size(64.dp)) {
        TextTitle(symbol, modifier = Modifier.align(Alignment.Center))
    }
}