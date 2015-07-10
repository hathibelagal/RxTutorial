package com.hathy.rxtutorial;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MultiThreaded {

    // Fetches remote data using OkHttp
    public static String fetchData(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String output = "";
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        output = request.url().getHost() + " "
                + response.body().string().length() + " bytes.";
        return output;
    }

    public static void updateView(final TextView view){

        Observable<String> fetchFromGoogle = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String data = fetchData("http://www.google.com");
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }catch(Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> fetchFromYahoo = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String data = fetchData("http://www.yahoo.com");
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }catch(Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());

        // Fetch from both simultaneously
        Observable<String> zipped = Observable.zip(fetchFromGoogle, fetchFromYahoo, new Func2<String, String, String>() {
            @Override
            public String call(String google, String yahoo) {
                return google + "\n" + yahoo;
            }
        });

        // Fetch one after another
        Observable<String> concatenated = Observable.concat(fetchFromGoogle, fetchFromYahoo);

        concatenated.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("Concatenated : ", Thread.currentThread().getName());
                        view.setText(view.getText() + "\n" + s);
                    }
                });
    }
}
