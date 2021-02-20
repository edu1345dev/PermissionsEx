package com.example.permissionsex

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var permissionsHelper: PermissionsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        permissionsHelper = PermissionsHelper(this, sharedPreferencesHelper)

        initViews()
    }

    override fun onStart() {
        super.onStart()

        if (shouldAskUserToGrantPermissions()) {
            AlertDialog.Builder(this).setTitle("Alerta de uso de recursos do sistema")
                .setMessage("Va as configuraçoes para dar as permissoes")
                .setPositiveButton("Valeu") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    private fun shouldAskUserToGrantPermissions(): Boolean {
        return sharedPreferencesHelper.getShowAlertOnStartUp()
    }

    private fun initViews() {
        val btWriteExternal = findViewById<Button>(R.id.bt_write_external)
        val btCamera = findViewById<Button>(R.id.bt_camera)
        val btAll = findViewById<Button>(R.id.bt_all)

        btWriteExternal.setOnClickListener {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            requestPermission(permission)
        }

        btCamera.setOnClickListener {
            val permission = Manifest.permission.CAMERA
            requestPermission(permission)
        }

        btAll.setOnClickListener {
            requestAllPermission()
        }
    }

    private fun requestPermission(permission: String) {
        permissionsHelper.requestPermission(permission)
    }

    private fun requestAllPermission() {
        val permissions = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        permissionsHelper.requestAllPermission(permissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsHelper.handleRequestPermissionResult(requestCode, permissions, grantResults)
    }
}