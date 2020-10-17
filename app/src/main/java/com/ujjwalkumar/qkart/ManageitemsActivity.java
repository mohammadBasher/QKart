package com.ujjwalkumar.qkart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ManageitemsActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double t = 0;
    private String selleruid = "";
    private HashMap<String, Object> tmp = new HashMap<>();
    private HashMap<String, Object> seller = new HashMap<>();
    private HashMap<String, Object> itm = new HashMap<>();
    private double amt = 0;
    private boolean isRecommended = false;
    private double u = 0;
    private double flag = 0;

    private ArrayList<HashMap<String, Object>> lmpitems = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> cart = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> slist = new ArrayList<>();

    private ImageView imageviewback;
    private TextView textviewstatus;
    private ListView listview1;
    private TextView textviewshopname;
    private TextView textviewaddress;
    private TextView textviewcontact;
    private TextView textviewamt;
    private LinearLayout linearproceed;

    private Intent ini = new Intent();
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private DatabaseReference db4 = _firebase.getReference("items");
    private ChildEventListener _db4_child_listener;
    private SharedPreferences sp1;
    private AlertDialog.Builder confirm;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.manageitems);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        imageviewback = (ImageView) findViewById(R.id.imageviewback);
        textviewstatus = (TextView) findViewById(R.id.textviewstatus);
        listview1 = (ListView) findViewById(R.id.listview1);
        textviewshopname = (TextView) findViewById(R.id.textviewshopname);
        textviewaddress = (TextView) findViewById(R.id.textviewaddress);
        textviewcontact = (TextView) findViewById(R.id.textviewcontact);
        textviewamt = (TextView) findViewById(R.id.textviewamt);
        linearproceed = (LinearLayout) findViewById(R.id.linearproceed);
        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        confirm = new AlertDialog.Builder(this);

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                finish();
            }
        });

        linearproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (amt > 0) {
                    ini.setAction(Intent.ACTION_VIEW);
                    ini.setClass(getApplicationContext(), MycartActivity.class);
                    ini.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ini.putExtra("itemmap", new Gson().toJson(cart));
                    ini.putExtra("sellermap", getIntent().getStringExtra("map"));
                    ini.putExtra("amount", String.valueOf(amt));
                    startActivity(ini);
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), "No item added in cart");
                }
            }
        });

        _db4_child_listener = new ChildEventListener() {
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
                if (_childValue.get("sellerid").toString().equals(selleruid)) {
                    _loadlist();
                }
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
                if (_childValue.get("sellerid").toString().equals(selleruid)) {
                    _loadlist();
                }
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        db4.addChildEventListener(_db4_child_listener);

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
        linearproceed.setBackground(gd1);
        if (!sp1.getString("slist", "").equals("")) {
            slist = new Gson().fromJson(sp1.getString("slist", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
        }
        isRecommended = false;
        seller = new Gson().fromJson(getIntent().getStringExtra("map"), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        amt = 0;
        selleruid = seller.get("uid").toString();
        textviewshopname.setText(seller.get("name").toString());
        textviewaddress.setText(seller.get("address").toString());
        textviewcontact.setText(seller.get("contact").toString());
        listview1.setAdapter(new Listview1Adapter(filtered));
        _loadlist();
        db4.addChildEventListener(_db4_child_listener);
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
        lmpitems.clear();
        filtered.clear();
        db4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                lmpitems = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        lmpitems.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                t = 0;
                for (int _repeat146 = 0; _repeat146 < (int) (lmpitems.size()); _repeat146++) {
                    if (lmpitems.get((int) t).get("sellerid").toString().equals(selleruid) && lmpitems.get((int) t).get("status").toString().equals("1")) {
                        tmp = lmpitems.get((int) t);
                        tmp.put("qty", "0");
                        filtered.add(tmp);
                    }
                    t++;
                }
                if (filtered.size() > 0) {
                    textviewstatus.setVisibility(View.GONE);
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("This seller has not added any items yet.");
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
    }


    private void _setAmount(final double _amt) {
        textviewamt.setText(String.valueOf(_amt));
    }


    private void _checkRecommendation(final String _name, final String _detail) {
        u = 0;
        flag = 0;
        for (int _repeat10 = 0; _repeat10 < (int) (slist.size()); _repeat10++) {
            if (_name.contains(slist.get((int) u).get("name").toString()) || _detail.contains(slist.get((int) u).get("name").toString())) {
                flag = 1;
                break;
            }
            u++;
        }
        if (flag == 1) {
            isRecommended = true;
        } else {
            isRecommended = false;
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

            android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
            gd2.setColor(Color.parseColor("#FFFFFF"));
            gd2.setCornerRadius(30);
            linear2.setBackground(gd2);
            android.graphics.drawable.GradientDrawable gd3 = new android.graphics.drawable.GradientDrawable();
            gd3.setStroke(4, Color.parseColor("#FF1744"));
            gd3.setCornerRadius(10);
            linearop1.setBackground(gd3);
            imageviewstar.setVisibility(View.GONE);
            textviewitemname.setText(filtered.get((int) _position).get("name").toString());
            textviewitemprice.setText(filtered.get((int) _position).get("price").toString());
            textviewdetail.setText(filtered.get((int) _position).get("detail").toString());
            if (!filtered.get((int) _position).get("price").toString().equals(filtered.get((int) _position).get("mrp").toString())) {
                textviewitemmrp.setText(filtered.get((int) _position).get("mrp").toString());
                textviewitemmrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textviewitemmrp.setVisibility(View.INVISIBLE);
            }
            _checkRecommendation(filtered.get((int) _position).get("name").toString(), filtered.get((int) _position).get("detail").toString());
            if (isRecommended) {
                imageviewstar.setVisibility(View.VISIBLE);
            }
            imageviewplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    itm = filtered.get((int) _position);
                    textviewqty.setText(String.valueOf((long) (Double.parseDouble(textviewqty.getText().toString()) + 1)));
                    itm.put("qty", textviewqty.getText().toString());
                    amt = amt + Double.parseDouble(filtered.get((int) _position).get("price").toString());
                    _setAmount(amt);
                    t = 0;
                    for (int _repeat105 = 0; _repeat105 < (int) (cart.size()); _repeat105++) {
                        if (cart.get((int) t).get("id").toString().equals(filtered.get((int) _position).get("id").toString())) {
                            cart.remove((int) (t));
                            break;
                        }
                        t++;
                    }
                    cart.add(itm);
                }
            });
            imageviewminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    if (Double.parseDouble(textviewqty.getText().toString()) > 0) {
                        itm = filtered.get((int) _position);
                        textviewqty.setText(String.valueOf((long) (Double.parseDouble(textviewqty.getText().toString()) - 1)));
                        itm.put("qty", textviewqty.getText().toString());
                        amt = amt - Double.parseDouble(filtered.get((int) _position).get("price").toString());
                        _setAmount(amt);
                        t = 0;
                        for (int _repeat150 = 0; _repeat150 < (int) (cart.size()); _repeat150++) {
                            if (cart.get((int) t).get("id").toString().equals(filtered.get((int) _position).get("id").toString())) {
                                cart.remove((int) (t));
                                break;
                            }
                            t++;
                        }
                        if (Double.parseDouble(textviewqty.getText().toString()) > 0) {
                            cart.add(itm);
                        }
                    }
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
