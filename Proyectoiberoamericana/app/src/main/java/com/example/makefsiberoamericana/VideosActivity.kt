package com.example.makefsiberoamericana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class VideosActivity : AppCompatActivity() {

    private lateinit var welcomeMessage: TextView
    private lateinit var perfilButton: Button
    private lateinit var uploadButton: Button
    private lateinit var videosList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        welcomeMessage = findViewById(R.id.welcome_message)
        perfilButton = findViewById(R.id.btn_perfil)
        uploadButton = findViewById(R.id.upload_video)
        videosList = findViewById(R.id.videos_list)

        // Obtener el email y la contraseña del usuario desde el intent
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPassword = intent.getStringExtra("USER_PASSWORD")
        val videoName = intent.getStringExtra("VIDEO_NAME")

        // Mostrar el email del usuario en el mensaje de bienvenida
        if (userEmail != null) {
            welcomeMessage.text = "Bienvenido $userEmail!"
        } else {
            welcomeMessage.text = "Bienvenido usuario!"
        }

        // Mostrar el nombre del video si está disponible
        if (videoName != null && videoName.isNotEmpty()) {
            addVideoToList(videoName, "Hecho por $userEmail", "1000+ vistas")
        }

        perfilButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("USER_EMAIL", userEmail)
                putExtra("USER_PASSWORD", userPassword)
            }
            startActivity(intent)
        }

        uploadButton.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java).apply {
                putExtra("USER_EMAIL", userEmail)
                putExtra("USER_PASSWORD", userPassword)
            }
            startActivity(intent)
        }
    }

    private fun addVideoToList(title: String, author: String, views: String) {
        val videoItem = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
            background = ContextCompat.getDrawable(context, R.drawable.video_item_background)
            setMargins(16, 16, 16, 16)
        }

        val videoImageView = ImageView(this).apply {
            setImageResource(R.drawable.tamales) // Imagen de ejemplo
            layoutParams = LinearLayout.LayoutParams(200, 200).apply {
                marginEnd = 16
            }
        }

        val textContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val titleTextView = TextView(this).apply {
            text = title
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }

        val authorTextView = TextView(this).apply {
            text = author
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
        }

        val viewsTextView = TextView(this).apply {
            text = views
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
        }

        textContainer.addView(titleTextView)
        textContainer.addView(authorTextView)
        textContainer.addView(viewsTextView)

        videoItem.addView(videoImageView)
        videoItem.addView(textContainer)
        videosList.addView(videoItem)
    }

    private fun LinearLayout.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(left, top, right, bottom)
        layoutParams = params
    }
}
