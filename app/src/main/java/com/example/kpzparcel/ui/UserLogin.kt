
package com.example.kpzparcel.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kpzparcel.R
import com.example.kpzparcel.ui.theme.AbrilFatFace
import com.example.kpzparcel.ui.theme.BasicRegular
import com.example.kpzparcel.ui.theme.BebasNueue
import com.example.kpzparcel.ui.theme.DotGothic
import com.example.kpzparcel.ui.theme.Jersey15
import com.example.kpzparcel.ui.theme.KPZParcelTheme
import com.example.kpzparcel.ui.theme.Montserrat

data class TrackNum(
    var code: String = ""  // This represents the tracking number.
) {
    fun isNotEmpty(): Boolean {
        return code.isNotEmpty()
    }
}

@Composable
fun UserLoginForm(

    PageUser: (String) -> Unit = {}, // Modify to accept trackingNumber as a parameter
) {
    Surface {
        var trackNum by remember { mutableStateOf(TrackNum()) } // State for the tracking number
        val context = LocalContext.current
        val image1 = painterResource(R.drawable.box)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Text(
                text = "KPZ Parcel Management System",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 70.sp,
                    fontFamily = Jersey15,
                    //fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 50.sp,
                    color = Color(0xFF6D5E0F)
                ),

            )

            Image(
                painter = image1,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Enter your tracking number",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = BasicRegular,
                    //fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 50.sp,
                ),
                modifier = Modifier.padding(10.dp)
            )

            // Tracking number input field
            TrackingNumberField(
                value = trackNum.code,
                onChange = { data -> trackNum = trackNum.copy(code = data) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    PageUser(trackNum.code)
                    trackNum = TrackNum()
                },
                enabled = trackNum.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }

            Spacer(modifier = Modifier.height(50.dp))

        }
    }
}


@Composable
fun TrackingNumberField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Tracking Number",
    placeholder: String = "Enter your tracking number"
) {

    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun AdminButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Login as Admin")
    }
}


@Preview(showBackground = true)
@Composable
fun UserLoginFormPreview() {
    KPZParcelTheme {
        UserLoginForm()
    }
}