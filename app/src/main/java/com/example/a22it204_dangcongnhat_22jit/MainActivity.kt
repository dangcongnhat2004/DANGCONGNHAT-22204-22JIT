package com.example.a22it204_dangcongnhat_22jit
import ManageFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.a22it204_dangcongnhat_22jit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavigation: BottomNavigationView = binding.bottomNavigation

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    replaceFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_manage -> {
                    replaceFragment(ManageFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_caculate -> {
                    replaceFragment(CalculateFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_settings -> {
                    replaceFragment(settingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        // Set default fragment when activity is created
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}
