package com.example.exchanger

import android.annotation.SuppressLint
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
    private lateinit var mainCurrency: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculateButton = findViewById(R.id.calculateButton)
        moneyAmount = findViewById(R.id.moneyAmount)
        currency = findViewById(R.id.currency)
        result = findViewById(R.id.result)
        mainCurrency= findViewById(R.id.mainCurrency)
        var base_code: String = "UAH"

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
                    currency.text = "\uD83C\uDDFA\uD83C\uDDF8 USD"
                }
                R.id.EUR -> {
                    currency.text = "\uD83C\uDDEA\uD83C\uDDFA EUR"
                }
                R.id.PLN -> {
                    currency.text = "\uD83C\uDDF5\uD83C\uDDF1 PLN"
                }
                R.id.UAH -> {
                    currency.text = "\uD83C\uDDFA\uD83C\uDDE6 UAH"
                }
            }
            false
        }

        val popupMenu1 = PopupMenu(this, mainCurrency)
        popupMenu1.inflate(R.menu.popup_menu)
        popupMenu1.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.USD -> {
                    mainCurrency.text = "\uD83C\uDDFA\uD83C\uDDF8 USD"
                    base_code = "USD"
                }
                R.id.EUR -> {
                    mainCurrency.text = "\uD83C\uDDEA\uD83C\uDDFA EUR"
                    base_code = "EUR"
                }
                R.id.PLN -> {
                    mainCurrency.text = "\uD83C\uDDF5\uD83C\uDDF1 PLN"
                    base_code = "PLN"
                }
                R.id.UAH -> {
                    mainCurrency.text = "\uD83C\uDDFA\uD83C\uDDE6 UAH"
                    base_code = "UAH"
                }
            }
            false
        }

        calculateButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val selectedCurrency = currency.text.toString()
                val resValue = mainApi.getCurrencyRate(base_code)

                runOnUiThread {
                    val moneyAmountText = moneyAmount.text.toString()
                    if (moneyAmountText.isNotEmpty()) {
                        val amount = moneyAmountText.toFloat()
                        val conversionRate = when (selectedCurrency) {
                            "\uD83C\uDDFA\uD83C\uDDF8 USD" -> resValue.conversion_rates.USD
                            "\uD83C\uDDEA\uD83C\uDDFA EUR" -> resValue.conversion_rates.EUR
                            "\uD83C\uDDF5\uD83C\uDDF1 PLN" -> resValue.conversion_rates.PLN
                            "\uD83C\uDDFA\uD83C\uDDE6 UAH" -> resValue.conversion_rates.UAH
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

        mainCurrency.setOnClickListener {
            popupMenu1.show()
        }
    }
}
