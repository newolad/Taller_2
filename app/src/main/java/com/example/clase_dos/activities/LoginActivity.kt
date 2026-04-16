package com.example.clase_dos.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.clase_dos.R
import com.example.clase_dos.activities.main.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etContraseña: EditText
    private lateinit var btnIngresar: Button
    private lateinit var tvRegistrate: TextView
    private lateinit var tvOlvidasteContraseña: TextView
    private lateinit var tvGoogle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Configuración de rootview para manejar el teclado y insets
        val rootview = findViewById<LinearLayout>(R.id.in_ll_main)
        ViewCompat.setOnApplyWindowInsetsListener(rootview) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                bottomPadding
            )
            insets
        }

        etCorreo = findViewById(R.id.in_et_usuario)
        etContraseña = findViewById(R.id.in_et_contrasena)
        btnIngresar = findViewById(R.id.in_btn_ingresar)
        tvRegistrate = findViewById(R.id.in_tv_registrate)
        tvOlvidasteContraseña = findViewById(R.id.in_tv_recuperar)
        tvGoogle = findViewById(R.id.in_btn_google)

        // Navegación a la actividad de registro
        tvRegistrate.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Acción al presionar el botón de ingresar (Login con Supabase)
        btnIngresar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val contraseña = etContraseña.text.toString().trim()

            // Validaciones de los campos necesarios
            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Validación de longitud mínima (opcional, según tu política)
            if (contraseña.length < 6) { // Usualmente Supabase pide mínimo 6
                Toast.makeText(this, "La contraseña es demasiado corta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Inicio de sesión con Supabase
            lifecycleScope.launch {
                try {
                    // Cambiado signUpWith por signInWith para el Login
                    SupabaseClient.client.auth.signInWith(Email) {
                        email = correo
                        password = contraseña
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        "Inicio de sesión exitoso",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error al iniciar sesión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        //Inicio de sesion con google
        tvGoogle.setOnClickListener {
            iniciaSesionConGoogle()
        }

    }

    private fun iniciaSesionConGoogle() {
        lifecycleScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("418187571075-q0n7o7cprujfij9sp9e7tqos40imli7m.apps.googleusercontent.com")
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val credentialManager = CredentialManager.create(this@LoginActivity)
                val result = credentialManager.getCredential(this@LoginActivity, request)
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)

                SupabaseClient.client.auth.signInWith(IDToken) {
                    idToken = googleIdTokenCredential.idToken
                    provider = Google
                }

                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Inicio de sesión con Google exitoso",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }


            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error al iniciar sesión con Google: ${e.message}",
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }
}


