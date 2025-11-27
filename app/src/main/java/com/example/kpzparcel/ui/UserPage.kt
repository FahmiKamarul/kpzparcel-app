package com.example.kpzparcel.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kpzparcel.model.ParcelData
import com.example.kpzparcel.ui.theme.Montserrat
import com.example.kpzparcel.viewmodel.ParcelUiState
import com.example.kpzparcel.viewmodel.ParcelViewModel
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

@Composable
fun UserPage(
    trackingNumber: String,
    viewModel: ParcelViewModel = viewModel() // 1. Get the ViewModel
) {

    // 2. TRIGGER THE API CALL
    // LaunchedEffect runs once when this screen opens.
    // It tells the ViewModel: "Go get data for this tracking number!"
    LaunchedEffect(trackingNumber) {
        viewModel.fetchParcel(trackingNumber)
    }

    // 3. OBSERVE THE RESULT
    // This variable updates automatically: Loading -> Success OR Error
    val state by viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(vertical = 40.dp)
    ) {

        // 4. SHOW UI BASED ON STATE
        when (val currentState = state) {
            is ParcelUiState.Loading -> {
                CircularProgressIndicator(color = Color(0xFF6D5E0F))
                Spacer(modifier = Modifier.height(10.dp))
                Text("Searching for $trackingNumber...")
            }
            is ParcelUiState.Error -> {
                Text(
                    text = "Error: ${currentState.message}",
                    color = Color.Red,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                // Optional: Add a "Try Again" button here if you want
            }
            is ParcelUiState.Success -> {
                // If successful, show the details using your custom design
                ParcelDetails(parcel = currentState.parcel)
            }
        }
    }
}

@Composable
fun ParcelDetails(parcel: ParcelData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Parcel Found!",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 45.sp,
                fontFamily = Montserrat, // Make sure you have this font imported
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6D5E0F)
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Displaying the data from the API
        DetailItem(label = "Status", value = parcel.status)
        DetailItem(label = "Owner", value = parcel.customer_name)
        DetailItem(label = "Shelf", value = parcel.shelf_number.toString())
        DetailItem(label = "Weight", value = parcel.weight)
        DetailItem(label = "Date", value = parcel.date_arrived)

        Spacer(modifier = Modifier.height(20.dp))
        if (parcel.status != "Collected") {

            Text(
                text = "Please collect soon!",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBA1A1A)
                ),
                modifier = Modifier.padding(16.dp)
            )

            // --- BARCODE GENERATION START ---
            // This creates the barcode only once and remembers it
            val barcodeBitmap = remember(parcel.tracking_number) {
                try {
                    val encoder = BarcodeEncoder()
                    // 600 x 150 pixels is a good size for a barcode
                    encoder.encodeBitmap(parcel.tracking_number, BarcodeFormat.CODE_128, 600, 150)
                } catch (e: Exception) {
                    null
                }
            }

            // Show the barcode if it was created successfully
            barcodeBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Barcode for ${parcel.tracking_number}",
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(0.8f) // Takes up 80% of screen width
                        .height(100.dp)
                )

                // Optional: Show the number below the barcode for clarity
                Text(
                    text = parcel.tracking_number,
                    style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            // --- BARCODE GENERATION END ---

        }
        // Optional: You can add an 'else' if you want a different message when it IS collected
        else {
            Text(
                text = "Thank you for collecting your parcel.",
                color = Color.Green,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Text(
        text = "$label: $value",
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = Montserrat,
        ),
        modifier = Modifier.padding(5.dp)
    )
}