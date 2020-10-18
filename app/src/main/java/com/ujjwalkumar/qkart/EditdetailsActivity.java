package com.ujjwalkumar.qkart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import com.ujjwalkumar.qkart.util.GoogleMapController;
import com.ujjwalkumar.qkart.util.SketchwareUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EditdetailsActivity extends AppCompatActivity {

    private Timer _timer = new Timer();
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private double lat = 0;
    private double lng = 0;
    private HashMap<String, Object> mp = new HashMap<>();
    private boolean mapReady = false;
    private ImageView imageviewprofile;
    private Button buttondone;
    private LinearLayout linear4;
    private LinearLayout linear21;
    private LinearLayout linear10;
    private LinearLayout linear11;
    private TextView textviewstatus;
    private MapView mapview1;
    private GoogleMapController _mapview1_controller;
    private EditText edittextname;
    private EditText edittextcontact;
    private EditText edittextaddress1;
    private EditText edittextaddress2;
    private EditText edittextaddress3;
    private EditText edittextaddress4;

    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;
    private Intent ine = new Intent();
    private LocationManager locate;
    private LocationListener _locate_location_listener;
    private SharedPreferences sp1;
    private DatabaseReference db2 = _firebase.getReference("consumers");
    private ChildEventListener _db2_child_listener;
    private TimerTask wait;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.editdetails);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            initializeLogic();
        }
    }

    private void initialize(Bundle _savedInstanceState) {

        imageviewprofile = (ImageView) findViewById(R.id.imageviewprofile);
        buttondone = (Button) findViewById(R.id.buttondone);
        linear4 = (LinearLayout) findViewById(R.id.linear4);
        linear21 = (LinearLayout) findViewById(R.id.linear21);
        linear10 = (LinearLayout) findViewById(R.id.linear10);
        linear11 = (LinearLayout) findViewById(R.id.linear11);
        textviewstatus = (TextView) findViewById(R.id.textviewstatus);
        mapview1 = (MapView) findViewById(R.id.mapview1);
        mapview1.onCreate(_savedInstanceState);
        edittextname = (EditText) findViewById(R.id.edittextname);
        edittextcontact = (EditText) findViewById(R.id.edittextcontact);
        edittextaddress1 = (EditText) findViewById(R.id.edittextaddress1);
        edittextaddress2 = (EditText) findViewById(R.id.edittextaddress2);
        edittextaddress3 = (EditText) findViewById(R.id.edittextaddress3);
        edittextaddress4 = (EditText) findViewById(R.id.edittextaddress4);
        auth = FirebaseAuth.getInstance();
        locate = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        imageviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

            }
        });

        buttondone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (!edittextname.getText().toString().equals("")) {
                    if (!edittextcontact.getText().toString().equals("")) {
                        if (!edittextaddress1.getText().toString().equals("")) {
                            if (!(lat == 0)) {
                                _save_info();
                                mp = new HashMap<>();
                                mp.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mp.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                mp.put("name", sp1.getString("name", ""));
                                mp.put("address", sp1.getString("address", ""));
                                mp.put("contact", sp1.getString("contact", ""));
                                mp.put("img", sp1.getString("img", ""));
                                mp.put("lat", sp1.getString("lat", ""));
                                mp.put("lng", sp1.getString("lng", ""));
                                db2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(mp);
                                SketchwareUtil.showMessage(getApplicationContext(), "saved successfully");
                                finish();
                            } else {
                                SketchwareUtil.showMessage(getApplicationContext(), "Location not updated");
                            }
                        } else {
                            SketchwareUtil.showMessage(getApplicationContext(), "Enter address");
                        }
                    } else {
                        SketchwareUtil.showMessage(getApplicationContext(), "Enter contact");
                    }
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), "Enter name");
                }
            }
        });

        _mapview1_controller = new GoogleMapController(mapview1, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap _googleMap) {
                _mapview1_controller.setGoogleMap(_googleMap);
                mapReady = true;
//                _mapview1_controller.addMarker("id", 28.644800, 77.216721);
//
//                _googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//                    @Override
//                    public void onMarkerDragStart(Marker marker) {
//                    }
//
//                    @Override
//                    public void onMarkerDrag(Marker marker) {
//                    }
//
//                    @Override
//                    public void onMarkerDragEnd(Marker marker) {
//                        LatLng latLng = marker.getPosition();
//                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//                        try {
//                            android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        });

        _locate_location_listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location _param1) {
                final double _lat = _param1.getLatitude();
                final double _lng = _param1.getLongitude();
                final double _acc = _param1.getAccuracy();
                lat = _lat;
                lng = _lng;
                textviewstatus.setText("Location updated");
                _setLoc(_lat, _lng);
                locate.removeUpdates(_locate_location_listener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

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
        gd1.setCornerRadius(50);
        buttondone.setBackground(gd1);
        android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
        gd2.setColor(Color.parseColor("#CFD8DC"));
        gd2.setCornerRadius(150);
        imageviewprofile.setBackground(gd2);
        android.graphics.drawable.GradientDrawable gd4 = new android.graphics.drawable.GradientDrawable();
        gd4.setColor(Color.parseColor("#FFFFFF"));
        gd4.setCornerRadius(50);
        linear4.setBackground(gd4);
        android.graphics.drawable.GradientDrawable gd3 = new android.graphics.drawable.GradientDrawable();
        gd3.setColor(Color.parseColor("#FFCCBC"));
        gd3.setCornerRadius(80);
        linear21.setBackground(gd3);
        android.graphics.drawable.GradientDrawable gd6 = new android.graphics.drawable.GradientDrawable();
        gd6.setColor(Color.parseColor("#FFCCBC"));
        gd6.setCornerRadius(80);
        linear10.setBackground(gd6);
        android.graphics.drawable.GradientDrawable gd7 = new android.graphics.drawable.GradientDrawable();
        gd7.setColor(Color.parseColor("#FFCCBC"));
        gd7.setCornerRadius(80);
        linear11.setBackground(gd7);
        mapReady = false;
        if (ContextCompat.checkSelfPermission(EditdetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locate.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, _locate_location_listener);

            Criteria criteria = new Criteria();
            String bestProvider = locate.getBestProvider(criteria, true);
            Location location = locate.getLastKnownLocation(bestProvider);
            if (location == null) {
                SketchwareUtil.showMessage(getApplicationContext(),"Please check your GPS");
            }
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                textviewstatus.setText("Location updated");
                _setLoc(lat, lng);
                locate.removeUpdates(_locate_location_listener);
            }
        }
        edittextname.setText(sp1.getString("name", ""));
        edittextcontact.setText(sp1.getString("contact", ""));
        edittextaddress1.setText(sp1.getString("address", ""));
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locate.removeUpdates(_locate_location_listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview1.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapview1.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview1.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview1.onResume();
    }

    private void _save_info() {
        sp1.edit().putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).commit();
        sp1.edit().putString("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).commit();
        sp1.edit().putString("name", edittextname.getText().toString()).commit();
        sp1.edit().putString("address", edittextaddress1.getText().toString().concat(" , ".concat(edittextaddress2.getText().toString().concat(" \n".concat(edittextaddress3.getText().toString().concat(" \n".concat(edittextaddress4.getText().toString()))))))).commit();
        sp1.edit().putString("contact", edittextcontact.getText().toString()).commit();
        sp1.edit().putString("img", "").commit();
        sp1.edit().putString("lat", String.valueOf(lat)).commit();
        sp1.edit().putString("lng", String.valueOf(lng)).commit();
    }


    private void _setLoc(final double _latitude, final double _longitude) {
        if (mapReady) {
            _mapview1_controller.moveCamera(_latitude, _longitude);
            _mapview1_controller.zoomTo(15);
            _mapview1_controller.addMarker("id", _latitude, _longitude);
            _mapview1_controller.setMarkerIcon("id", R.drawable.ic_location_on_black);
            _mapview1_controller.setMarkerVisible("id", true);
        } else {
            wait = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _setLoc(_latitude, _longitude);
                        }
                    });
                }
            };
            _timer.schedule(wait, (int) (1000));
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
