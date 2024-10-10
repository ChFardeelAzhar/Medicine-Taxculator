package com.example.ratecalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratecalculator.ui.theme.RateCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RateCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedicineCalculator()
                }

            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineCalculator() {

    var tradePrice by remember { mutableStateOf("") }
    var retailPrice by remember { mutableStateOf("") }
    var totalSaleTax by remember { mutableStateOf("") }
    var percentage by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    val context = LocalContext.current

    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    // Function to calculate percentage
    fun calculatePercentage() {
        val trade = tradePrice.ifEmpty { "0" }.toFloat()
        val retail = retailPrice.ifEmpty { "0" }.toFloat()
        val currentPercentage = percentage.ifEmpty { "0" }.toFloat()
        val currentTotalSaleTax = totalSaleTax.ifEmpty { "0" }.toFloat()
        val qty = quantity.ifEmpty { "1" }.toFloat()

        val saleTaxPerItem = currentTotalSaleTax / qty

        if (trade != null && retail != null && trade != 0f && retail != 0f) {
            try {
                val calculatedPercentage = (100 - (((trade + saleTaxPerItem) / retail) * 100))
                percentage = String.format("%.2f", calculatedPercentage)
            } catch (e: Exception) {
                Toast.makeText(context, "${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }

        } else if (currentPercentage != null && retail != null && currentPercentage != 0f && retail != 0f) {

            try {
                val calculatedTradePrice =
                    ((100 - currentPercentage) * retail / 100) - saleTaxPerItem
                tradePrice = String.format("%.2f", calculatedTradePrice)
            } catch (e: Exception) {
                Toast.makeText(context, "${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }

        } else if (currentPercentage != null && trade != null && currentPercentage != 0f && trade != 0f) {

            try {
                val calculatedRetailPrice =
                    ((trade + saleTaxPerItem) / (100 - currentPercentage)) * 100
                retailPrice = String.format("%.2f", calculatedRetailPrice)
            } catch (e: Exception) {
                Toast.makeText(context, "${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }

        }

    }

    // Function to calculate retail price based on trade and percentage
    fun resetValues() {
        tradePrice = ""
        retailPrice = ""
        totalSaleTax = ""
        percentage = ""
        quantity = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Rate Calculator",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(top = 30.dp)
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    Divider(modifier = Modifier.fillMaxWidth().padding(end = 16.dp), thickness = 1.dp)


                }

            }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(20.dp)
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = tradePrice,
                onValueChange = {
                    tradePrice = it
                },
                enabled = if (retailPrice.isEmpty() && tradePrice.isEmpty() && percentage.isEmpty()) true
                else if (retailPrice.isNotEmpty() && tradePrice.isNotEmpty() && percentage.isNotEmpty()) true
                else if (retailPrice.isNotEmpty() && percentage.isNotEmpty()) false
                else true,
                label = { Text("T.P") },
                trailingIcon = {
                    Icon(
                        Icons.Default.Money,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )

            OutlinedTextField(
                value = retailPrice,
                onValueChange = {
                    retailPrice = it
                },
                label = { Text("R.P") },

                enabled = if (retailPrice.isEmpty() && tradePrice.isEmpty() && percentage.isEmpty()) true
                else if (retailPrice.isNotEmpty() && tradePrice.isNotEmpty() && percentage.isNotEmpty()) true
                else if (tradePrice.isNotEmpty() && percentage.isNotEmpty()) false
                else true,
                trailingIcon = {
                    Icon(
                        Icons.Outlined.ShoppingCart, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )

            OutlinedTextField(
                value = percentage,
                onValueChange = {
                    percentage = it
                },
                label = { Text("% Percentage") },
                enabled = if (retailPrice.isEmpty() && tradePrice.isEmpty() && percentage.isEmpty()) true
                else if (retailPrice.isNotEmpty() && tradePrice.isNotEmpty() && percentage.isNotEmpty()) true
                else if (retailPrice.isNotEmpty() && tradePrice.isNotEmpty()) false
                else true,
                trailingIcon = {
                    Icon(
                        Icons.Default.Percent, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )


            OutlinedTextField(
                value = totalSaleTax,
                onValueChange = { totalSaleTax = it },
                label = { Text("Sale.Tax") },
                trailingIcon = {
                    Icon(
                        Icons.Default.AttachMoney, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Qty") },
                trailingIcon = {
                    Icon(
                        Icons.Default.ProductionQuantityLimits, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.size(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        calculatePercentage()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.weight(1f)

                ) {
                    Text(text = "Calculate")
                }

                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    onClick = {
                        resetValues()
                        focusRequester.requestFocus()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Reset")
                }

            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Text(
                    text = "By Your Developer : Frdl",
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

        }

    }

}

@Preview
@Composable
fun MedicineCalculatorPreview() {
    MedicineCalculator()
}
