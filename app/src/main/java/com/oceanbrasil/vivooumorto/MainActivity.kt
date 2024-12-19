package com.oceanbrasil.vivooumorto

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class CharacterRM(
    val id: Int,
    val name: String,
    val status: String
)

interface RickAndMortyApi {
    @GET("character/{id}")
    fun getCharacter(@Path("id") id: Int): Call<CharacterRM>
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RickAndMortyApi::class.java)

        val characterName = findViewById<TextView>(R.id.characterName)

        val call = api.getCharacter(100)
        call.enqueue(object : retrofit2.Callback<CharacterRM> {
            override fun onResponse(p0: Call<CharacterRM>, resp: Response<CharacterRM>) {
                if (resp.isSuccessful) {
                    val characterRM = resp.body()
                    characterRM?.let {
                        characterName.text = it.name
                    }
                } else {
                    Toast.makeText(applicationContext,
                        "Erro: ${resp.code()}"
                        , Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(p0: Call<CharacterRM>, p1: Throwable) {
                Toast.makeText(applicationContext,
                    "Não foi possível obter o personagem"
                    , Toast.LENGTH_LONG).show()
            }

        })

    }
}