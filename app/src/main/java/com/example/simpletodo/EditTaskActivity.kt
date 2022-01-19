package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EditTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

                            // Same as getIntent().getExtras()
        val bundle: Bundle? = intent.extras
        val itemPosition = bundle!!.getInt("position")
        val itemText = bundle!!.getString("task_text")

        // Substitute existing data into forms
        findViewById<EditText>(R.id.taskName).setText(itemText)

        // Cancel Button
        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        // Confirm Button
        findViewById<Button>(R.id.confirm_button).setOnClickListener {
            val taskText = findViewById<EditText>(R.id.taskName).text.toString()
            val data = Intent()

            data.putExtra("position", itemPosition)
            data.putExtra("task_text", taskText)

            setResult(RESULT_OK, data)
            finish()
        }
    }
}