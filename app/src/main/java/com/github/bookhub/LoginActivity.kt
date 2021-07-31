package com.github.bookhub

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        val loginMap = mutableMapOf<String, String>()
        loginMap["tony"] = "alpha"
        sharedPreferences = getSharedPreferences(
            R.string.sharedPreferencesLogin.toString(),
            AppCompatActivity.MODE_PRIVATE
        )
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(intent)
            finish()
        }
        loginButton = findViewById(R.id.btnLogin)
        loginEmail = findViewById(R.id.etEmail)
        loginPassword = findViewById(R.id.etPassword)
        loginButton.setOnClickListener {
            val tmpEmail: String = loginEmail.text.toString()
            if (loginMap.containsKey(tmpEmail) &&
                loginPassword.text.toString() == loginMap[tmpEmail]
            ) {
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                sharedPreferences.edit().putString("name", tmpEmail).apply()

                intent.putExtra("name", tmpEmail)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}