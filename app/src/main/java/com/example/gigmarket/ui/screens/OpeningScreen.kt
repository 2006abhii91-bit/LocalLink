package com.example.gigmarket.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gigmarket.ui.components.LocalLinkLogo
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import com.example.gigmarket.ui.theme.GigMarketTheme
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonElectricBlue
import com.example.gigmarket.ui.theme.NeonLinkOrange
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OpeningScreen(navController: NavController) {
    val infiniteTransition = rememberInfiniteTransition(label = "landing")

    // Particle drift offset
    val particleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleOffset"
    )

    // Background gradient pulse
    val gradientAlpha by infiniteTransition.animateFloat(
        initialValue = 0.06f,
        targetValue = 0.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkBackgroundSecondary)
                )
            )
    ) {
        // Floating particle background
        ParticleBackground(
            particleOffset = particleOffset,
            gradientAlpha = gradientAlpha,
            modifier = Modifier.fillMaxSize()
        )

        // Content centered, max width 430dp for mobile-first
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 430.dp)
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // LocalLink logo with tagline ("Connecting Your Community")
            LocalLinkLogo(
                fontSize = 48.sp,
                taglineFontSize = 15.sp,
                showTagline = true
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Primary (cyan) Login Button
            PrimaryCyanButton(
                text = "Login",
                onClick = { navController.navigate("worker_login") }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Secondary (outlined) Create Account Button
            SecondaryOutlinedButton(
                text = "Create Account",
                onClick = { navController.navigate("worker_signup") }
            )
        }
    }
}

@Composable
fun PrimaryCyanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "primaryBtnGlow")
    val hoverGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hoverGlow"
    )

    val scale = if (isPressed) 0.97f else 1f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .height(56.dp)
            .shadow(
                elevation = if (isPressed) 8.dp else 18.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = NeonCyan.copy(alpha = hoverGlow * 0.7f),
                spotColor = NeonCyan.copy(alpha = hoverGlow * 0.7f)
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonCyan,
                        NeonElectricBlue
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBackground,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SecondaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale = if (isPressed) 0.97f else 1f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .height(56.dp)
            .shadow(
                elevation = if (isPressed) 4.dp else 10.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = NeonCyan.copy(alpha = 0.25f),
                spotColor = NeonCyan.copy(alpha = 0.25f)
            )
            .background(
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(28.dp)
            )
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.85f),
                        NeonCyan.copy(alpha = 0.45f)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ParticleBackground(
    particleOffset: Float,
    gradientAlpha: Float,
    modifier: Modifier = Modifier
) {
    val particles = remember {
        listOf(
            Triple(Offset(0.15f, 0.2f),  0.003f, NeonElectricBlue),
            Triple(Offset(0.80f, 0.15f), 0.002f, NeonLinkOrange),
            Triple(Offset(0.55f, 0.45f), 0.004f, NeonElectricBlue),
            Triple(Offset(0.25f, 0.70f), 0.0025f, NeonLinkOrange),
            Triple(Offset(0.70f, 0.75f), 0.003f, NeonElectricBlue),
            Triple(Offset(0.40f, 0.10f), 0.002f, NeonLinkOrange),
            Triple(Offset(0.90f, 0.55f), 0.0035f, NeonElectricBlue),
            Triple(Offset(0.10f, 0.88f), 0.002f, NeonLinkOrange),
            Triple(Offset(0.65f, 0.30f), 0.003f, NeonElectricBlue),
            Triple(Offset(0.35f, 0.55f), 0.0025f, NeonLinkOrange),
            Triple(Offset(0.85f, 0.88f), 0.002f, NeonElectricBlue),
            Triple(Offset(0.05f, 0.45f), 0.004f, NeonLinkOrange),
        )
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NeonElectricBlue.copy(alpha = gradientAlpha),
                    Color.Transparent
                ),
                center = Offset(w * 0.2f, h * 0.3f),
                radius = w * 0.6f
            ),
            radius = w * 0.6f,
            center = Offset(w * 0.2f, h * 0.3f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NeonLinkOrange.copy(alpha = gradientAlpha * 0.8f),
                    Color.Transparent
                ),
                center = Offset(w * 0.8f, h * 0.7f),
                radius = w * 0.5f
            ),
            radius = w * 0.5f,
            center = Offset(w * 0.8f, h * 0.7f)
        )

        particles.forEachIndexed { index, (origin, speed, color) ->
            val angle = (particleOffset + index * 0.08f) * 2 * PI.toFloat()
            val orbitRadius = 30f + index * 8f
            val cx = origin.x * w + cos(angle * speed * 200) * orbitRadius
            val cy = origin.y * h + sin(angle * speed * 200) * orbitRadius
            val particleRadius = 2.5f + (index % 3) * 1.5f

            drawCircle(
                color = color.copy(alpha = 0.4f + 0.25f * sin(angle + index).toFloat()),
                radius = particleRadius,
                center = Offset(cx, cy)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = particleRadius * 5
                ),
                radius = particleRadius * 5,
                center = Offset(cx, cy)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
fun OpeningScreenPreview() {
    GigMarketTheme {
        OpeningScreen(navController = rememberNavController())
    }
}

