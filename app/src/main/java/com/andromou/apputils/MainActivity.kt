package com.andromou.apputils


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.andromou.apputils.AppUtils.checkMoreApps
import com.andromou.apputils.AppUtils.isNetworkConnected
import com.andromou.apputils.AppUtils.rateApp
import com.andromou.apputils.AppUtils.shareApp
import com.andromou.apputils.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)



        binding!!.moreapps.setOnClickListener { v ->
           if (isNetworkConnected(this)) { checkMoreApps(this, "AndroMou") }

        }
        binding!!.ratetheapp.setOnClickListener { v ->
            if (isNetworkConnected(this)){ rateApp(this) }
        }

        binding!!.shareapp.setOnClickListener { v ->
            shareApp(this)
        }
     }


}