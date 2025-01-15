package ro.pub.cs.systems.eim.practicaltest02v2


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class ServerCommunicationActivity : AppCompatActivity() {

    private lateinit var connectButton: Button
    private lateinit var serverResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_communication)

        connectButton = findViewById(R.id.connect_button)
        serverResponse = findViewById(R.id.server_response)

        connectButton.setOnClickListener {
            connectToServer()
        }
    }

    private fun connectToServer() {
        Thread {
            try {
                // Conectare la serverul local (IP-ul emulatorului este 10.0.2.2)
                val socket = Socket("10.41.27.67", 12345)
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))

                // Citirea răspunsului de la server
                val response = input.readLine()
                runOnUiThread {
                    serverResponse.text = "Răspunsul serverului: $response"
                }

                // Închidere conexiune
                input.close()
                socket.close()
            } catch (e: Exception) {
                Log.e("SERVER_ERROR", "Eroare la conectarea la server: ${e.message}")
                runOnUiThread {
                    serverResponse.text = "Eroare: ${e.message}"
                }
            }
        }.start()
    }
}