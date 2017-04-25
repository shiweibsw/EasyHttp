package com.example.easyhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.easyhttp.http.HttpManager;
import com.example.easyhttp.http.callback.OnResultCallBack;
import com.example.easyhttp.http.subscriber.HttpSubscriber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private HttpSubscriber mHttpObserver;
    private TextView resultTv;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = (TextView) findViewById(R.id.result);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result="";
                resultTv.setText("");
                HttpManager.getInstance().getDatasWithCache(mHttpObserver, 1, 10, "json", false);
            }
        });

        mHttpObserver = new HttpSubscriber(new OnResultCallBack<TestBean>() {
            @Override
            public void onSuccess(TestBean tb) {
                for (TestBean.ListBean bean : tb.getList()) {
                    result += bean.toString();
                }
                resultTv.setText(result);
            }

            @Override
            public void onError(int code, String errorMsg) {
                resultTv.setText("onError: code:" + code + "  errorMsg:" + errorMsg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHttpObserver.unSubscribe();
    }
}

