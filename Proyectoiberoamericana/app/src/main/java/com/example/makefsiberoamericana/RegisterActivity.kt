package com.example.makefsiberoamericana

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        // Inicializar Firebase Auth y Database Reference
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        // Inicializar vistas
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val userEmail = emailEditText.text.toString()
        val userPassword = passwordEditText.text.toString()
        val userConfirmPassword = confirmPasswordEditText.text.toString()

        if (userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (userPassword != userConfirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso en Authentication, ahora guardar datos adicionales en Realtime Database
                    val firebaseUser: FirebaseUser? = auth.currentUser
                    val userId = firebaseUser?.uid

                    val user = User(userId, userEmail)

                    if (userId != null) {
                        dbRef.child(userId).setValue(user)
                            .addOnCompleteListener {
                                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
                                emailEditText.setText("")
                                passwordEditText.setText("")
                                confirmPasswordEditText.setText("")
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener {
                                Log.d("RegisterActivity", "Error saving user in Realtime Database", it)
                                Toast.makeText(this, "Error al registrar en la base de datos", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    // Error al registrar en Authentication
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    data class User(val userId: String?, val email: String)
}
