package com.example.clase_dos.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.clase_dos.R
import com.example.clase_dos.data.UsuarioRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etReContrasena: EditText
    private lateinit var cbTerminos: CheckBox
    private lateinit var btnRegistrarse: Button
    private lateinit var tvIrLogin: TextView

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

        //Referenciacion de datos en lista
        etNombres = findViewById(R.id.re_et_nombres)
        etApellidos = findViewById(R.id.re_et_apellidos)
        etCorreo = findViewById(R.id.re_et_correo)
        etContrasena = findViewById(R.id.re_et_contrasena)
        etReContrasena = findViewById(R.id.re_et_re_contrasena)
        cbTerminos = findViewById(R.id.re_cb_terminos)
        btnRegistrarse = findViewById(R.id.re_btn_registrarse)
        tvIrLogin = findViewById(R.id.re_tv_ir_login)

        // Listener de Botón de registro
        btnRegistrarse.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val reContrasena = etReContrasena.text.toString().trim()
            cbTerminos.isChecked

            //validaciones
            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || reContrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!cbTerminos.isChecked){
                Toast.makeText(this, "Por favor, acepte los términos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (contrasena != reContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(contrasena.length < 8){
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Registro Supabase
            lifecycleScope.launch {
                try {
                    //Paso 1: Registrar corre y contraseña en AuthUser de Supabase
                    SupabaseClient.client.auth.signUpWith(Email){
                        email = correo
                        password = contrasena
                    }
                    //Paso2: UUID y pasos adicionales
                    val userId = SupabaseClient.client.auth.currentUserOrNull()?.id?:""
                    UsuarioRepository.insertarUsuario(
                        id = userId,
                        nombres = nombres,
                        apellidos = apellidos,
                        correo = correo
                    )

                    //Paso 3: Registro exitoso
                    runOnUiThread {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }


                }catch (e: Exception){
                    runOnUiThread {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error en el registro: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

                tvIrLogin.setOnClickListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
        }
    }
}