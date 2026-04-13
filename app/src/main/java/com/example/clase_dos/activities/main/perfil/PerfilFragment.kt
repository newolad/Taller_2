package com.example.clase_dos.activities.main.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.clase_dos.R

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        val btnEditar = view.findViewById<Button>(R.id.fp_btn_editar)
        btnEditar.setOnClickListener {
            // Navegamos al fragmento de edición de perfil
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PerfilEdicionFragment())
                .addToBackStack(null) // Esto permite volver atrás con el botón del sistema
                .commit()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = PerfilFragment()
    }
}