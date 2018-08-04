package com.example.easyhttp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.easyhttp.http.cache.RxCache
import com.example.easyhttp.http.cache.diskconverter.GsonDiskConverter
import com.example.easyhttp.http.callback.OnResultCallBack
import com.example.easyhttp.http.subscriber.HttpSubscriber
import com.knightdavion.kotlin.ibiliplayer.data.remote.HttpManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private var resultStr: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxCache.initializeDefault(RxCache.Builder()
                .appVersion(1)
                .diskDir(File(cacheDir.path + File.separator + "data-cache"))
                .diskConverter(GsonDiskConverter())
                .diskMax((20 * 1024 * 1024).toLong())
                .memoryMax(2 * 1024 * 1024)
                .setDebug(true)
                .build())

        btn.setOnClickListener {
            resultStr = ""
            HttpManager.getDatasCached(HttpSubscriber(object : OnResultCallBack<TestBean> {
                //getDatasNoCache
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

