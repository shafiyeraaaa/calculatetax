package com.example.calculatetax
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculatetax.ui.theme.CalculateTaxTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculateTaxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaxLayout(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TaxLayout(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }
    var taxInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val taxPercent = taxInput.toDoubleOrNull() ?: 0.0
    val tax = calculateTax(amount, taxPercent, roundUp)
    Column (
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = stringResource(R.string.calculate_tax),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditTextNumber(
            label = R.string.bill_amount,
            placeholder = R.string.ex_amount,
            leadingIcon = R.drawable.baseline_account_balance_wallet_24,
            value = amountInput,
            onValueChange = { amountInput = it},
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        EditTextNumber(
            label = R.string.tax_percentage,
            placeholder = R.string.ex_amount,
            leadingIcon = R.drawable.ic_tax,
            value = taxInput,
            onValueChange =  { taxInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        RoundTax(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(R.string.tax_amount, tax),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditTextNumber(
    @StringRes label:Int,
    @StringRes placeholder: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value:String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        placeholder = { Text(stringResource(placeholder)) },
        leadingIcon = { Icon(
            painter = painterResource(id = leadingIcon),
            contentDescription = null)},
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

@Composable
fun RoundTax(roundUp: Boolean,
             onRoundUpChanged: (Boolean) -> Unit,
             modifier: Modifier = Modifier
) {
    Row (modifier = Modifier.fillMaxWidth().size(48.dp)) {
        Text(text = stringResource(R.string.round_up_tax))
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
        )
    }
}
private fun calculateTax(
    amount: Double,
    taxPercent:Double = 10.0,
    roundUp: Boolean
):String {
    var tax = taxPercent / 100 * amount
    if (roundUp) {
        tax = kotlin.math.ceil(tax)
    }
    return NumberFormat.getCurrencyInstance().format(tax)
}

@Preview(showBackground = true)
@Composable
fun TaxLayoutPreview() {
    CalculateTaxTheme {
        TaxLayout()
    }
}