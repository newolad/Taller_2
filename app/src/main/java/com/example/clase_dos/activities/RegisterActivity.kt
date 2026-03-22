package com.example.clase_dos.activities

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clase_dos.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_register)

        // Corregido: Usamos el nuevo ID re_ll_main
        val rootView = findViewById<ViewGroup>(R.id.re_ll_main)
        
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
                val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
                insets
            }
        }

        // Corregido: Referencia al ID re_tv_ir_login para volver al Login
        findViewById<TextView>(R.id.re_tv_ir_login).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}