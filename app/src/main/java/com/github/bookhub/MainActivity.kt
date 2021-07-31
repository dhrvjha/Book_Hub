package com.github.bookhub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var toolbar: Toolbar
    private lateinit var dashboardMenuItem: MenuItem
    private var previousMenuItem: MenuItem? = null

    override fun onPostCreate(savedInstanceState: Bundle?) {
        navigationView.setNavigationItemSelectedListener {
            previousMenuItem?.isChecked = false
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.menuProfile -> {
                    openFragmentMainActivity(profile(), "Profile")
                }
                R.id.menuDashbord -> {
                    openFragmentMainActivity(dashboard(), "Dashboard")
                }
                R.id.menuLogout -> {
                    Toast.makeText(this@MainActivity, "Logging out", Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit().clear().apply()
                    intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        setUpActionBar()
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        super.onPostCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences =
            getSharedPreferences(R.string.sharedPreferencesLogin.toString(), MODE_PRIVATE)


        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationLayout)

        navigationView.setCheckedItem(R.id.menuDashbord)
        openFragmentMainActivity(dashboard(), "Dashboard")
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = sharedPreferences.getString("name", "Avenger")
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openFragmentMainActivity(Object: Fragment, title: String = "Book Hub") {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, Object)
            .commit()
        supportActionBar?.title = title
        drawerLayout.closeDrawers()
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frameLayout)) {
            !is dashboard -> openFragmentMainActivity(dashboard(), "Dashboard")
            else -> super.onBackPressed()
        }
    }
}
