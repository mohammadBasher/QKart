package com.ujjwalkumar.qkart;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AuthenticateActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double t = 0;
    private boolean isloginscreen = false;
    private double flag = 0;

    private ArrayList<HashMap<String, Object>> lmp = new ArrayList<>();

    private LinearLayout linearlogin;
    private LinearLayout linearsignup;
    private LinearLayout linear14;
    private TextView textviewforgot;
    private LinearLayout linear19;
    private LinearLayout linear20;
    private EditText edittextle;
    private EditText edittextlp;
    private ImageView imageviewlogin;
    private TextView textviewsignup;
    private LinearLayout linear4;
    private LinearLayout linear10;
    private LinearLayout linear11;
    private EditText edittextse;
    private EditText edittextsp;
    private ImageView imageviewsignup;
    private TextView textviewlogin;

    private Intent ina = new Intent();
    private ObjectAnimator anix = new ObjectAnimator();
    private ObjectAnimator aniy = new ObjectAnimator();
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private SharedPreferences sp1;
    private DatabaseReference db2 = _firebase.getReference("consumers");
    private ChildEventListener _db2_child_listener;
    private ObjectAnimator ani = new ObjectAnimator();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.authenticate);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize();
        initializeLogic();
    }

    private void initialize() {

        linearlogin = (LinearLayout) findViewById(R.id.linearlogin);
        linearsignup = (LinearLayout) findViewById(R.id.linearsignup);
        linear14 = (LinearLayout) findViewById(R.id.linear14);
        textviewforgot = (TextView) findViewById(R.id.textviewforgot);
        linear19 = (LinearLayout) findViewById(R.id.linear19);
        linear20 = (LinearLayout) findViewById(R.id.linear20);
        edittextle = (EditText) findViewById(R.id.edittextle);
        edittextlp = (EditText) findViewById(R.id.edittextlp);
        imageviewlogin = (ImageView) findViewById(R.id.imageviewlogin);
        textviewsignup = (TextView) findViewById(R.id.textviewsignup);
        linear4 = (LinearLayout) findViewById(R.id.linear4);
        linear10 = (LinearLayout) findViewById(R.id.linear10);
        linear11 = (LinearLayout) findViewById(R.id.linear11);
        edittextse = (EditText) findViewById(R.id.edittextse);
        edittextsp = (EditText) findViewById(R.id.edittextsp);
        imageviewsignup = (ImageView) findViewById(R.id.imageviewsignup);
        textviewlogin = (TextView) findViewById(R.id.textviewlogin);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        textviewforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (edittextle.getText().toString().equals("")) {
                    SketchwareUtil.showMessage(getApplicationContext(), "Enter your email");
                } else {
                    auth.sendPasswordResetEmail(edittextle.getText().toString()).addOnCompleteListener(_auth_reset_password_listener);
                }
            }
        });

        imageviewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (!edittextle.getText().toString().equals("")) {
                    if (!edittextlp.getText().toString().equals("")) {
                        imageviewlogin.setImageResource(R.drawable.ic_rotate_right_black);
                        ani.setTarget(imageviewlogin);
                        ani.setPropertyName("rotation");
                        ani.setFloatValues((float) (0), (float) (720));
                        ani.setDuration((int) (5000));
                        ani.setInterpolator(new LinearInterpolator());
                        ani.start();
                        sp1.edit().putString("email", edittextle.getText().toString()).commit();
                        sp1.edit().putString("password", edittextlp.getText().toString()).commit();
                        auth.signInWithEmailAndPassword(edittextle.getText().toString(), edittextlp.getText().toString()).addOnCompleteListener(AuthenticateActivity.this, _auth_sign_in_listener);
                    } else {
                        SketchwareUtil.showMessage(getApplicationContext(), "Enter password");
                    }
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), "Enter email");
                }
            }
        });

        textviewsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                isloginscreen = false;
                linearsignup.setVisibility(View.VISIBLE);
                linearlogin.setAlpha((float) (1));
                linearsignup.setAlpha((float) (0));
                anix.setTarget(linearsignup);
                anix.setPropertyName("alpha");
                anix.setFloatValues((float) (0), (float) (1));
                anix.setDuration((int) (500));
                aniy.setTarget(linearlogin);
                aniy.setPropertyName("alpha");
                aniy.setFloatValues((float) (1), (float) (0));
                aniy.setDuration((int) (500));
                anix.start();
                aniy.start();
            }
        });

        imageviewsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (!edittextse.getText().toString().equals("")) {
                    if (!edittextsp.getText().toString().equals("")) {
                        imageviewsignup.setImageResource(R.drawable.ic_rotate_right_black);
                        ani.setTarget(imageviewsignup);
                        ani.setPropertyName("rotation");
                        ani.setFloatValues((float) (0), (float) (720));
                        ani.setDuration((int) (5000));
                        ani.setInterpolator(new LinearInterpolator());
                        ani.start();
                        sp1.edit().putString("email", edittextse.getText().toString()).commit();
                        sp1.edit().putString("password", edittextsp.getText().toString()).commit();
                        auth.createUserWithEmailAndPassword(edittextse.getText().toString(), edittextsp.getText().toString()).addOnCompleteListener(AuthenticateActivity.this, _auth_create_user_listener);
                    } else {
                        SketchwareUtil.showMessage(getApplicationContext(), "Enter password");
                    }
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), "Enter email");
                }
            }
        });

        textviewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                isloginscreen = true;
                linearlogin.setVisibility(View.VISIBLE);
                linearsignup.setAlpha((float) (1));
                linearlogin.setAlpha((float) (0));
                anix.setTarget(linearlogin);
                anix.setPropertyName("alpha");
                anix.setFloatValues((float) (0), (float) (1));
                anix.setDuration((int) (500));
                aniy.setTarget(linearsignup);
                aniy.setPropertyName("alpha");
                aniy.setFloatValues((float) (1), (float) (0));
                aniy.setDuration((int) (500));
                anix.start();
                aniy.start();
            }
        });

        anix.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator _param1) {

            }

            @Override
            public void onAnimationEnd(Animator _param1) {
                if (!isloginscreen) {
                    linearlogin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator _param1) {

            }

            @Override
            public void onAnimationRepeat(Animator _param1) {

            }
        });

        aniy.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator _param1) {

            }

            @Override
            public void onAnimationEnd(Animator _param1) {
                if (isloginscreen) {
                    linearsignup.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator _param1) {

            }

            @Override
            public void onAnimationRepeat(Animator _param1) {

            }
        });

        _db2_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        db2.addChildEventListener(_db2_child_listener);

        _auth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                ani.cancel();
                imageviewsignup.setRotation((float) (0));
                imageviewsignup.setImageResource(R.drawable.ic_arrow_forward_black);
                if (_success) {
                    ina.setAction(Intent.ACTION_VIEW);
                    ina.setClass(getApplicationContext(), EditdetailsActivity.class);
                    ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(ina);
                    finish();
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
                }
            }
        };

        _auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                if (_success) {
                    db2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot _dataSnapshot) {
                            lmp = new ArrayList<>();
                            try {
                                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                };
                                for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                    HashMap<String, Object> _map = _data.getValue(_ind);
                                    lmp.add(_map);
                                }
                            } catch (Exception _e) {
                                _e.printStackTrace();
                            }
                            t = 0;
                            flag = -1;
                            for (int _repeat69 = 0; _repeat69 < (int) (lmp.size()); _repeat69++) {
                                if (lmp.get((int) t).get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    flag = t;
                                    break;
                                } else {
                                    t++;
                                }
                            }
                            if (!(flag == -1)) {
                                sp1.edit().putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).commit();
                                sp1.edit().putString("email", lmp.get((int) flag).get("email").toString()).commit();
                                sp1.edit().putString("name", lmp.get((int) flag).get("name").toString()).commit();
                                sp1.edit().putString("address", lmp.get((int) flag).get("address").toString()).commit();
                                sp1.edit().putString("lat", lmp.get((int) flag).get("lat").toString()).commit();
                                sp1.edit().putString("lng", lmp.get((int) flag).get("lng").toString()).commit();
                                sp1.edit().putString("contact", lmp.get((int) flag).get("contact").toString()).commit();
                                sp1.edit().putString("img", lmp.get((int) flag).get("img").toString()).commit();
                                ina.setAction(Intent.ACTION_VIEW);
                                ina.setClass(getApplicationContext(), HomeActivity.class);
                                ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(ina);
                                finish();
                            } else {
                                SketchwareUtil.showMessage(getApplicationContext(), "This email corresponds to a seller.");
                                FirebaseAuth.getInstance().signOut();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError _databaseError) {
                        }
                    });
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
                }
                ani.cancel();
                imageviewlogin.setRotation((float) (0));
                imageviewlogin.setImageResource(R.drawable.ic_arrow_forward_black);
            }
        };

        _auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                if (_success) {
                    SketchwareUtil.showMessage(getApplicationContext(), "Password reset mail sent");
                }
            }
        };
    }

    private void initializeLogic() {
        isloginscreen = true;
        if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
            if (sp1.getString("name", "").equals("")) {
                ina.setAction(Intent.ACTION_VIEW);
                ina.setClass(getApplicationContext(), EditdetailsActivity.class);
                ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(ina);
                finish();
            } else {
                ina.setAction(Intent.ACTION_VIEW);
                ina.setClass(getApplicationContext(), HomeActivity.class);
                ina.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(ina);
                finish();
            }
        } else {
            android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
            gd1.setColor(Color.parseColor("#FFFFFF"));
            gd1.setCornerRadius(50);
            linear14.setBackground(gd1);
            android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
            gd2.setColor(Color.parseColor("#FFCCBC"));
            gd2.setCornerRadius(80);
            linear19.setBackground(gd2);
            android.graphics.drawable.GradientDrawable gd3 = new android.graphics.drawable.GradientDrawable();
            gd3.setColor(Color.parseColor("#FFCCBC"));
            gd3.setCornerRadius(80);
            linear20.setBackground(gd3);
            android.graphics.drawable.GradientDrawable gd4 = new android.graphics.drawable.GradientDrawable();
            gd4.setColor(Color.parseColor("#FFFFFF"));
            gd4.setCornerRadius(50);
            linear4.setBackground(gd4);
            android.graphics.drawable.GradientDrawable gd6 = new android.graphics.drawable.GradientDrawable();
            gd6.setColor(Color.parseColor("#FFCCBC"));
            gd6.setCornerRadius(80);
            linear10.setBackground(gd6);
            android.graphics.drawable.GradientDrawable gd7 = new android.graphics.drawable.GradientDrawable();
            gd7.setColor(Color.parseColor("#FFCCBC"));
            gd7.setCornerRadius(80);
            linear11.setBackground(gd7);
            linearlogin.setVisibility(View.VISIBLE);
            linearsignup.setVisibility(View.GONE);
            db2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot _dataSnapshot) {
                    lmp = new ArrayList<>();
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                        };
                        for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            lmp.add(_map);
                        }
                    } catch (Exception _e) {
                        _e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError _databaseError) {
                }
            });
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
