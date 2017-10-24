package com.stinkinsweet.traveltogether;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * Created by Funkies PC on 13-Dec-16.
 */
public class  travel_journey extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private boolean firstDraw=false;
    private  GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private EditText txtSend;
    private TextView txtChatArea;
    private ImageButton btnSend;
private ImageButton btnMapView;
    private LatLng latLng;
private String left,join;
   private SupportMapFragment mapFrag;
    final CharSequence[] mapView = { "Street", "Satellite","Terrain" };
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationManager locationManager;
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms");
    private DatabaseReference chatRoot=FirebaseDatabase.getInstance().getReference().child("Chats");
    private PolylineOptions lineOptions;
    private String user_name="",room_name="",key="";
    private ArrayList<String> list_of_friends=new ArrayList<>();
    private ArrayList<String> list_of_keys=new ArrayList<>();
    private ArrayList<String> list_of_keys_new=new ArrayList<>();
private    Intent intent;
    private ArrayList<String> match=new ArrayList<>();
    private String chatKey;
    private  String chatName,chatMessage;
     private String friend;
    String friendKey;
    private Double latitude=0.0,longitude=0.0;
    private int y=0;
    int inputSelection=0;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private ArrayList<LatLng> points;
    private ArrayList<LatLng> allLatLng=new ArrayList<>();
    Polyline line;
    private String text;
private Intent starterIntent;
private Boolean fromRecreate=false;
private Window window;
    // Span to set text color to some RGB value
    final ForegroundColorSpan fcsJoin = new ForegroundColorSpan(Color.GREEN);
    final ForegroundColorSpan fcsLeft = new ForegroundColorSpan(Color.RED);
    int i=1,sizeOfList=0;
    // Span to make text bold
    final StyleSpan bssJoin = new StyleSpan(android.graphics.Typeface.BOLD);
    final StyleSpan bssLeft = new StyleSpan(android.graphics.Typeface.BOLD);

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if(!fromRecreate)
        root.child(room_name).child(key).setValue(null);
    // int i =list_of_keys.indexOf(key);
      //  list_of_keys.remove(i);
      /*  if(list_of_keys_new.contains(key)) {
            finish();
            startActivity(intent);
        }*/


    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_travel_journey);

        txtSend=(EditText)findViewById(R.id.txtSend);
        txtChatArea=(TextView) findViewById(R.id.txtChatArea);
        btnSend=(ImageButton) findViewById(R.id.btnSend);
        btnMapView=(ImageButton) findViewById(R.id.btnMapView);
starterIntent=getIntent();
        txtChatArea.setMovementMethod(new ScrollingMovementMethod());

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        key = getIntent().getExtras().get("key").toString();

        list_of_friends=getIntent().getExtras().getStringArrayList("list_of_friends");
       list_of_keys =getIntent().getExtras().getStringArrayList("list_of_keys");

sizeOfList=list_of_friends.size();


        //calculateFriendsForKey();
        points = new ArrayList<LatLng>();
       // SpannableStringBuilder sbJoin = new SpannableStringBuilder("HAS JOINED THE ROOM");






        window = this.getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);




        calculateLists();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                populateButtons(list_of_friends,list_of_keys);
            }
        }, 1000);

      //  populateButtons(list_of_friends,list_of_keys);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();





        root.child(room_name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {


 /*               if(i>sizeOfList) {
                    Friend sortFriend = dataSnapshot.getValue(Friend.class);
                    join = sortFriend.getName();
                    Toast.makeText(travel_journey.this, join + " joined the room", Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            recreate();
                        }
                    }, 2000);

                    fromRecreate = true;
                }
              i++;
*/

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {



                Friend sortFriend = dataSnapshot.getValue(Friend.class);
                left = sortFriend.getName();

                if(!user_name.equals(left))
                    Toast.makeText(travel_journey.this,left+ " left the room", Toast.LENGTH_SHORT).show();
                recreate();
                fromRecreate=true;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        chatRoot.child(room_name).addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s) {

                appendChatConversation(dataSnapshot);
                Message subMessage = dataSnapshot.getValue(Message.class);
                String name = subMessage.getName();
                if(name.equals(user_name))
                txtSend.setText("");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
                Message subMessage = dataSnapshot.getValue(Message.class);
                String name = subMessage.getName();
                if(name.equals(user_name))
                    txtSend.setText("");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void calculateLists() {
        root.child(room_name).orderByChild("timestamp").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_of_friends.clear();
                list_of_keys.clear();
                DataSnapshot friends = dataSnapshot;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    friendKey = child.getKey();
                    list_of_keys.add(friendKey);
                    Friend sortFriend =child.getValue(Friend.class);

                    friend = sortFriend.getName();

                    list_of_friends.add(friend);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void showMapViewOptions(View view)
    {
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("MAP TYPE");

        builder.setSingleChoiceItems(mapView,inputSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        inputSelection = item;
                        switch (inputSelection) {
                            case 0: {
                                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;
                            }
                            case 1: {
                                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            }
                            case 2: {
                                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            }
                        }
                            dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

         private static final int NUM_ROW = 1;
    private static final int NUM_COL = 4;


    private void populateButtons(ArrayList<String> key_of_friends, ArrayList<String> list_of_keys) {
      //  Toast.makeText(getApplicationContext(),""+key_of_friends.size()+"",Toast.LENGTH_LONG).show();
        TableLayout table =(TableLayout)findViewById(R.id.tableForButtons);
        for(int row=0;row<NUM_ROW;row++)
        {
            TableRow tableRow=new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));

            table.addView(tableRow);
            for(int col=0;col<list_of_friends.size();col++)
            {
               String userNameClicked="";
                String keyClicked="";
                ContextThemeWrapper newContext = new ContextThemeWrapper(this, R.style.buttonStyle);

            Button button =new Button(newContext);
                TableRow.LayoutParams relativeParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1.0f);
                relativeParams.setMargins( 5, 0, 5, 0);
                button.setLayoutParams(relativeParams);
               // button.setLayoutParams(new TableRow.LayoutParams(
               //         TableRow.LayoutParams.MATCH_PARENT,
               //         TableRow.LayoutParams.MATCH_PARENT,
               //         1.0f));
                userNameClicked=list_of_friends.get(col);
                keyClicked= list_of_keys.get(col);



                if(col==0)
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorBlue));

                }
                    //button.setBackgroundResource(R.drawable.blue);
                if(col==1)
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorRed));
                }
                   // button.setBackgroundResource(R.drawable.red);
                if(col==2)
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                }
                   // button.setBackgroundResource(R.drawable.green);
                if(col==3)
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                }
                   // button.setBackgroundResource(R.drawable.orange);
                if(col==4)
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                }
                   // button.setBackgroundResource(R.drawable.yellow);
                if(col!=0&&col!=1&&col!=2&&col!=3&&col!=4)
                {
                    button.setBackgroundColor(getResources().getColor(R.color.colorCyan));
                }
                   // button.setBackgroundResource(R.drawable.cyan);

                button.setTextColor(Color.BLACK);

                button.setText(list_of_friends.get(col));

button.setPadding(0,0,0,0);
                final String finalUserNameClicked = userNameClicked;
                final String finalkeyClicked = keyClicked;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onButtonClicked(finalUserNameClicked,finalkeyClicked);
                    }
                });
                tableRow.addView(button);
            }
        }
    }

    AsyncTask<Void,Void,Void> buttonAsync=new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    };

    private void onButtonClicked(final String userNameClicked, final String finalkeyClicked)
    {
        DatabaseReference buttonData=root.child(room_name).child(finalkeyClicked);

        buttonData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot pressedButton = dataSnapshot;
                latitude = pressedButton.child("Latitude").getValue(Double.class);
                longitude = pressedButton.child("Longitude").getValue(Double.class);

if(!(latitude==null)) {

    latLng = new LatLng(latitude, longitude);

//allLatLng.add(latLng);
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(latLng);
    markerOptions.title(userNameClicked);


    for (int i = 0; i < list_of_keys.size(); i++) {
        if (finalkeyClicked.equals(list_of_keys.get(i))) {
            if (i == 0) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
                    setActivityBackgroundColor(R.color.colorBlueBack);
                }

            } else if (i == 1) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    setActivityBackgroundColor(R.color.colorRedBack);
                }
            } else if (i == 2) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    setActivityBackgroundColor(R.color.colorGreenBack);
                }
            } else if (i == 3) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorOrange));
                    setActivityBackgroundColor(R.color.colorOrangeBack);
                }
            } else if (i == 4) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                    setActivityBackgroundColor(R.color.colorYellowBack);
                }

            } else if (i != 0 && i != 1 && i != 2 && i != 3 && i != 4) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorCyan));
                    setActivityBackgroundColor(R.color.colorCyanBack);
                }


            }
        }
    }


    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
mCurrLocationMarker.showInfoWindow();

   /* LatLngBounds.Builder bounds;
    bounds = new LatLngBounds.Builder();
    for(int i=0;i<allLatLng.size();i++)
    bounds.include(allLatLng.get(i));
    int padding = 20; // offset from edges of the map in pixels
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds.build(), padding);

    mGoogleMap.moveCamera(cu);
    mGoogleMap.animateCamera(cu);*/
    //move map camera
   mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
}

}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void appendChatConversation(DataSnapshot dataSnapshot)
    {
        Iterator i =dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            chatMessage=(String)((DataSnapshot)i.next()).getValue();
            chatName=(String)((DataSnapshot)i.next()).getValue();
            if(!match.contains(chatName)&&y==0) {

                match.add(chatName);
                y++;
            }
            txtChatArea.append(chatName+" : "+chatMessage+"\n");


        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        showExitAlertDialog();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;




            }
        }, 2000);
    }

    public void sendMessage(View view)
    {
        Map<String,Object> map=new HashMap<>();
        chatKey=chatRoot.child(room_name).push().getKey();
        root.updateChildren(map);

        DatabaseReference messageRoot=chatRoot.child(room_name).child(chatKey);

        Map<String,Object> map2=new HashMap<>();
        map2.put("Name",user_name);
        map2.put("Message",txtSend.getText().toString().trim());
        messageRoot.updateChildren(map2);

    }

    private void showExitAlertDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to exit the group?\nYour friends will not be able to see your location.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent intent = new Intent(getApplicationContext(), travel_room.class);
                        intent.putExtra("user",user_name);
                        startActivity(intent);
                        finish();




                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

   /* @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

*/
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
               // buildGoogleApiClient();

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            //buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

   /* protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
createLocationRequest();

    }*/
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(4);
    }
    @Override
    public void onConnected(Bundle bundle) {
      /*  mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(3*1000);
        mLocationRequest.setFastestInterval(3*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/
createLocationRequest();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {

                DatabaseReference friend_root=root.child(room_name).child(key);
                Map<String,Object> mapFriend=new HashMap<String,Object>();
                mapFriend.put("Name",user_name);
                mapFriend.put("Latitude",mLastLocation.getLatitude());
                mapFriend.put("Longitude",mLastLocation.getLongitude());
                friend_root.updateChildren(mapFriend);

            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
/*
    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker.remove();
        }
        if(!firstDraw)
        {

        }
        //Place current location marker
       // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude()); //you already have this

        points.add(latLng); //added

        redrawLine(); //added

        DatabaseReference friend_root=root.child(room_name).child(key);
        Map<String,Object> mapFriend=new HashMap<String,Object>();
        mapFriend.put("Name",user_name);
        mapFriend.put("Latitude",location.getLatitude());
        mapFriend.put("Longitude",location.getLongitude());
        friend_root.updateChildren(mapFriend);


    }
    private void redrawLine(){


        PolylineOptions options = new PolylineOptions().width(6).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
       // addMarker(); //add Marker in current position
        line = mGoogleMap.addPolyline(options); //add Polyline
    }



*/

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
    /*    if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker.remove();
        }*/
        if(!firstDraw)
        {
            initializeDraw();
        }

        //Place current location marker
        // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude()); //you already have this


        List<LatLng> points = line.getPoints();
        points.add(latLng);
        line.setPoints(points);

        DatabaseReference friend_root=root.child(room_name).child(key);
        Map<String,Object> mapFriend=new HashMap<String,Object>();
        mapFriend.put("Name",user_name);
        mapFriend.put("Latitude",location.getLatitude());
        mapFriend.put("Longitude",location.getLongitude());
        friend_root.updateChildren(mapFriend);


    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                   android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(travel_journey.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            //buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void setActivityBackgroundColor(int color) {
        View view = window.getDecorView();
        view.setBackgroundResource(color);
    }

    private void initializeDraw() {
        lineOptions = new PolylineOptions().width(5).color(Color.RED);
        line = mGoogleMap.addPolyline(lineOptions);
    }

}






 /* DataSnapshot chat = dataSnapshot;

        for (DataSnapshot newChat : chat.getChildren()) {
            chatMessage = newChat.child("Message").getValue(String.class);
            chatName = newChat.child("Name").getValue(String.class);

            txtChatArea.append(chatName+" : "+chatMessage+"\n");

        }
*/