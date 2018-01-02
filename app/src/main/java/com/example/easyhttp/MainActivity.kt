package com.example.easyhttp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.easyhttp.http.callback.OnResultCallBack
import com.example.easyhttp.http.subscriber.HttpSubscriber
import com.knightdavion.kotlin.ibiliplayer.data.remote.HttpManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var resultStr: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            resultStr = ""
            HttpManager.getDatas(HttpSubscriber(object : OnResultCallBack<TestBean> {
                override fun onSuccess(t: TestBean) {
                    t.list.forEach {
                        resultStr += it.toString()
                        result.text = resultStr
                    }
                }

                override fun onError(code: Int, errorMsg: String) {
                    result.text = "onError: code:$code  errorMsg:$errorMsg";
                }
            }, lifecycle), 1, 10, "json")
        }
    }
}

