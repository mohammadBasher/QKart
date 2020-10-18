package com.ujjwalkumar.qkart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkart.util.SketchwareUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MycartActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double s = 0;
    private HashMap<String, Object> order = new HashMap<>();
    private HashMap<String, Object> sellermap = new HashMap<>();
    private HashMap<String, Object> custmap = new HashMap<>();

    private ArrayList<HashMap<String, Object>> cart = new ArrayList<>();

    private LinearLayout lineardetail;
    private ImageView imageviewback;
    private TextView textviewsellername;
    private TextView textviewselleradd;
    private TextView textvieweditaddress;
    private TextView textviewaddress;
    private EditText edittextcomment;
    private TextView textviewshowhide;
    private ImageView imageviewshowhide;
    private ListView listview1;
    private TextView textviewamt;
    private LinearLayout linearplaceorder;

    private Intent inc = new Intent();
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private DatabaseReference db3 = _firebase.getReference("orders");
    private ChildEventListener _db3_child_listener;
    private SharedPreferences sp1;
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.mycart);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        lineardetail = (LinearLayout) findViewById(R.id.lineardetail);
        imageviewback = (ImageView) findViewById(R.id.imageviewback);
        textviewsellername = (TextView) findViewById(R.id.textviewsellername);
        textviewselleradd = (TextView) findViewById(R.id.textviewselleradd);
        textvieweditaddress = (TextView) findViewById(R.id.textvieweditaddress);
        textviewaddress = (TextView) findViewById(R.id.textviewaddress);
        edittextcomment = (EditText) findViewById(R.id.edittextcomment);
        textviewshowhide = (TextView) findViewById(R.id.textviewshowhide);
        imageviewshowhide = (ImageView) findViewById(R.id.imageviewshowhide);
        listview1 = (ListView) findViewById(R.id.listview1);
        textviewamt = (TextView) findViewById(R.id.textviewamt);
        linearplaceorder = (LinearLayout) findViewById(R.id.linearplaceorder);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                finish();
            }
        });

        textvieweditaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inc.setAction(Intent.ACTION_VIEW);
                inc.setClass(getApplicationContext(), EditdetailsActivity.class);
                inc.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inc);
            }
        });

        edittextcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                custmap.put("comment", _charSeq);
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        imageviewshowhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (s == 0) {
                    lineardetail.setVisibility(View.VISIBLE);
                    imageviewshowhide.setImageResource(R.drawable.ic_keyboard_arrow_up_black);
                    textviewshowhide.setText("Hide details");
                    s = 1;
                } else {
                    lineardetail.setVisibility(View.GONE);
                    imageviewshowhide.setImageResource(R.drawable.ic_keyboard_arrow_down_black);
                    textviewshowhide.setText("Show details");
                    s = 0;
                }
            }
        });

        linearplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                sp1.edit().putString("lastorder", order.get("time").toString()).commit();
                db3.child(order.get("oid").toString()).updateChildren(order);
                SketchwareUtil.showMessage(getApplicationContext(), "Order placed successfully.");
                inc.setAction(Intent.ACTION_VIEW);
                inc.setClass(getApplicationContext(), OrderplacedActivity.class);
                inc.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                inc.putExtra("oid", order.get("oid").toString());
                inc.putExtra("address", sp1.getString("address", ""));
                startActivity(inc);
                finish();
            }
        });

        _db3_child_listener = new ChildEventListener() {
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
        db3.addChildEventListener(_db3_child_listener);

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
        gd1.setColor(Color.parseColor("#FF1744"));
        gd1.setCornerRadius(30);
        linearplaceorder.setBackground(gd1);
        s = 0;
        lineardetail.setVisibility(View.GONE);
        textviewshowhide.setText("Show details");
        sellermap = new Gson().fromJson(getIntent().getStringExtra("sellermap"), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        cart = new Gson().fromJson(getIntent().getStringExtra("itemmap"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        textviewsellername.setText(sellermap.get("name").toString());
        textviewselleradd.setText(sellermap.get("address").toString());
        textviewaddress.setText(sp1.getString("address", ""));
        textviewamt.setText(getIntent().getStringExtra("amount"));
        custmap = new HashMap<>();
        custmap.put("custid", sp1.getString("uid", ""));
        custmap.put("name", sp1.getString("name", ""));
        custmap.put("address", sp1.getString("address", ""));
        custmap.put("lat", sp1.getString("lat", ""));
        custmap.put("lng", sp1.getString("lng", ""));
        custmap.put("contact", sp1.getString("contact", ""));
        cal = Calendar.getInstance();
        order = new HashMap<>();
        order.put("oid", db3.push().getKey());
        order.put("selleruid", sellermap.get("uid").toString());
        order.put("custuid", sp1.getString("uid", ""));
        order.put("amt", getIntent().getStringExtra("amount"));
        order.put("status", "1");
        order.put("time", String.valueOf((long) (cal.getTimeInMillis())));
        order.put("comment", "");
        order.put("custmap", new Gson().toJson(custmap));
        order.put("sellermap", new Gson().toJson(sellermap));
        order.put("itemmap", getIntent().getStringExtra("itemmap"));
        listview1.setAdapter(new Listview1Adapter(cart));
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _view, ViewGroup _viewGroup) {
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.items, null);
            }

            final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
            final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
            final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
            final TextView textviewdetail = (TextView) _v.findViewById(R.id.textviewdetail);
            final TextView textviewitemname = (TextView) _v.findViewById(R.id.textviewitemname);
            final ImageView imageviewstar = (ImageView) _v.findViewById(R.id.imageviewstar);
            final TextView textview3 = (TextView) _v.findViewById(R.id.textview3);
            final TextView textviewitemprice = (TextView) _v.findViewById(R.id.textviewitemprice);
            final TextView textviewitemmrp = (TextView) _v.findViewById(R.id.textviewitemmrp);
            final LinearLayout linearop1 = (LinearLayout) _v.findViewById(R.id.linearop1);
            final ImageView imageviewminus = (ImageView) _v.findViewById(R.id.imageviewminus);
            final TextView textviewqty = (TextView) _v.findViewById(R.id.textviewqty);
            final ImageView imageviewplus = (ImageView) _v.findViewById(R.id.imageviewplus);

            android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
            gd1.setColor(Color.parseColor("#FFFFFF"));
            gd1.setCornerRadius(30);
            linear2.setBackground(gd1);
            imageviewplus.setVisibility(View.GONE);
            imageviewminus.setVisibility(View.GONE);
            imageviewstar.setVisibility(View.GONE);
            textviewitemmrp.setVisibility(View.INVISIBLE);
            textviewitemname.setText(cart.get((int) _position).get("name").toString());
            textviewitemprice.setText(cart.get((int) _position).get("price").toString());
            textviewdetail.setText(cart.get((int) _position).get("detail").toString());
            textviewqty.setText(cart.get((int) _position).get("qty").toString());

            return _v;
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
