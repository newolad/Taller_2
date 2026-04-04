package com.example.clase_dos.activities.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.clase_dos.R
import com.example.clase_dos.activities.main.admin.AdminFragment
import com.example.clase_dos.activities.main.admin.UsuariosFragment
import com.example.clase_dos.activities.main.perfil.PerfilFragment
import com.example.clase_dos.activities.main.productos.CarritoFragment
import com.example.clase_dos.activities.main.productos.CatalogoFragment
import com.example.clase_dos.activities.main.productos.FavoritosFragment
import com.example.clase_dos.activities.main.productos.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_main)
        val bottom_Navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val nav_View = findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)

        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        toogle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.rosa)

        cargarFragment(HomeFragment())
        bottom_Navigation.selectedItemId = R.id.nav_home

        bottom_Navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> cargarFragment(HomeFragment())
                R.id.nav_catalogo -> cargarFragment(CatalogoFragment())
                R.id.nav_carrito -> cargarFragment(CarritoFragment())
                R.id.nav_perfil -> cargarFragment(PerfilFragment())
            }
            true
        }

        nav_View.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_favoritos -> cargarFragment(FavoritosFragment())
                R.id.nav_admin -> cargarFragment(AdminFragment())
                R.id.nav_usuarios -> cargarFragment(UsuariosFragment())
            }
            drawerLayout.closeDrawers()
            true
        }
    }

        private fun cargarFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragment)
                commit()

            }

        }


}

