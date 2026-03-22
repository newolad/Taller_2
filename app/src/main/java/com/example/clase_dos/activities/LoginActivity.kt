package com.example.clase_dos.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clase_dos.R
import com.example.clase_dos.activities.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Referencia correcta al ID del texto de registro
        val tvRegistrate = findViewById<TextView>(R.id.in_tv_registrate)
        tvRegistrate.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Referencia correcta al botón de ingresar
        val btnIngresar = findViewById<Button>(R.id.in_btn_ingresar)
        btnIngresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerramos login al entrar a la main
        }

        // Configuración de insets para el layout principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.in_ll_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}