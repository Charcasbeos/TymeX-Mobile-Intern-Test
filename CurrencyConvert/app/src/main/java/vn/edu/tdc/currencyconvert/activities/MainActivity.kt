package vn.edu.tdc.currencyconvert.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.edu.tdc.currencyconvert.R
import vn.edu.tdc.currencyconvert.models.RateData
import vn.edu.tdc.currencyconvert.models.SymbolData
import vn.edu.tdc.currencyconvert.models.CurrencyModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var keyFromSpinner: Spinner // Spinner for selecting the currency to convert from
    private lateinit var keyToSpinner: Spinner // Spinner for selecting the currency to convert to
    private lateinit var amountInput: EditText // Input field for the amount to convert
    private lateinit var amountOutput: TextView // Output field for displaying the converted amount
    private lateinit var timestamp: TextView // TextView to display the timestamp of the last conversion

    private var keyFrom: String = "" // Holds the key of the selected currency to convert from
    private var keyTo: String = "" // Holds the key of the selected currency to convert to
    private val currencyModel = CurrencyModel() // Instance of CurrencyModel to handle data operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        // Initialize views
        keyFromSpinner = findViewById(R.id.keyFrom)
        keyToSpinner = findViewById(R.id.keyTo)
        amountInput = findViewById(R.id.edt_1)
        timestamp = findViewById(R.id.timestamp)
        amountOutput = findViewById(R.id.result)
        amountOutput.isFocusable = false // Disable focus for output field
        amountOutput.isCursorVisible = false // Disable cursor visibility for output field

        // Load the list of currency symbols
        loadSymbols()

        // Add a TextWatcher to the input field to trigger conversion when text changes
        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currencyConvert() // Trigger conversion on text change
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Load currency symbols asynchronously
    private fun loadSymbols() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val symbolDataList = currencyModel.getSymbols() // Fetch symbols from the model
                withContext(Dispatchers.Main) {
                    setupSpinner(symbolDataList) // Setup spinners with fetched symbols
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching symbols: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, e.message ?: "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Setup the spinners with currency symbols
    private fun setupSpinner(symbolDataList: List<SymbolData>) {
        // Create an adapter for the spinner
        val symbolDataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            symbolDataList.map { it.name } // Map symbol data to display names
        )
        symbolDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        keyFromSpinner.adapter = symbolDataAdapter // Set adapter for the 'from' spinner
        keyToSpinner.adapter = symbolDataAdapter // Set adapter for the 'to' spinner

        // Listener for the 'from' spinner
        keyFromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                keyFrom = symbolDataList[position].key // Get the selected currency key
                currencyConvert() // Convert currency when selected
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Listener for the 'to' spinner
        keyToSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                keyTo = symbolDataList[position].key // Get the selected currency key
                currencyConvert() // Convert currency when selected
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
    private fun currencyConvert() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val (rateList, baseKey) = currencyModel.getRates() // Fetch current exchange rates
                val input = amountInput.text.toString().toDoubleOrNull() // Get input amount
                if (input == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    }
                    amountOutput.text = "0.0"
                    return@launch
                }
                val rateTo = rateList.find { it.key == keyTo }?.value // Get conversion rate to selected currency
                val rateFrom = rateList.find { it.key == keyFrom }?.value // Get conversion rate from selected currency

                if (rateTo == null || rateFrom == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Exchange rate not found for the selected currencies.", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Calculate the converted amount
                val fromBase = input / rateFrom.toDouble()
                val baseTo = fromBase * rateTo.toDouble()
                val formattedResult = String.format("%.2f", baseTo) // Format the result

                withContext(Dispatchers.Main) {
                    amountOutput.text = formattedResult // Display converted amount
                    val dateTime = currencyModel.timeCurrent.toLong().let {
                        Instant.ofEpochSecond(it)
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    }
                    timestamp.text = dateTime.toString() // Display timestamp
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during currency conversion: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "An error occurred during the conversion process.", Toast.LENGTH_SHORT).show()
                    amountOutput.text = "0.0" // Reset output on error
                }
            }
        }
    }

//
//    // Convert the currency based on the selected values
//    @SuppressLint("DefaultLocale")
//    private fun currencyConvert() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val (rateList, baseKey) = currencyModel.getRates() // Fetch the current exchange rates
//                val input = amountInput.text.toString().toDoubleOrNull() // Get the input amount
//                if (input == null) {
//                    withContext(Dispatchers.Main) {
//                        // Show a toast message if the input is not valid
//                        Toast.makeText(this@MainActivity, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
//                    }
//                    amountOutput.text = "0.0"
//                    return@launch
//                }
//                val rateTo = rateList.find { it.key == keyTo }?.value // Get the conversion rate to the selected currency
//                val rateFrom = rateList.find { it.key == keyFrom }?.value // Get the conversion rate from the selected currency
//                // Check if the rates are valid
//                if (rateTo == null || rateFrom == null) {
//                    Toast.makeText(this@MainActivity, "Exchange rate not found for the selected currencies.", Toast.LENGTH_SHORT).show()
//                    return@launch
//                }
//
//                // Calculate the amount converted from the base currency
//                val fromBase = input / rateFrom.toDouble()
//                // Calculate the amount converted to the target currency
//                val baseTo = fromBase * rateTo.toDouble()
//
//                val formattedResult = String.format("%.2f", baseTo) // Format the result to two decimal places
//
//                // Update the UI on the main thread
//                withContext(Dispatchers.Main) {
//                    amountOutput.text = formattedResult // Display the converted amount
//                    // Format and display the timestamp of the conversion
//                    val dateTime = currencyModel.timeCurrent.toLong().let {
//                        Instant.ofEpochSecond(it)
//                            .atZone(ZoneId.systemDefault())
//                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                    }
//                    timestamp.text = dateTime.toString() // Display the formatted timestamp
//                }
//            } catch (e: Exception) {
//                Log.e("MainActivity", "Error converting currency: ${e.message}") // Log any errors
//                Toast.makeText(this@MainActivity, "An error occurred during the conversion process.", Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    }
}