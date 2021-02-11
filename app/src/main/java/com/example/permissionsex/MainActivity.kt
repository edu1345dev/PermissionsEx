package com.example.permissionsex

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        val btWriteExternal = findViewById<Button>(R.id.bt_write_external)
        val btCamera = findViewById<Button>(R.id.bt_camera)
        val btMaps = findViewById<Button>(R.id.bt_maps)
        val btAll = findViewById<Button>(R.id.bt_all)

        btWriteExternal.setOnClickListener {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            requestPermission(permission)
        }

        btCamera.setOnClickListener {
            val permission = Manifest.permission.CAMERA
            requestPermission(permission)
        }

        btMaps.setOnClickListener {
            val permission = Manifest.permission.ACCESS_FINE_LOCATION
            requestPermission(permission)
        }

        btAll.setOnClickListener {
            requestAllPermission()
        }
    }

    private fun requestPermission(permission: String) {
        val isGranted = ContextCompat.checkSelfPermission(this, permission)

        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(permission),
                    1)
        } else {
            Toast.makeText(this, "permission already granted", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestAllPermission() {
        val permission1 = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permission2 = Manifest.permission.CAMERA
        val permission3 = Manifest.permission.ACCESS_FINE_LOCATION

        val permissions = listOf(permission1, permission2, permission3)
        val permissionsNotGranted = mutableListOf<String>()

        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(it)
            }
        }

        if (permissionsNotGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsNotGranted.toTypedArray(),
                    2)
        } else {
            Toast.makeText(this, "all permission granted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (permissions.isEmpty()) return

        when (requestCode) {
            1 -> {
                if (shouldAskAgain(permissions[0])) {
                    requestPermission(permissions[0])
                }
            }
            else -> {
                Toast.makeText(this, "${permissions[0]} permission granted - request code $requestCode", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun shouldAskAgain(permission: String) =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(permission)
}