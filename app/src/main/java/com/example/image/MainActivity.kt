package com.example.image

import android.app.ActionBar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.image.ui.theme.ImageTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mytoolbar1 = findViewById<Toolbar>(R.id.mytoolbar)
        setSupportActionBar(mytoolbar1)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.camera ->{
                Toast.makeText(this, "You Clicked Camera", Toast.LENGTH_LONG).show()
                return true
            }R.id.favorite ->{
                Toast.makeText(this, "You Clicked favorite", Toast.LENGTH_LONG).show()
                return true
            }R.id.share -> {
                Toast.makeText(this, "You clicked Share", Toast.LENGTH_LONG).show()
                return true
            }R.id.settings ->{
                Toast.makeText(this, "You Clicked settings", Toast.LENGTH_LONG).show()
                return true
            }

            else -> {super.onOptionsItemSelected(item)}
        }
    }
}
