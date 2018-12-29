package com.jeremelau.libapplication;

import android.os.Bundle;
import android.widget.TextView;

import com.guoguang.flickview.FlickHelper;
import com.guoguang.utils.IDCardValidateUtil;
import com.guoguang.utils.PhoneNumUtil;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.hello_world);
        FlickHelper.getFlickHelper().startFlick(textView, 1, 0, 800);

        System.out.println(IDCardValidateUtil.validate_effective("320924199812123456"));
        System.out.println(PhoneNumUtil.isPhoneNumberValid("+86" + "18888888888", "86"));
    }
}
