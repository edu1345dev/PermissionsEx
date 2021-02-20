package com.example.permissionsex

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsHelper(
    private val activity: Activity,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {
    fun requestPermission(permission: String) {
        val isGranted = ContextCompat.checkSelfPermission(activity, permission)

        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                1
            )
        } else {
            Log.d(
                PermissionsHelper::class.java.toString(),
                "permission already granted: $permission"
            )
        }
    }

    fun requestAllPermission(permissions: List<String>) {
        val permissionsNotGranted = mutableListOf<String>()

        permissions.forEach {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNotGranted.add(it)
            }
        }

        if (permissionsNotGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsNotGranted.toTypedArray(),
                2
            )
        } else {
            Log.d(PermissionsHelper::class.java.toString(), "all permission granted")
        }
    }

    fun handleRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (permissions.isEmpty()) return

        when (requestCode) {
            1 -> {
                if (shouldAskRationale(permissions[0])) {
                    AlertDialog.Builder(activity)
                        .setTitle("Alerta de permissão necessária")
                        .setMessage("Precisamos da permissao ${permissions[0]} pra funcionar corretamente.")
                        .setPositiveButton("Tudo bem") { dialog, _ ->
                            requestPermission(permissions[0])
                        }
                        .setNegativeButton("Não quero") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                } else {
                    sharedPreferencesHelper.putShowAlertOnStartUp(true)
                }
            }
            else -> {

            }
        }
    }

    private fun shouldAskRationale(permission: String) =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                activity.shouldShowRequestPermissionRationale(permission)
}