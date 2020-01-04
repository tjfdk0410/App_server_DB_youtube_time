package com.example.madcamp_2nd

import ai.fritz.core.Fritz
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUIRED_PERMISSIONS = arrayOf("android.permission.CAMERA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        setSupportActionBar(toolbar)
        Fritz.configure(this, "417e5b76c3d64043ae4ed82820985f08");
        // set adapter for view pager
        val adapter = MainAdapter(supportFragmentManager)
        view_pager.adapter = adapter

        // sync view pager with tabs
        tab.setupWithViewPager(view_pager)


        setPermissions()

    }

    /**
     * setPermissions()
     *      setting permissions on contacts
     */
    private fun setPermissions() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // manually defined but don't know
            val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                // description here if needed.
            } else {
                // No explanation needed, we can request the permission.
                // TODO
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        } else {
            // Permission has already been granted
        }

        if (allPermissionsGranted()) {

        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
//                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {

        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}

/*
class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, ContactFragment()).commit()
        // set bottom navigation bar
        setBottomNavBar()
        // 기능
        setPermissions()
    }

    /**
     *  setBottomNavBar()
     *      configure views for navigation bar
     */
    private fun setBottomNavBar() {
        toolbar = supportActionBar!!
        toolbar.title = "Contacts"
        // listen selection
        bottom_navigation.setOnNavigationItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.navigation_contacts -> {
                    val fragment = ContactFragment()
                    toolbar.title = "Contacts"
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                }
                R.id.navigation_gallery -> {
                    val fragment = GalleryFragment()
                    toolbar.title = "Gallery"
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                }
                R.id.navigation_custom -> {
                    val fragment = CustomFragment()
                    toolbar.title = "Custom"
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                }
            }
            false
        }
    }
}
*/