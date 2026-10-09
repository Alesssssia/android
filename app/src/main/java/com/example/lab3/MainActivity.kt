package com.example.lab3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        var userLogin: EditText = findViewById(R.id.user_login)
        var userEmail: EditText = findViewById(R.id.user_email)
        var userPhone : EditText = findViewById(R.id.user_tel)
        var userPass: EditText = findViewById(R.id.user_pass)
        val button: Button = findViewById(R.id.button_reg)

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val phone = userPhone.text.toString().trim()
            val pass = userPass.text.toString().trim()

            // 1. Проверка: все поля заполнены
            if (login.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 2. Проверка: email корректен (формат + разрешённый домен)
            if (!isValidEmail(email)) {
                Toast.makeText(
                    this,
                    "Введите корректный email (gmail.com, mail.ru, yandex.ru и т.п.)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // 3. Проверка: телефон только цифры (и, опционально, + в начале)
            if (!phone.matches(Regex("^\\+?\\d{7,15}$"))) {
                Toast.makeText(this, "Телефон должен содержать только цифры", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Всё ок — сохраняем в БД
            val user = User(login, email, phone, pass)
            val db = DbHelper(this, null)
            db.addUser(user)

            Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()

            userLogin.text.clear()
            userEmail.text.clear()
            userPhone.text.clear()
            userPass.text.clear()
        }
    }

    // Проверка email: формат + список разрешённых доменов
    private fun isValidEmail(email: String): Boolean {
        // 1. Общая проверка формата: что-то@что-то.домен
        val pattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (!pattern.matches(email)) return false

        // 2. Проверка допустимых доменов
        val allowedDomains = listOf(
            "gmail.com",
            "mail.ru",
            "yandex.ru",
            "ya.ru",
            "inbox.ru",
            "list.ru",
            "bk.ru",
            "internet.ru",
            "outlook.com",
            "hotmail.com"
        )

        // Берём часть после @ и приводим к нижнему регистру
        val domain = email.substringAfter("@").lowercase()
        return domain in allowedDomains
    }
}
