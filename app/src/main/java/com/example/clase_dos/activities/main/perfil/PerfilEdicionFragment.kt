package com.example.clase_dos.activities.main.perfil

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.clase_dos.R
import com.example.clase_dos.activities.SupabaseClient
import com.example.clase_dos.data.UsuarioRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import java.io.File

class PerfilEdicionFragment : Fragment() {

    private var uriFotoSeleccionada: Uri? = null
    private lateinit var ivEditarFoto: ImageView
    private lateinit var archivoFotoTemp: File

    // Lanzador para solicitar el permiso de cámara
    private val lanzadorPermisoCamara =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
            if (concedido) {
                abrirCamara()
            } else {
                Toast.makeText(requireContext(), "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show()
            }
        }

    // Lanzador para la cámara
    private val lanzadorCamara =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
            if (exito) {
                uriFotoSeleccionada = Uri.fromFile(archivoFotoTemp)
                ivEditarFoto.load(uriFotoSeleccionada) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    // Lanzador para la galería
    private val lanzadorGaleria =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uriFotoSeleccionada = uri
                ivEditarFoto.load(uri) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_perfil_edicion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculación con los IDs pe_ del layout rosa
        ivEditarFoto       = view.findViewById(R.id.pe_iv_foto)
        val ivCamaraIcon   = view.findViewById<ImageView>(R.id.pe_iv_camara)
        val etNombres      = view.findViewById<EditText>(R.id.pe_et_nombres)
        val etApellidos    = view.findViewById<EditText>(R.id.pe_et_apellidos)
        val etCorreo       = view.findViewById<EditText>(R.id.pe_et_correo)
        val etContrasena   = view.findViewById<EditText>(R.id.pe_et_contrasena)
        val etReContrasena = view.findViewById<EditText>(R.id.pe_et_re_contrasena)
        val btnGuardar     = view.findViewById<Button>(R.id.pe_btn_guardar)

        // Cargar datos actuales
        lifecycleScope.launch {
            try {
                val usuario = UsuarioRepository.obtenerUsuarioActual()
                if (usuario != null) {
                    etNombres.setText(usuario.nombres)
                    etApellidos.setText(usuario.apellidos)
                    etCorreo.setText(usuario.correo ?: "")

                    if (!usuario.fotoUrl.isNullOrEmpty()) {
                        ivEditarFoto.load(usuario.fotoUrl) {
                            transformations(CircleCropTransformation())
                            placeholder(R.mipmap.ic_launcher_round)
                            error(R.mipmap.ic_launcher_round)
                        }
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("PerfilEdicion", "Error al cargar datos: ${e.message}")
            }
        }

        ivCamaraIcon.setOnClickListener { mostrarOpcionesFoto() }

        btnGuardar.setOnClickListener {
            guardarCambios(etNombres, etApellidos, etCorreo, etContrasena, etReContrasena)
        }
    }

    private fun mostrarOpcionesFoto() {
        val opciones = arrayOf("Tomar foto", "Elegir de galería")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Foto de perfil")
            .setItems(opciones) { _, cual ->
                when (cual) {
                    0 -> verificarPermisoCamara()
                    1 -> lanzadorGaleria.launch("image/*")
                }
            }
            .show()
    }

    private fun verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
            == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            lanzadorPermisoCamara.launch(Manifest.permission.CAMERA)
        }
    }

    private fun abrirCamara() {
        val carpeta = File(requireContext().cacheDir, "images")
        if (!carpeta.exists()) carpeta.mkdirs()
        archivoFotoTemp = File(carpeta, "foto_perfil_temp.jpg")

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            archivoFotoTemp
        )
        lanzadorCamara.launch(uri)
    }

    private fun guardarCambios(
        etNombres: EditText, etApellidos: EditText,
        etCorreo: EditText, etContrasena: EditText, etReContrasena: EditText
    ) {
        val nombres    = etNombres.text.toString().trim()
        val apellidos  = etApellidos.text.toString().trim()
        val correo     = etCorreo.text.toString().trim()
        val contrasena = etContrasena.text.toString()
        val reContrasena = etReContrasena.text.toString()

        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty()) {
            Toast.makeText(requireContext(), "Nombres, apellidos y correo son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena.isNotEmpty()) {
            if (contrasena.length < 6) {
                Toast.makeText(requireContext(), "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                return
            }
            if (contrasena != reContrasena) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return
            }
        }

        lifecycleScope.launch {
            try {
                var fotoUrl: String? = null
                if (uriFotoSeleccionada != null) {
                    fotoUrl = UsuarioRepository.subirFotoPerfil(requireContext(), uriFotoSeleccionada!!)
                }

                UsuarioRepository.actualizarPerfil(nombres, apellidos, correo, fotoUrl)

                if (contrasena.isNotEmpty()) {
                    SupabaseClient.client.auth.updateUser { password = contrasena }
                }

                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
