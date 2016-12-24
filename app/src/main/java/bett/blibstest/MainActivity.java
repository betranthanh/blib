package bett.blibstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import bett.blib.instagram.util.StringUtil;
import bett.blib.utils.StringUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView labelHello = (TextView) findViewById(R.id.labelHello);
        labelHello.setText(StringUtils.generateRandomString());

    }
}
