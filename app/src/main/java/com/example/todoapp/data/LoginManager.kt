package com.example.todoapp.data

import android.content.Context
import android.content.SharedPreferences

class LoginManager(context: Context) {
    private val spf: SharedPreferences =
        context.getSharedPreferences("login_manager", Context.MODE_PRIVATE)

    fun loginInfo(account: String, password: String, remember: Boolean, autoLogin: Boolean) {
        spf.edit().apply() {
            if (remember) {
                putString("account", account)
                putString("password", password)
            } else {
                remove("account")
                remove("password")
            }
            putBoolean("remember", remember)
            putBoolean("autoLogin", autoLogin)
            apply()
        }
    }
    fun isRemember(): Boolean = spf.getBoolean("remember", false)

    fun isAutoLogin(): Boolean = spf.getBoolean("autoLogin", false)

    fun getAccount(): String? = spf.getString("account", null)

    fun getPassword(): String? = spf.getString("password", null)
}