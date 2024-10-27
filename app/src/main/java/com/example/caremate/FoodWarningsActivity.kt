package com.example.caremate

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class FoodWarningsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_warnings) // This should point to the correct XML layout

        // Set up the ListView for displaying food warnings
        val listView: ListView = findViewById(R.id.foods_list)
        val foods = arrayOf("Grapefruit", "Alcohol", "Dairy Products", "Leafy Greens")

        // Set up an adapter to display the list of foods
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foods)
        listView.adapter = adapter

        // Set an item click listener if needed
        listView.setOnItemClickListener { _, _, position, _ ->
            // Example: Show a message or handle a click action
            // You can display a toast message or navigate to a details screen
        }
    }
}
