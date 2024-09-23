package lk.mzpo.alias.ui.theme

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun FloatingBar(x: Int, y:Int, size: Int = Random.nextInt(20, 200), color: Color = Color.LightGray, target: Float = Random.nextInt(0, 20).toFloat(), duration: Int = Random.nextInt(500, 10000))
{
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseIn),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Box(
        modifier = Modifier
            .size(size.dp)
            .offset(y = x.dp-animatedOffset.dp, x = y.dp+animatedOffset.dp) // Полукруг, чуть ниже текста
            .shadow(10.dp, CircleShape)
            .background(Color.LightGray, shape = CircleShape)
    )
}