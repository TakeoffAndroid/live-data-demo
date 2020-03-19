package com.takeoffandroid.livedatademo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private val simpleLiveData = MutableLiveData<String>()
    private val mediatorLiveData = MediatorLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        simpleLiveData.observe(this, Observer {
            showToast("I'm a simple live data. So directly reading message from the edit text = $it")
        })

        mediatorLiveData.observe(this, Observer {
            showToast(it)
        })

        sendLiveDataTrigger.setOnClickListener {
            simpleLiveData.value = messageCatcher.text.toString()
        }

        mediatorLiveDataTrigger.setOnClickListener {
            mediatorLiveData.addSource(simpleLiveData) {
                mediatorLiveData.value = "I'm a mediator live data and I have subscribed myself to simple live data = $it"
            }
            mediatorLiveData.removeSource(simpleLiveData)
        }

        transformationMapTrigger.setOnClickListener {
            getTransformedData().observe(this, Observer {
                showToast("I'm a transformed live data and I transformed myself from simple live data = $it")
            })
        }
    }


    private fun getTransformedData(): LiveData<String> {
        return Transformations.map(simpleLiveData) {
            it
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
