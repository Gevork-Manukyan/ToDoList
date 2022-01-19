package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadItems()


        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove item from list
                listOfTasks.removeAt(position)
                // 2. Notify adapter that our data set changed
                adapter.notifyDataSetChanged()
                // 3. Save file
                saveItems()
            }
        }

        // Lookup the recyclerview in activity layout                              // casting
        var rvTasks: RecyclerView = findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach the adapter to the recyclerview to populate items
        rvTasks.adapter = adapter

        // Set layout manager to position the items
        rvTasks.layoutManager = LinearLayoutManager(this)


        // Create functionality for adding items to list
        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("onClick", "User clicked on button")

            var inputTextField = findViewById<EditText>(R.id.addTaskField)

            // 1. Grab the text the user has inputed into @id/addTaskField
            var inputedTask = inputTextField.text.toString()

            // 2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(inputedTask)

            // Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Clear text field
            inputTextField.setText("")

            // 4. Save file
            saveItems()
        }

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        saveItems()
//    }

    //*** Save the data the user has changed ***//
    //   By writing and reading from a file     //

    // Get the file we need
    fun getDataFile() : File {

        // Every line represents a single task
        return File(filesDir, "data.txt")
    }

    // Load items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save/write data to file
    fun saveItems() {
        try {
            // writeLines: for each element in list, it writes to a new line in file
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}