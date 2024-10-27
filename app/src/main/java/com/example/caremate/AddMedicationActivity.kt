package com.example.caremate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.caremate.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class AddMedicationActivity : ComponentActivity() {

    private lateinit var currentPhotoPath: String
    private lateinit var tvMedicationsDisplay: LinearLayout
    private val medicationsList = mutableListOf<String>()

    private val requestCameraPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show()
            }
        }

    private val takePictureLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val file = File(currentPhotoPath)
                addImageToMedicationsDisplay(file.absolutePath)
                findViewById<EditText>(R.id.etMedicationName).visibility = EditText.GONE // Hide the text box
                findViewById<ImageButton>(R.id.btnScanMedication).visibility = ImageButton.GONE // Hide the camera button
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medication)

        val initialMedicationInput: EditText = findViewById(R.id.etMedicationName)
        val btnSendMedication: ImageButton = findViewById(R.id.btnSendMedication)
        val btnScanMedication: ImageButton = findViewById(R.id.btnScanMedication)
        tvMedicationsDisplay = findViewById(R.id.tvMedicationsDisplay) // Initialize the container

        btnSendMedication.setOnClickListener {
            val medicationName = initialMedicationInput.text.toString()
            if (medicationName.isNotEmpty()) {
                addTextToMedicationsDisplay(medicationName)
                initialMedicationInput.text.clear()
                btnScanMedication.visibility = ImageButton.GONE // Hide the camera button if there is text input
            } else {
                Toast.makeText(this, "Please enter a medication name", Toast.LENGTH_SHORT).show()
            }
        }

        btnScanMedication.setOnClickListener {
            if (checkAndRequestPermissions()) {
                dispatchTakePictureIntent()
            }
        }
    }

    private fun addTextToMedicationsDisplay(medicationName: String) {
        // Create a new TextView to display the medication
        val textView = TextView(this).apply {
            text = medicationName
            textSize = 16f
            setPadding(16, 8, 16, 8)
        }
        // Add it to the container
        tvMedicationsDisplay.addView(textView)
    }

    private fun addImageToMedicationsDisplay(imagePath: String) {
        // Create an ImageView to display the image
        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400 // Set a specific height for the image preview
            ).apply {
                setMargins(16, 8, 16, 8)
            }
            setImageBitmap(BitmapFactory.decodeFile(imagePath))
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        // Add it to the container
        tvMedicationsDisplay.addView(imageView)
    }

    private fun checkAndRequestPermissions(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            false
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.caremate.fileprovider", photoFile)
                currentPhotoPath = photoFile.absolutePath
                takePictureLauncher.launch(photoURI)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}
