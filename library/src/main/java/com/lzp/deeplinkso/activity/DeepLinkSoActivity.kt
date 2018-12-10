package com.lzp.deeplinkso.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lzp.deeplinkso.R
import com.lzp.deeplinkso.utils.DeepLinkSoNavigator

/**
 * DeepLink中转页面
 * */
class DeepLinkSoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)

        DeepLinkSoNavigator.startActivity(this)
        finish()
    }

}
