package com.example.studentapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.studentapp.ui.theme.StudentAppTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentAppTheme {
                MainScreen(onNavigate = { destination -> navigateTo(destination) })
            }
        }
    }

    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }
}

// ✅ Move this function OUTSIDE MainActivity so it can be accessed in @Preview
@Composable
fun MainScreen(onNavigate: (Class<*>) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Enables scrolling
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Professional & Stylish Heading
        Text(
            text = "Welcome to Your Student Portal",
            fontSize = 26.sp,  // Increased size
            fontWeight = FontWeight.ExtraBold, // Bolder text
            color = Color.Black,  // Changed to black
            letterSpacing = 1.sp, // Added spacing for a clean look
            modifier = Modifier.padding(top = 40.dp, bottom = 32.dp) // Adjusted padding
        )

        // Add Students Card
        StudentCard(
            title = "Add Students",
            description = "Register new students in the system.",
            onClick = { onNavigate(StudentFormActivity::class.java) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // View Students Card
        StudentCard(
            title = "View Students",
            description = """
        - Display all students
        - Search for a student
        - Edit student information
        - Delete a student
    """.trimIndent(),

            onClick = { onNavigate(StudentViewActivity::class.java) }
        )

        // Spacer to push the footer to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Footer
        Text(
            text = "© 2025 Student Portal",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(bottom = 16.dp) // Adjust the padding as needed
        )
    }
}

// ✅ Move StudentCard outside MainActivity for reusability
@Composable
fun StudentCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = description, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

// Now @Preview works because MainScreen is outside MainActivity
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    StudentAppTheme {
        MainScreen(onNavigate = {}) // Empty lambda for preview
    }
}
