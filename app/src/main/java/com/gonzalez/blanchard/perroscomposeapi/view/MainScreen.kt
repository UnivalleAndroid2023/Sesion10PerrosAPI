package com.gonzalez.blanchard.perroscomposeapi.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.gonzalez.blanchard.perroscomposeapi.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    //var perroImages by remember { mutableStateOf(listOf<String>()) }
    val perroImages = remember { mutableStateListOf<String>() }
    var name by remember { mutableStateOf("Husky") }
    var isLoading = remember {
        mutableStateOf(true)
    }

    Column(modifier = Modifier.fillMaxSize() ){

        TextField(
            value = name,
            onValueChange = { newText ->
                name = newText
            },
            label = { Text("Ingrese la raza del perro") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            obtenerPerro(
                name.lowercase(),
                perroImages,
                isLoading,
            )
        }) {
            Text(text = "Buscar perros")
        }

        if(isLoading.value){
            CircularProgressIndicator()
        }

        LazyColumn {
            items(perroImages) { item ->
                // Mostramos cada elemento en un item de la listax

                Text(item)

                val painter: Painter = rememberImagePainter(
                    data = item,
                    builder = {
                        crossfade(true)
                    }
                )

                Image(
                    painter = painter,
                    contentDescription = "Imagen de perro",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

            }
        }



    }
}



private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/breed/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


private fun obtenerPerro(
    nombre:String,
    perroImages: MutableList<String>,
    isLoading: MutableState<Boolean>,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val call = getRetrofit().create(APIService::class.java).getPerros("$nombre/images")
        val perros = call.body()
        isLoading.value = true

        withContext(Dispatchers.Main) {

            Log.e("call", call.toString())
            //itemsList = images
            if(call.isSuccessful){
                //show recyclerview
                val images = perros?.images ?: emptyList()
                perroImages.clear()
                perroImages.addAll(images)
                isLoading.value = false
            }
        }

    }
}



@Preview
@Composable
fun PreviewMainScreen(){
    MainScreen()
}