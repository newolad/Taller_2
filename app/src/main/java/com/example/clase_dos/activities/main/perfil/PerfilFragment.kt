package com.example.clase_dos.activities.main.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clase_dos.R

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Corregido: Referencia al archivo correcto fragment_perfil
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PerfilFragment()
    }
}