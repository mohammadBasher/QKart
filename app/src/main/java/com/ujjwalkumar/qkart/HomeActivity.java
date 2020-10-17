package com.ujjwalkumar.qkart;

import android.animation.ObjectAnimator;
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
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double t = 0;
    private String UID = "";
    private double lat = 0;
    private double lng = 0;
    private double u = 0;
    private HashMap<String, Object> cmap = new HashMap<>();
    private double dist = 0;
    private double lat1 = 0;
    private double lon1 = 0;
    private double lat2 = 0;
    private double lon2 = 0;
    private double dlat = 0;
    private double dlon = 0;
    private double a = 0;
    private HashMap<String, Object> tmp = new HashMap<>();

    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> lmp_shops = new ArrayList<>();

    private ImageView imageviewabout;
    private LinearLayout linearinfo;
    private LinearLayout linearloading;
    private ListView listview1;
    private EditText edittext1;
    private ImageView imageviewcart;
    private TextView textviewstatus;
    private LinearLayout linearorders;
    private LinearLayout linearslist;
    private LinearLayout linearhelp;
    private LinearLayout linearaccount;

    private Intent inh = new Intent();
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private DatabaseReference db2 = _firebase.getReference("consumers");
    private ChildEventListener _db2_child_listener;
    private DatabaseReference db3 = _firebase.getReference("orders");
    private ChildEventListener _db3_child_listener;
    private SharedPreferences sp1;
    private Calendar cal = Calendar.getInstance();
    private DatabaseReference db1 = _firebase.getReference("sellers");
    private ChildEventListener _db1_child_listener;
    private Calendar cal2 = Calendar.getInstance();
    private ObjectAnimator ani1 = new ObjectAnimator();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.home);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        imageviewabout = (ImageView) findViewById(R.id.imageviewabout);
        linearinfo = (LinearLayout) findViewById(R.id.linearinfo);
        linearloading = (LinearLayout) findViewById(R.id.linearloading);
        listview1 = (ListView) findViewById(R.id.listview1);
        edittext1 = (EditText) findViewById(R.id.edittext1);
        imageviewcart = (ImageView) findViewById(R.id.imageviewcart);
        textviewstatus = (TextView) findViewById(R.id.textviewstatus);
        linearorders = (LinearLayout) findViewById(R.id.linearorders);
        linearslist = (LinearLayout) findViewById(R.id.linearslist);
        linearhelp = (LinearLayout) findViewById(R.id.linearhelp);
        linearaccount = (LinearLayout) findViewById(R.id.linearaccount);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), AboutActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                tmp = filtered.get((int) _position);
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), ManageitemsActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                inh.putExtra("map", new Gson().toJson(tmp));
                startActivity(inh);
            }
        });

        edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                _searchlist(_charSeq);
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        linearorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), MyordersActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        linearslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), ShoppinglistActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        linearhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), HelpActivity.class);
                inh.putExtra("oid", "");
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
            }
        });

        linearaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                inh.setAction(Intent.ACTION_VIEW);
                inh.setClass(getApplicationContext(), AccountActivity.class);
                inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inh);
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

        _db1_child_listener = new ChildEventListener() {
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
        db1.addChildEventListener(_db1_child_listener);

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
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(50);
        linearinfo.setBackground(gd1);
        UID = sp1.getString("uid", "");
        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
        ani1.setTarget(imageviewcart);
        ani1.setPropertyName("translationX");
        ani1.setFloatValues((float) (0), (float) (700));
        ani1.setInterpolator(new BounceInterpolator());
        ani1.setDuration((int) (10000));
        ani1.start();
        _loadlist();
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    private void _loadlist() {
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                lmp_shops = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        lmp_shops.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                t = 0;
                u = 0;
                for (int _repeat11 = 0; _repeat11 < (int) (lmp_shops.size()); _repeat11++) {
                    _distance(Double.parseDouble(lmp_shops.get((int) t).get("lat").toString()), Double.parseDouble(lmp_shops.get((int) t).get("lng").toString()));
                    if (dist < Double.parseDouble(lmp_shops.get((int) t).get("range").toString())) {
                        cmap = lmp_shops.get((int) t);
                        cmap.put("distance", new DecimalFormat("0.00").format(dist));
                        filtered.add(cmap);
                    }
                    t++;
                }
                if (filtered.size() > 0) {
                    ani1.cancel();
                    linearloading.setVisibility(View.GONE);
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("No shop found in your locality.");
                    ani1.cancel();
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
    }


    private void _distance(final double _latc, final double _lngc) {
        lat1 = Math.toRadians(lat);
        lon1 = Math.toRadians(lng);
        lat2 = Math.toRadians(_latc);
        lon2 = Math.toRadians(_lngc);
        dlat = lat2 - lat1;
        dlon = lon2 - lon1;
        a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + ((Math.cos(lat1 / 2) * Math.cos(lat2 / 2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2)));
        dist = 12742 * Math.asin(Math.sqrt(a));
    }


    private void _searchlist(final String _str) {
        filtered.clear();
        linearloading.setVisibility(View.VISIBLE);
        textviewstatus.setText("Loading nearby shops ...");
        ani1.start();
        t = 0;
        u = 0;
        for (int _repeat12 = 0; _repeat12 < (int) (lmp_shops.size()); _repeat12++) {
            if (lmp_shops.get((int) t).get("name").toString().toLowerCase().contains(_str.toLowerCase())) {
                _distance(Double.parseDouble(lmp_shops.get((int) t).get("lat").toString()), Double.parseDouble(lmp_shops.get((int) t).get("lng").toString()));
                if (dist < Double.parseDouble(lmp_shops.get((int) t).get("range").toString())) {
                    cmap = lmp_shops.get((int) t);
                    cmap.put("distance", new DecimalFormat("0.00").format(dist));
                    filtered.add(cmap);
                }
            }
            t++;
        }
        if (filtered.size() > 0) {
            ani1.cancel();
            linearloading.setVisibility(View.GONE);
            listview1.setAdapter(new Listview1Adapter(filtered));
            ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
        } else {
            textviewstatus.setText("No result found.");
            ani1.cancel();
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
                _v = _inflater.inflate(R.layout.shops, null);
            }

            final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
            final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
            final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
            final TextView textviewshopname = (TextView) _v.findViewById(R.id.textviewshopname);
            final TextView textviewshopaddress = (TextView) _v.findViewById(R.id.textviewshopaddress);
            final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
            final TextView textviewdistance = (TextView) _v.findViewById(R.id.textviewdistance);
            final TextView textview4 = (TextView) _v.findViewById(R.id.textview4);

            linear1.setElevation(4);
            if (filtered.get((int) _position).containsKey("name")) {
                textviewshopname.setText(filtered.get((int) _position).get("name").toString());
            }
            if (filtered.get((int) _position).containsKey("address")) {
                textviewshopaddress.setText(filtered.get((int) _position).get("address").toString());
            }
            if (filtered.get((int) _position).containsKey("distance")) {
                textviewdistance.setText(filtered.get((int) _position).get("distance").toString());
            }

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
