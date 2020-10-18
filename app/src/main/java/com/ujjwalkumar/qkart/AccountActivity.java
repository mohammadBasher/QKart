package com.ujjwalkumar.qkart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Random;

public class AccountActivity extends AppCompatActivity {

    private LinearLayout linearinfo;
    private LinearLayout linearedit;
    private TextView textviewsignout;
    private ImageView imageviewback;
    private ImageView imageviewprofile;
    private TextView textviewname;
    private TextView textviewcontact;
    private TextView textviewemail;
    private TextView textviewaddress;

    private Intent inp = new Intent();
    private SharedPreferences sp1;
    private AlertDialog.Builder confirm;
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.account);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        linearinfo = (LinearLayout) findViewById(R.id.linearinfo);
        linearedit = (LinearLayout) findViewById(R.id.linearedit);
        textviewsignout = (TextView) findViewById(R.id.textviewsignout);
        imageviewback = (ImageView) findViewById(R.id.imageviewback);
        imageviewprofile = (ImageView) findViewById(R.id.imageviewprofile);
        textviewname = (TextView) findViewById(R.id.textviewname);
        textviewcontact = (TextView) findViewById(R.id.textviewcontact);
        textviewemail = (TextView) findViewById(R.id.textviewemail);
        textviewaddress = (TextView) findViewById(R.id.textviewaddress);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        confirm = new AlertDialog.Builder(this);
        auth = FirebaseAuth.getInstance();

        linearedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inp.setAction(Intent.ACTION_VIEW);
                inp.setClass(getApplicationContext(), EditdetailsActivity.class);
                inp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inp);
                finish();
            }
        });

        textviewsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                confirm.setTitle("Sign out");
                confirm.setMessage("Do you want to sign out?");
                confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        FirebaseAuth.getInstance().signOut();
                        sp1.edit().putString("uid", "").commit();
                        sp1.edit().putString("email", "").commit();
                        sp1.edit().putString("name", "").commit();
                        sp1.edit().putString("address", "").commit();
                        sp1.edit().putString("lat", "").commit();
                        sp1.edit().putString("lng", "").commit();
                        sp1.edit().putString("contact", "").commit();
                        sp1.edit().putString("img", "").commit();
                        sp1.edit().putString("lastorder", "").commit();
                        inp.setAction(Intent.ACTION_VIEW);
                        inp.setClass(getApplicationContext(), AuthenticateActivity.class);
                        inp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(inp);
                        finish();
                    }
                });
                confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                confirm.create().show();
            }
        });

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inp.setAction(Intent.ACTION_VIEW);
                inp.setClass(getApplicationContext(), HomeActivity.class);
                inp.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inp);
                finish();
            }
        });

        _auth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();

            }
        };
    }

    private void initializeLogic() {
        android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
        gd1.setColor(Color.parseColor("#B0BEC5"));
        gd1.setCornerRadius(180);
        imageviewprofile.setBackground(gd1);
        android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
        gd2.setColor(Color.parseColor("#FFFFFF"));
        gd2.setCornerRadius(50);
        linearinfo.setBackground(gd2);
        textviewname.setText(sp1.getString("name", ""));
        textviewcontact.setText(sp1.getString("contact", ""));
        textviewemail.setText(sp1.getString("email", ""));
        textviewaddress.setText(sp1.getString("address", ""));
        if (!sp1.getString("img", "").equals("")) {
            Glide.with(getApplicationContext()).load(Uri.parse(sp1.getString("img", ""))).into(imageviewprofile);
        }
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
