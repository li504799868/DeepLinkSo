package com.lzp.deeplinkso.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

/**
 * 测试用Activity
 * */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val userId = intent.getLongExtra("userId", 0)
        val userName = intent.getStringExtra("userName")
        Toast.makeText(this, "$userId:$userName", Toast.LENGTH_SHORT).show()
    }
}
