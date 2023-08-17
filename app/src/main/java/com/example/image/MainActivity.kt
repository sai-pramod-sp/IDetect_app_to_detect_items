package com.example.image

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.image.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager:  FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    val permissionRequest: MutableList<String> = ArrayList()

    private var isreadmediaaudio = false
    private var isreadmediavideo = false
    private var isreadmediaimages = false
    private var iscamera = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mytoolbar1 = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(binding.toolbar)

        var toggle = ActionBarDrawerToggle(this, binding.drawerlayout, binding.toolbar, R.string.nav_open, R.string.nav_close)
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)
        binding.bottomNavigation.background = null

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.home -> openfragment(HomeFragment())
                R.id.favorite1 -> openfragment(FavFragment())
                R.id.history -> openfragment(HistoryFragment())
                R.id.settings -> openfragment(SettingsFragment())
            }
            true
        }

        fragmentManager = supportFragmentManager
        openfragment(HomeFragment())

        binding.add.setOnClickListener{
            Toast.makeText(this, "Dialogue box opened", Toast.LENGTH_SHORT).show()
        }

        grantruntimepermission()


    }

    private fun grantruntimepermission(){

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission ->

            isreadmediaaudio = permission[Manifest.permission.READ_MEDIA_AUDIO] ?: isreadmediaaudio
            isreadmediavideo = permission[Manifest.permission.READ_MEDIA_VIDEO] ?: isreadmediavideo
            isreadmediaimages = permission[Manifest.permission.READ_MEDIA_IMAGES] ?: isreadmediaimages
            iscamera = permission[Manifest.permission.CAMERA] ?: iscamera

        }

        isreadmediaaudio = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        isreadmediavideo = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
        isreadmediaimages = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        iscamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if(!iscamera){
            permissionRequest.add(Manifest.permission.CAMERA)
        }
        if(!isreadmediaaudio){
            permissionRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
        }
        if(!isreadmediavideo){
            permissionRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
        }
        if(!isreadmediaimages){
            permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }


    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.video_stream -> openfragment(VideoFragment())
            R.id.image_search -> openfragment(ImageFragment())
            R.id.video_cam -> openfragment(LiveVideoFragment())
            R.id.shopping_cart -> openfragment(CartListFragment())
            R.id.home -> openfragment(HomeFragment())
            R.id.favorite1 -> openfragment(FavFragment())
            R.id.history -> openfragment(HistoryFragment())
            R.id.settings -> openfragment(SettingsFragment())
        }
        binding.drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(binding.drawerlayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerlayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    private fun openfragment(fragment: Fragment){
        val fragmenttransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmenttransaction.replace(R.id.fragment_container, fragment)
        fragmenttransaction.commit()
    }

}
