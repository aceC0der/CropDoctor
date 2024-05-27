package com.example.cropdiseasedetector
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.content.Context
//import android.graphics.Color
//import android.util.Log
////import com.google.firebase.ml.custom.*
//import com.google.firebase.ml.modeldownloader.CustomModel
//import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
//import com.google.firebase.ml.modeldownloader.DownloadType
//import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
//import org.tensorflow.lite.Interpreter
//import java.io.*
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//
//val IMAGE_HEIGHT = 200
//val IMAGE_WIDTH = 200
//val NUM_ClASS = 14
//val LABELS: Array<String> = arrayOf("Corn___Common_Rust", " Corn___Gray_Leaf_Spot",
//    " Corn___Healthy", "Corn___Northern_Leaf_Blight", "Potato___Early_Blight",
//    "Potato___Healthy", "Potato___Late_Blight","Rice___Brown_Spot","Rice___Healthy",
//    "Rice___Leaf_Blast", "Rice___Neck_Blast",
//    "Wheat___Brown_Rust", "Wheat___Healthy", "Wheat___Yellow_Rust")
//
//
//fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
//    var inputStream: InputStream? = null
//    try {
//        inputStream = context.contentResolver.openInputStream(uri)
//        return BitmapFactory.decodeStream(inputStream)
//    } catch (e: FileNotFoundException) {
//        e.printStackTrace()
//    } finally {
//        try {
//            inputStream?.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//    return null
//}
//
////fun classifyImage(bitmap_image: Bitmap, context: Context): String {
////    // Load the custom model file
////    val localModel = FirebaseCustomLocalModel.Builder()
////        .setAssetFilePath("model.tflite")
////        .build()
////
////    val option = FirebaseModelInterpreterOptions.Builder(localModel).build()
////    val interpreter = FirebaseModelInterpreter.getInstance(option)
////
////    val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
////        .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, IMAGE_WIDTH, IMAGE_HEIGHT, 3))
////        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, NUM_ClASS))
////        .build()
////
////    val bitmap = Bitmap.createScaledBitmap(bitmap_image, IMAGE_WIDTH, IMAGE_HEIGHT, true)
////
////    val batchNum = 0
////    val input = Array(1) { Array(IMAGE_WIDTH) { Array(IMAGE_HEIGHT) { FloatArray(3) } } }
////    for (x in 0..IMAGE_WIDTH-1) {
////        for (y in 0..IMAGE_HEIGHT-1) {
////            val pixel = bitmap.getPixel(x, y)
////            // Normalize channel values to [-1.0, 1.0]. This requirement varies by
////            // model. For example, some models might require values to be normalized
////            // to the range [0.0, 1.0] instead.
////            input[batchNum][x][y][0] = (Color.red(pixel) - 127) / 255.0f
////            input[batchNum][x][y][1] = (Color.green(pixel) - 127) / 255.0f
////            input[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 255.0f
////        }
////    }
////
////    val inputs = FirebaseModelInputs.Builder()
////        .add(input) // add() as many input arrays as your model requires
////        .build()
////    var predLabel = "Failed"
////    interpreter?.run(inputs, inputOutputOptions)?.addOnSuccessListener { result ->
////        val output = result.getOutput<Array<FloatArray>>(0)
////        val probabilities = output[0]
////        var idx: Int = 0
////        var mx: Float = 0.0f
////        var p = 0
////        for (i in probabilities.indices){
////            if (probabilities[i]>mx){
////                mx = probabilities[i]
////                p = i
////            }
////            Log.i("MLKit", String.format("%s: %1.4f", LABELS[idx], probabilities[i]))
////            idx += 1
////        }
////        Log.i("MLKit", String.format("Predicted Label : %s: %1.4f", LABELS[p], probabilities[p]))
////        makeToast(context = context, String.format("Predicted Label : %s: %1.4f", LABELS[p], probabilities[p]))
////        predLabel = LABELS[p]
////    }?.addOnFailureListener { e ->
////        Log.i("MLKit", "Error!!! $e")
////    }
////    return predLabel
////}
//
//fun classifyImage(bitmap_image: Bitmap, context: Context) {
//    val conditions = CustomModelDownloadConditions.Builder()
//        .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
//        .build()
//    FirebaseModelDownloader.getInstance()
//        .getModel("model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
//            conditions)
//        .addOnSuccessListener { model: CustomModel? ->
//            // Download complete. Depending on your app, you could enable the ML
//            // feature, or switch from the local model to the remote model, etc.
//
//            // The CustomModel object contains the local path of the model file,
//            // which you can use to instantiate a TensorFlow Lite interpreter.
//            val modelFile = model?.file
//            if (modelFile != null) {
//                var interpreter = Interpreter(modelFile)
//                val bitmap = Bitmap.createScaledBitmap(bitmap_image, IMAGE_WIDTH, IMAGE_HEIGHT, true)
//                val input = ByteBuffer.allocateDirect(IMAGE_WIDTH* IMAGE_HEIGHT*3*4).order(ByteOrder.nativeOrder())
//                for (y in 0 until IMAGE_WIDTH) {
//                    for (x in 0 until IMAGE_HEIGHT) {
//                        val px = bitmap.getPixel(x, y)
//
//                        // Get channel values from the pixel value.
//                        val r = Color.red(px)
//                        val g = Color.green(px)
//                        val b = Color.blue(px)
//
//                        // Normalize channel values to [-1.0, 1.0]. This requirement depends on the model.
//                        // For example, some models might require values to be normalized to the range
//                        // [0.0, 1.0] instead.
//                        val rf = (r - 127) / 255f
//                        val gf = (g - 127) / 255f
//                        val bf = (b - 127) / 255f
//
//                        input.putFloat(rf)
//                        input.putFloat(gf)
//                        input.putFloat(bf)
//                    }
//                }
//                val bufferSize = 1000 * java.lang.Float.SIZE / java.lang.Byte.SIZE
//                val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
//                interpreter?.run(input, modelOutput)
//                modelOutput.rewind()
//                val probabilities = modelOutput.asFloatBuffer()
//                try {
////                    val reader = BufferedReader(
////                        InputStreamReader(context.assets.open("custom_labels.txt"))
////                    )
////                    for (i in 0 until probabilities.capacity()) {
////                        val label: String = reader.readLine()
////                        val probability = probabilities.get(i)
////                        println("$label: $probability")
////                    }
//                    var idx: Int = 0
//                    var mx: Float = 0.0f
//                    var p = 0
//                    for (i in 0 until  probabilities.capacity()){
//                        if (probabilities.get(i)>mx){
//                            mx = probabilities[i]
//                            p = i
//                        }
//                        Log.i("MLKit", String.format("%s: %1.4f", LABELS[idx], probabilities.get(i)))
//                        idx += 1
//                    }
//                    Log.i("MLKit", String.format("Predicted Label : %s: %1.4f", LABELS[p], probabilities[p]))
//                    makeToast(context = context, String.format("Predicted Label : %s: %1.4f", LABELS[p], probabilities[p]))
//                } catch (e: IOException) {
//                    // File not found?
//                    Log.i("MLKit", String.format("Prediction Failed!!!"))
//                }
//            }
//        }
//        .addOnFailureListener {
//            Log.i("MLKit", String.format("Prediction Failed!!! Model Not Found"))
//        }
//}