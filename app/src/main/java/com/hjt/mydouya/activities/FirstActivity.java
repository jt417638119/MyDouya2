package com.hjt.mydouya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.hjt.mydouya.R;
import com.hjt.mydouya.views.ToolbarX;


/**
 * Created by HJT on 2017/11/12.
 */

public class FirstActivity extends BaseActivity {
    private Button button;
    private ToolbarX mToolBarX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        button = (Button) findViewById(R.id.bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, SecondActivity.class));
            }
        });

        mToolBarX = getToolBar();
        mToolBarX.setTitle(R.string.title_first);
        mToolBarX.setSubTite(R.string.title_sub_first);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_first;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}













