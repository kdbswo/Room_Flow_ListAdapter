package com.loci.room_flow_listadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.loci.room_flow_listadapter.databinding.ActivityMainBinding
import com.loci.room_flow_listadapter.db.MyDatabase
import com.loci.room_flow_listadapter.db.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = MyDatabase.getDatabase(this)

        val recyclerView = binding.userRv
        val myListAdapter = MyListAdapter()

        recyclerView.adapter = myListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.save.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val name = binding.name.text.toString()
                val age = binding.age.text.toString()

                val userEntity = UserEntity(0, name, age.toInt())
                db.userDao().insert(userEntity)

            }
        }


        binding.get.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().getAllDataWithFlow().collect {

                    withContext(Dispatchers.Main) {
                        myListAdapter.submitList(it)
                    }

                }
            }

        }

    }
}