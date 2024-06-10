package com.example.makefsiberoamericana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class UploadActivity : AppCompatActivity() {

    private lateinit var videoNameEditText: EditText
    private lateinit var uploadButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload)

        // Inicializar vistas
        videoNameEditText = findViewById(R.id.video_name_input)
        uploadButton = findViewById(R.id.upload_button)
        backButton = findViewById(R.id.back2_button)

        // Configurar el botón de subir video
        uploadButton.setOnClickListener {
            val videoName = videoNameEditText.text.toString()
            val intent = Intent(this, VideosActivity::class.java).apply {
                putExtra("VIDEO_NAME", videoName)
            }
            startActivity(intent)
        }

        // Configurar el botón de volver
        backButton.setOnClickListener {
            val intent = Intent(this, VideosActivity::class.java)
            startActivity(intent)
        }
    }
}
