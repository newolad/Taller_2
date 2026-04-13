package com.example.clase_dos.activities.main.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.clase_dos.R

class PerfilEdicionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil_edicion, container, false)

        val btnGuardar = view.findViewById<Button>(R.id.pe_btn_guardar)
        btnGuardar.setOnClickListener {
            // Aquí irá la lógica para guardar los cambios en el perfil
            // Por ahora, podemos volver al fragmento de perfil anterior
            parentFragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = PerfilEdicionFragment()
    }
}