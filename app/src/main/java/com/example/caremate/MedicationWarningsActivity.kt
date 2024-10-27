package com.example.caremate

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MedicationWarningsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medication_warnings)

        // Ensure the layout being set contains 'warning_text_view'
        val warningTextView: TextView = findViewById(R.id.warning_text_view)
        warningTextView.text = "List of Medication Warnings"
    }
}
