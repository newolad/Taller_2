package com.example.clase_dos.activities.main.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.clase_dos.R
import com.example.clase_dos.data.UsuarioRepository
import kotlinx.coroutines.launch

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivFoto = view.findViewById<ImageView>(R.id.fp_iv_foto)
        val tvNombre = view.findViewById<TextView>(R.id.fp_tv_nombre)
        val tvCorreo = view.findViewById<TextView>(R.id.fp_tv_correo)
        val btnEditar = view.findViewById<Button>(R.id.fp_btn_editar)

        // Cargar datos actuales
        lifecycleScope.launch {
            try {
                val usuario = UsuarioRepository.obtenerUsuarioActual()
                if (usuario != null) {
                    tvNombre.text = "${usuario.nombres} ${usuario.apellidos}"
                    tvCorreo.text = usuario.correo

                    if (!usuario.fotoUrl.isNullOrEmpty()) {
                        ivFoto.load(usuario.fotoUrl) {
                            transformations(CircleCropTransformation())
                            placeholder(R.mipmap.ic_launcher_round)
                            error(R.mipmap.ic_launcher_round)
                        }
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("PerfilFragment", "Error al cargar datos: ${e.message}")
            }
        }

        btnEditar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PerfilEdicionFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PerfilFragment()
    }
}
