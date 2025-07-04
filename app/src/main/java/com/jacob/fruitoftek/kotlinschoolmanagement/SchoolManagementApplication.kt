package com.jacob.fruitoftek.kotlinschoolmanagement

import android.app.Application
import com.jacob.fruitoftek.kotlinschoolmanagement.network.NetworkMonitor

class SchoolManagementApplication : Application() {
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate() {
        super.onCreate()
        networkMonitor = NetworkMonitor(this)
        networkMonitor.register()
    }

    override fun onTerminate() {
        super.onTerminate()
        networkMonitor.unregister()
    }
}