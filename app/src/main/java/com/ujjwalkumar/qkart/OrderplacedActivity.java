package com.ujjwalkumar.qkart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class OrderplacedActivity extends AppCompatActivity {

    private ImageView imageviewback;
    private LinearLayout linearorder;
    private LinearLayout linearcontinue;
    private TextView textviewmyorders;
    private TextView textviewaddress;
    private TextView textvieworderid;

    private Intent inop = new Intent();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.orderplaced);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        imageviewback = (ImageView) findViewById(R.id.imageviewback);
        linearorder = (LinearLayout) findViewById(R.id.linearorder);
        linearcontinue = (LinearLayout) findViewById(R.id.linearcontinue);
        textviewmyorders = (TextView) findViewById(R.id.textviewmyorders);
        textviewaddress = (TextView) findViewById(R.id.textviewaddress);
        textvieworderid = (TextView) findViewById(R.id.textvieworderid);

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inop.setAction(Intent.ACTION_VIEW);
                inop.setClass(getApplicationContext(), ManageitemsActivity.class);
                inop.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inop);
                finish();
            }
        });

        linearcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inop.setAction(Intent.ACTION_VIEW);
                inop.setClass(getApplicationContext(), HomeActivity.class);
                inop.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inop);
                finish();
            }
        });

        textviewmyorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inop.setAction(Intent.ACTION_VIEW);
                inop.setClass(getApplicationContext(), MyordersActivity.class);
                inop.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inop);
                finish();
            }
        });
    }

    private void initializeLogic() {
        android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(30);
        linearorder.setBackground(gd1);
        android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
        gd2.setColor(Color.parseColor("#FF1744"));
        gd2.setCornerRadius(50);
        linearcontinue.setBackground(gd2);
        textviewaddress.setText(getIntent().getStringExtra("address"));
        textvieworderid.setText(getIntent().getStringExtra("oid"));
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }

}
