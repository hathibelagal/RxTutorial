package com.hathy.rxtutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.functions.Action1;

public class EventsActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.events_activity);

        Button myButton = (Button)findViewById(R.id.my_button);
        Observable<OnClickEvent> clicksObservable = ViewObservable.clicks(myButton);

        clicksObservable
                .skip(4)
                .subscribe(new Action1<OnClickEvent>() {
                    @Override
                    public void call(OnClickEvent onClickEvent) {
                        Log.d("Click Action", "Clicked!");
                    }
                });
    }
}
