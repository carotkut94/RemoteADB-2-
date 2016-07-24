package com.death.remoteadb;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class About extends AppCompatActivity {

    int unicode = 0x1F495;
    String str = "\u1F495";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Typeface tf = Typeface.createFromAsset(getAssets(),"Font4.ttf");
        TextView title = (TextView) findViewById(R.id.about);
        TextView content = (TextView) findViewById(R.id.aboucontent);
        TextView slogan = (TextView) findViewById(R.id.slogan);
        slogan.setTypeface(tf);
        slogan.setText("Made with ♥️ in India");
        title.setTypeface(tf);
        content.setTypeface(tf);
    }
}
