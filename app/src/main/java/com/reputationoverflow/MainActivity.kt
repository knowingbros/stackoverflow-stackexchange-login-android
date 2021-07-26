package com.reputationoverflow

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.reputationoverflow.databinding.ActivityMainBinding
import com.reputationoverflow.repository.Repository
import com.reputationoverflow.session.SessionUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (intent.data != null) {
            val response = SessionUtil.fromUri(intent.data!!)
            if (response != null) {
                val success = response.getSuccess()
                if (success != null) {
                    lifecycleScope.launch {
                        repository.setSession(success)
                    }
                } else {
                    val error = response.getError()!!
                    Toast.makeText(applicationContext, error.description, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}