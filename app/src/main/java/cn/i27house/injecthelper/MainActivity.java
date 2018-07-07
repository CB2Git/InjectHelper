package cn.i27house.injecthelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import cn.inject.annotation.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    Integer age;

    String name;


    List<Object> mkk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
