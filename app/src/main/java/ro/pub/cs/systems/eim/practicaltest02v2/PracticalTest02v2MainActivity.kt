package ro.pub.cs.systems.eim.practicaltest02v2
//ro.pub.cs.systems.eim.practicaltest02v2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class PracticalTest02v2MainActivity : AppCompatActivity() {

    private lateinit var wordInput: EditText
    private lateinit var submitButton: Button
    private lateinit var definitionResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test_02v2_main)

        wordInput = findViewById(R.id.word_input)
        submitButton = findViewById(R.id.submit_button)
        definitionResult = findViewById(R.id.definition_result)

        submitButton.setOnClickListener {
            val word = wordInput.text.toString()
            if (word.isNotEmpty()) {
                fetchDefinition(word)
            } else {
                definitionResult.text = "Introduceți un cuvânt!"
            }
        }
    }

    private fun fetchDefinition(word: String) {
        val apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/$word"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    definitionResult.text = "Eroare la conexiune!"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonArray = JSONArray(responseBody)
                        val firstEntry = jsonArray.getJSONObject(0)
                        val meanings = firstEntry.getJSONArray("meanings")
                        val firstMeaning = meanings.getJSONObject(0)
                        val definitions = firstMeaning.getJSONArray("definitions")
                        val firstDefinition = definitions.getJSONObject(0)

                        val definition = firstDefinition.getString("definition")
                        Log.d("HTTP_RESPONSE", "Response: $responseBody")

                        val broadcastIntent = Intent("ro.pub.cs.systems.eim.practicaltest02v2.DEFINITION_BROADCAST")
                        broadcastIntent.putExtra("definition", definition)
                        sendBroadcast(broadcastIntent)

                        runOnUiThread {
                            definitionResult.text = definition
                        }
                    } catch (e: JSONException) {
                        runOnUiThread {
                            definitionResult.text = "Nu s-a găsit definiția!"
                        }
                    }
                } else {
                    runOnUiThread {
                        definitionResult.text = "Eroare la accesarea serviciului web!"
                    }
                }
            }
        })
    }
}