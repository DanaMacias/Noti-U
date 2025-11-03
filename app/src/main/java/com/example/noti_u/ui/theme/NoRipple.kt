package com.example.noti_u.ui.theme



import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun buttonAnimation(
    drawableId: Int, // Image resource ID for the button
    modifier: Modifier = Modifier, // Optional modifier for size or positioning
    onClick: () -> Unit = {} // Action executed when the button is clicked
) {
    // Detects user interaction (whether the button is pressed)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth scale animation: enlarges when pressed, returns to normal when released
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1f,
        label = "scaleAnimation"
    )

    // Displays the image that behaves like a button with an animation
    Image(
        painter = painterResource(id = drawableId), // Loads the image from resources
        contentDescription = null, // No description since it’s decorative
        contentScale = ContentScale.Fit, // Keeps image proportions
        modifier = modifier
            .size(90.dp) // Sets the button size
            .scale(scale) // Applies the zoom animation
            .clickable(
                interactionSource = interactionSource, // Handles press detection
                indication = null // Disables ripple effect for a cleaner look
            ) {
                onClick() // Executes the action when clicked
            }
    )
}