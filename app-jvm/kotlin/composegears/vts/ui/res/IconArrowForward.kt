package composegears.vts.ui.res

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconArrowForwardSmall: ImageVector
    get() {
        if (_ArrowForwardSmall != null) {
            return _ArrowForwardSmall!!
        }
        _ArrowForwardSmall = ImageVector.Builder(
            name = "ArrowForward",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveToRelative(321f, 880f)
                lineToRelative(-71f, -71f)
                lineToRelative(329f, -329f)
                lineToRelative(-329f, -329f)
                lineToRelative(71f, -71f)
                lineToRelative(400f, 400f)
                lineTo(321f, 880f)
                close()
            }
        }.build()

        return _ArrowForwardSmall!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowForwardSmall: ImageVector? = null
