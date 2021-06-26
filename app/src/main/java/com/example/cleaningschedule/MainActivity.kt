package com.example.cleaningschedule

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)

        val topLevelDestinations: HashSet<Int> = HashSet(listOf(R.id.toDoList, R.id.allTasks, R.id.settings))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setOpenableLayout(drawerLayout)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        nav_view.menu.getItem(0).isChecked = true

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val rooms: Set<String> = setOf(
            getString(R.string.living_room),
            getString(R.string.kitchen),
            getString(R.string.dining_room),
            getString(R.string.bathroom),
            getString(R.string.bedroom),
            getString(R.string.hallway),
            getString(R.string.office),
            getString(R.string.pantry))
        if (sharedPreferences.getStringSet("rooms", null) == null) {
            with (sharedPreferences.edit()) {
                putStringSet("rooms", rooms)
                commit()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun navigateTo(menuItem: MenuItem) {
        val fragmentId = when (menuItem.itemId) {
            R.id.nav_to_do_list_fragment -> R.id.toDoList
            R.id.nav_all_tasks_fragment -> R.id.allTasks
            else -> R.id.settings
        }

        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(fragmentId)
        drawerLayout.closeDrawers()
    }
}
