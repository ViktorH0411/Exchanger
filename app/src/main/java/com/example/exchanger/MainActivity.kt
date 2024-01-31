package com.example.exchanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import com.example.exchanger.api.MainApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var calculateButton: Button
    private lateinit var moneyAmount: EditText
    private lateinit var currency: TextView
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculateButton = findViewById(R.id.calculateButton)
        moneyAmount = findViewById(R.id.moneyAmount)
        currency = findViewById(R.id.currency)
        result = findViewById(R.id.result)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val mainApi = retrofit.create(MainApi::class.java)

        val popupMenu = PopupMenu(this, currency)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.USD -> {
                    currency.text = "USD"
                }
                R.id.EUR -> {
                    currency.text = "EUR"
                }
                R.id.PLN -> {
                    currency.text = "PLN"
                }
                R.id.RUB -> {
                    currency.text = "RUB"
                }
            }
            false
        }

        calculateButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val selectedCurrency = currency.text.toString()
                val resValue = mainApi.getCurrencyRate("UAH")

                runOnUiThread {
                    val moneyAmountText = moneyAmount.text.toString()
                    if (moneyAmountText.isNotEmpty()) {
                        val amount = moneyAmountText.toFloat()
                        val conversionRate = when (selectedCurrency) {
                            "USD" -> resValue.conversion_rates.USD
                            "EUR" -> resValue.conversion_rates.EUR
                            "PLN" -> resValue.conversion_rates.PLN
                            "RUB" -> resValue.conversion_rates.RUB
                            else -> 1.0  // Default to 1.0 if currency is not recognized
                        }

                        val resultValue = amount * conversionRate
                        val roundedResult = round(resultValue * 100.0) / 100.0
                        result.text = roundedResult.toString()
                    } else {
                        result.text = "Enter a valid amount"
                    }
                }
            }
        }

        currency.setOnClickListener {
            popupMenu.show()
        }
    }
}
