package com.example.simpletodo

import android.content.Intent
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
    val EDIT_TASK_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadItems()

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemClick(position: Int) {

                // 1. Send user to new activity with item data
                val taskText = listOfTasks.get(position)
                val intent = getEditTaskIntent()

                intent.putExtra("task_text", taskText)
                intent.putExtra("position", position)

                // 1.5 Activity result is handled by 'onActivityResult()'
                startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)

                // 2. Safe file
                saveItems()
            }



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

    //*** Calling Another Activity ***//

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when(requestCode) {

                EDIT_TASK_REQUEST_CODE -> {
                    Log.i("request-test", "HERE")
                    val taskPosition = data?.extras?.getInt("position")
                    val taskText = data?.extras?.getString("task_text")
                    listOfTasks[taskPosition!!] = taskText!!
                    adapter.notifyItemChanged(taskPosition)
                    saveItems()
                }

            }
        } else if (resultCode == RESULT_CANCELED) {
            return
        }
    }

    fun getEditTaskIntent() : Intent {
        return Intent(this, EditTaskActivity::class.java)
    }

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