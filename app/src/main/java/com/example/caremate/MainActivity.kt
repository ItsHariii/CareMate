package com.example.caremate // Make sure this matches your app package


import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.view.View

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set an OnClickListener for the entire view to detect clicks anywhere on the screen
        val mainLayout: View = findViewById(R.id.backgroundImage)
        mainLayout.setOnClickListener {
            // Navigate to com.example.caremate.AddMedicationActivity
            val intent = Intent(this, AddMedicationActivity::class.java)
            startActivity(intent)
        }
    }
}

