package com.example.cropdiseasedetector

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.cropdiseasedetector.ui.theme.CropDiseaseDetectorTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CropDiseaseDetectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PickImageFromGallery()
//                        PhotoPicker()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CropDiseaseDetectorTheme {
        PickImageFromGallery()
    }
}

@Composable
fun PickImageFromGallery() {

//    var selectedImageUri by remember {
//        mutableStateOf<Uri?>(null)
//    }
//
//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = {uri ->  selectedImageUri = uri}
//    )
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }
//    var prediction = ""
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column{
            // Your content here
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Your bottom row content here
            Button(
                onClick = {
//                    singlePhotoPickerLauncher.launch(
//                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                    )
                    launcher.launch("image/*")
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Text(text = "Pick photo")
            }
            Button(
                onClick = {
                    uploadImage(selectedImageUri!!, context)
                    val prediction = updateTest(selectedImageUri!!, context = context)
//                            val bitmap = getBitmapFromUri(selectedImageUri!!, context = context)
//                            classifyImage(bitmap!!, context)
                    onValueChange(context, prediction)
//                    database.child("Test").addValueEventListener(object : ValueEventListener{
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            val value = snapshot.getValue(String::class.java)
//                            if(value=="Done")
//                                makeToast(context, "The Prediction is $prediction", 10000)
//                        }
//                    })
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Text(text = "Predict")
            }
        }
    }
}

//@Composable
//fun PhotoPicker() {
//    var selectedImageUri by remember { mutableStateOf<String?>(null) }
//    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
//        selectedImageUri = uri.toString()
//    }
//
//    Surface(color = MaterialTheme.colors.background) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "Select an image", style = MaterialTheme.typography.h6)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Show the selected image if available
//            selectedImageUri?.let { uri ->
//                Image(
//                    painter = rememberImagePainter(uri),
//                    contentDescription = "Selected image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp),
//                    contentScale = ContentScale.Crop
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Button to launch the content picker
//            Button(onClick = { launcher.launch("image/*") }) {
//                Text(text = "Select")
//            }
//        }
//    }
//}