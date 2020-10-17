package com.ujjwalkumar.qkart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ShoppinglistActivity extends AppCompatActivity {


    private HashMap<String, Object> mp = new HashMap<>();

    private ArrayList<HashMap<String, Object>> lmp = new ArrayList<>();

    private ImageView imageviewback;
    private LinearLayout linear1;
    private ListView listview1;
    private EditText edittext;
    private ImageView imageviewadd;

    private Intent insl = new Intent();
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.shoppinglist);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        imageviewback = (ImageView) findViewById(R.id.imageviewback);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        listview1 = (ListView) findViewById(R.id.listview1);
        edittext = (EditText) findViewById(R.id.edittext);
        imageviewadd = (ImageView) findViewById(R.id.imageviewadd);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                finish();
            }
        });

        imageviewadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
//                if (!edittext.getText().toString().equals("")) {
//                    mp = new HashMap<>();
//                    mp.put("name", edittext.getText().toString());
//                    lmp.add(mp);
//                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
//                    edittext.setText("");
//                    sp1.edit().putString("slist", new Gson().toJson(lmp)).commit();
//                } else {
//                    SketchwareUtil.showMessage(getApplicationContext(), "Enter item name");
//                }
            }
        });
    }

    private void initializeLogic() {
        android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(30);
        linear1.setBackground(gd1);
        if (!sp1.getString("slist", "").equals("")) {
            lmp = new Gson().fromJson(sp1.getString("slist", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
        }
        _refreshList();
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    private void _refreshList() {
        if (lmp.size() > 0) {
            listview1.setAdapter(new Listview1Adapter(lmp));
            ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
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
                _v = _inflater.inflate(R.layout.slist, null);
            }

            final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
            final TextView textviewname = (TextView) _v.findViewById(R.id.textviewname);
            final ImageView imageviewdelete = (ImageView) _v.findViewById(R.id.imageviewdelete);

            android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
            gd2.setColor(Color.parseColor("#FFCCBC"));
            gd2.setCornerRadius(30);
            linear1.setBackground(gd2);
            if (lmp.get((int) _position).containsKey("name")) {
                textviewname.setText(lmp.get((int) _position).get("name").toString());
            }
            imageviewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    lmp.remove((int) (_position));
                    sp1.edit().putString("slist", new Gson().toJson(lmp)).commit();
                    _refreshList();
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
