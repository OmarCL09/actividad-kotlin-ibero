package com.example.makefsiberoamericana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Inicializar Firebase Auth y Database Reference
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        // Inicializar vistas
        emailEdit = findViewById(R.id.user_email)
        passwordEdit = findViewById(R.id.user_name)
        saveButton = findViewById(R.id.save_button)
        backButton = findViewById(R.id.backpage_button)

        // Obtener el email y la contraseña del usuario desde el intent
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPassword = intent.getStringExtra("USER_PASSWORD")
        if (userEmail != null) {
            emailEdit.setText(userEmail)
        }
        if (userPassword != null) {
            passwordEdit.setText(userPassword)
        }

        saveButton.setOnClickListener {
            saveUserData()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, VideosActivity::class.java).apply {
                putExtra("USER_EMAIL", emailEdit.text.toString())
                putExtra("USER_PASSWORD", passwordEdit.text.toString())
            }
            startActivity(intent)
        }
    }

    private fun saveUserData() {
        val newEmail = emailEdit.text.toString()
        val newPassword = passwordEdit.text.toString()
        val user: FirebaseUser? = auth.currentUser

        if (user != null) {
            var isUpdated = false

            if (newEmail.isNotEmpty() && newEmail != user.email) {
                user.updateEmail(newEmail)
                    .addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            dbRef.child(user.uid).child("email").setValue(newEmail)
                            Toast.makeText(this, "Email actualizado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error al actualizar el email: ${emailTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                isUpdated = true
            }

            if (newPassword.isNotEmpty()) {
                user.updatePassword(newPassword)
                    .addOnCompleteListener { passwordTask ->
                        if (passwordTask.isSuccessful) {
                            Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error al actualizar la contraseña: ${passwordTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                isUpdated = true
            }

            if (!isUpdated) {
                Toast.makeText(this, "No hay cambios para actualizar", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}
