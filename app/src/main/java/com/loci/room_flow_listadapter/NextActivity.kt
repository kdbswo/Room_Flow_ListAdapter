package com.loci.room_flow_listadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.loci.room_flow_listadapter.databinding.ActivityNextBinding
import com.loci.room_flow_listadapter.db.MyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NextActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = MyDatabase.getDatabase(this)

        val recyclerView = binding.userRV
        val myListAdapter = MyListAdapter()

        recyclerView.adapter = myListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.read.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().getAllDataWithFlow().collect {

                    withContext(Dispatchers.Main) {
                        myListAdapter.submitList(it)
                    }

                }
            }
        }

        binding.update.setOnClickListener {
            val id = binding.id.text.toString().toInt()

            CoroutineScope(Dispatchers.IO).launch {
                val result = db.userDao().getAllData()
                val userEntity = result[id]
                userEntity.name = "update 된 id"

                db.userDao().update(userEntity)
            }
        }

        binding.remove.setOnClickListener {

            val id = binding.id.text.toString().toInt()


            CoroutineScope(Dispatchers.IO).launch {
                val result = db.userDao().getAllData()
                val userEntity = result[id]

                db.userDao().delete(userEntity)
            }
        }

    }
}