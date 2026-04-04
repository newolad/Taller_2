package com.example.clase_dos.activities.main.productos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clase_dos.R



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
        private val listaProductos = listOf(
            Product("Botas Trekking", 10.99, R.drawable.boots_trekking),
            Product("Gafas Fotocromaticas", 19.99, R.drawable.gafas_fotocromaticas),
            Product("Morral de Viaje", 29.99, R.drawable.morral_senderismo),
            Product("Carpa 4 Personas", 39.99, R.drawable.carpa_4_personas),
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_productos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ProductAdapter(listaProductos)
        return view

    }

}