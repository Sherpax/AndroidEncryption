package com.example.androidencryption

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androidencryption.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var txtPassword: TextView
    private lateinit var etPassword: EditText
    private lateinit var btnSavePassword: Button
    private lateinit var btnDecryptPassword: Button
    private val cryptoManager: CryptoManager = CryptoManager()
    private lateinit var secSharedPref: SecSharedPref

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        loadViewElements(binding)

        secSharedPref = SecSharedPref.getInstance(context = applicationContext)

        loadEvents()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadEvents() {
        btnSavePassword.setOnClickListener { encryptAndShowWithShared() }
        btnDecryptPassword.setOnClickListener { decryptAndShowWithShared() }

    }

    private fun encryptAndShowWithShared() {
        secSharedPref.saveString("password_25", etPassword.text.toString())
    }

    private fun decryptAndShowWithShared() {
        txtPassword.text = secSharedPref.getString("password_25", "no key")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun decryptAndShow() {
        val file = File(filesDir, "secret.txt")
        val messageDecrypted = cryptoManager.decrypt(
            inputStream = FileInputStream(file)
        ).decodeToString() as CharSequence
        txtPassword.text = messageDecrypted
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun encryptAndShow() {
        val bytes = etPassword.text.toString().toByteArray()
        val file = File(filesDir, "secret.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        val fos = FileOutputStream(file)

        val messageToDecrypt = cryptoManager.encrypt(bytes, fos).decodeToString() as CharSequence
        txtPassword.text = messageToDecrypt
    }

    private fun loadViewElements(binding: ActivityMainBinding) {
        txtPassword = binding.txtPassword
        etPassword = binding.etPassword
        btnSavePassword = binding.btnSavePassword
        btnDecryptPassword = binding.btnDecryptPassword
    }

}