package com.example.lab
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: Button
    private lateinit var textViewMessages: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSendMessage = findViewById(R.id.buttonSendMessage)
        textViewMessages = findViewById(R.id.textViewMessages)

        buttonSendMessage.setOnClickListener {
            val message = editTextMessage.text.toString()
            sendMessage(message)
        }

        database.child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messages = StringBuilder()
                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(String::class.java)
                    messages.append("$message\n")
                }
                textViewMessages.text = messages.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val loggButton = findViewById<Button>(R.id.button3)
        val SettingsButton = findViewById<Button>(R.id.button4)
        val ResultButton = findViewById<Button>(R.id.button6)

        loggButton.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        SettingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        ResultButton.setOnClickListener {
            val intent = Intent(this, result::class.java)
            startActivity(intent)
        }
    }

    private fun sendMessage(message: String) {
        val key = database.child("messages").push().key
        key?.let {
            database.child("messages").child(it).setValue(message)
        }

        editTextMessage.text.clear()
    }
}
