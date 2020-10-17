package com.ujjwalkumar.qkart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MyordersActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double t = 0;
    private String UID = "";
    private double lat = 0;
    private double lng = 0;
    private HashMap<String, Object> tmp = new HashMap<>();
    private double sum = 0;
    private HashMap<String, Object> cmap = new HashMap<>();
    private HashMap<String, Object> fmap = new HashMap<>();
    private double status = 0;

    private ArrayList<HashMap<String, Object>> lmp_orders = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();

    private ImageView imageviewback;
    private TextView textviewstatus;
    private ListView listview1;
    private TextView textviewamt;

    private Intent inmo = new Intent();
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
        setContentView(R.layout.myorders);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        imageviewback = (ImageView) findViewById(R.id.imageviewback);
        textviewstatus = (TextView) findViewById(R.id.textviewstatus);
        listview1 = (ListView) findViewById(R.id.listview1);
        textviewamt = (TextView) findViewById(R.id.textviewamt);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                finish();
            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                tmp = filtered.get((int) _position);
                inmo.setAction(Intent.ACTION_VIEW);
                inmo.setClass(getApplicationContext(), OrderdetailsActivity.class);
                inmo.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                inmo.putExtra("map", new Gson().toJson(tmp));
                startActivity(inmo);
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
        UID = sp1.getString("uid", "");
        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
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
        db3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                lmp_orders = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        lmp_orders.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                t = 0;
                sum = 0;
                for (int _repeat13 = 0; _repeat13 < (int) (lmp_orders.size()); _repeat13++) {
                    if (lmp_orders.get((int) t).get("custuid").toString().equals(UID)) {
                        cmap = new Gson().fromJson(lmp_orders.get((int) t).get("sellermap").toString(), new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                        fmap = lmp_orders.get((int) t);
                        fmap.put("name", cmap.get("name").toString());
                        fmap.put("address", cmap.get("address").toString());
                        fmap.put("lat", cmap.get("lat").toString());
                        fmap.put("lng", cmap.get("lng").toString());
                        fmap.put("contact", cmap.get("contact").toString());
                        filtered.add(fmap);
                        sum = sum + Double.parseDouble(lmp_orders.get((int) t).get("amt").toString());
                    }
                    t++;
                }
                if (filtered.size() > 0) {
                    textviewstatus.setVisibility(View.GONE);
                    textviewamt.setText(String.valueOf(sum));
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("No orders found");
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
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
                _v = _inflater.inflate(R.layout.orders, null);
            }

            final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
            final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
            final LinearLayout linear9 = (LinearLayout) _v.findViewById(R.id.linear9);
            final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
            final LinearLayout linear10 = (LinearLayout) _v.findViewById(R.id.linear10);
            final TextView textviewname = (TextView) _v.findViewById(R.id.textviewname);
            final TextView textview8 = (TextView) _v.findViewById(R.id.textview8);
            final TextView textviewamt = (TextView) _v.findViewById(R.id.textviewamt);
            final TextView textview10 = (TextView) _v.findViewById(R.id.textview10);
            final TextView textviewtime = (TextView) _v.findViewById(R.id.textviewtime);
            final LinearLayout linear4 = (LinearLayout) _v.findViewById(R.id.linear4);
            final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
            final LinearLayout linear6 = (LinearLayout) _v.findViewById(R.id.linear6);
            final LinearLayout linear7 = (LinearLayout) _v.findViewById(R.id.linear7);
            final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
            final TextView textview3 = (TextView) _v.findViewById(R.id.textview3);
            final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
            final TextView textview4 = (TextView) _v.findViewById(R.id.textview4);
            final ImageView imageview3 = (ImageView) _v.findViewById(R.id.imageview3);
            final TextView textview5 = (TextView) _v.findViewById(R.id.textview5);
            final ImageView imageview4 = (ImageView) _v.findViewById(R.id.imageview4);
            final TextView textview6 = (TextView) _v.findViewById(R.id.textview6);
            final TextView textviewhelp = (TextView) _v.findViewById(R.id.textviewhelp);
            final TextView textview11 = (TextView) _v.findViewById(R.id.textview11);
            final TextView textvieworderid = (TextView) _v.findViewById(R.id.textvieworderid);

            cal.setTimeInMillis((long) (Double.parseDouble(filtered.get((int) _position).get("time").toString())));
            textviewname.setText(filtered.get((int) _position).get("name").toString());
            textviewamt.setText(filtered.get((int) _position).get("amt").toString());
            textviewtime.setText(new SimpleDateFormat("dd-MM-yyyy   HH:mm:ss").format(cal.getTime()));
            textvieworderid.setText(filtered.get((int) _position).get("oid").toString());
            status = Double.parseDouble(filtered.get((int) _position).get("status").toString());
            if (status == 1) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status0);
                imageview3.setImageResource(R.drawable.status0);
                imageview4.setImageResource(R.drawable.status0);
            }
            if (status == 2) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status2);
                imageview3.setImageResource(R.drawable.status0);
                imageview4.setImageResource(R.drawable.status0);
            }
            if (status == 3) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status2);
                imageview3.setImageResource(R.drawable.status3);
                imageview4.setImageResource(R.drawable.status0);
            }
            if (status == 4) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status2);
                imageview3.setImageResource(R.drawable.status3);
                imageview4.setImageResource(R.drawable.status4);
            }
            textviewhelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    inmo.setAction(Intent.ACTION_VIEW);
                    inmo.setClass(getApplicationContext(), HelpActivity.class);
                    inmo.putExtra("oid", filtered.get((int) _position).get("oid").toString());
                    inmo.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(inmo);
                }
            });

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
