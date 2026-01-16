import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import composegears.vts.App

fun main() = application {
    Window(
        title = "vts",
        onCloseRequest = { exitApplication() },
    ) {
        App()
    }
}