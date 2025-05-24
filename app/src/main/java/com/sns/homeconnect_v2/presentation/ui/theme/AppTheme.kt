import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColors = lightColorScheme(
    primary = Color(0xFF3D8CF0),
    secondary = Color(0xFFA4C8F0),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color(0xFF333333),
    onBackground = Color(0xFF333333),
    onSurface = Color(0xFF333333),
    error = Color(0xFFF44336),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF3D8CF0),
    secondary = Color(0xFF2A6ACF),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color(0xFFEAEAEA),
    onBackground = Color(0xFFEAEAEA),
    onSurface = Color(0xFFEAEAEA),
    error = Color(0xFFF44336),
    onError = Color.White
)

private val AppTypography = Typography() // You can customize text styles if needed

@Composable
fun IoTHomeConnectAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    // Áp dụng màu primary cho status bar
    val systemUiController = rememberSystemUiController()
    val primaryColor = if (darkTheme) DarkColors.primary else LightColors.primary

    SideEffect {
        // Thiết lập màu primaryColor cho status bar
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = !darkTheme // Sử dụng icon màu tối khi theme là light
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}