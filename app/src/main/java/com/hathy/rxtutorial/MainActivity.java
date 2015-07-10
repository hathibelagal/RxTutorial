package com.hathy.rxtutorial;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Basics();

        view = new TextView(this);
        setContentView(view);

        MultiThreaded.updateView(view);
    }
}
