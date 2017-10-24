package com.stinkinsweet.traveltogether;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;

/**
 * Created by Funkies PC on 09-Dec-16.
 */
public class travel_together extends AppCompatActivity {
    private TextView txtYourRoom,txtJoin;
    private Button btnStart;
    private ListView listFriends;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_friends=new ArrayList<>();
    private ArrayList<String> list_of_keys=new ArrayList<>();

    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms");
    private DatabaseReference rootChat=FirebaseDatabase.getInstance().getReference().getRoot().child("Chats");
String friendKey;
    private String friend;
    private String ready;
    private   Set<String> setFriend=new HashSet<String>();
    private   Set<String> setKey=new HashSet<String>();
    private int x;
    private Context context=this;
    private Toolbar toolbar;
    Boolean fromStart=false;

    private String result="";

    private String user_name="",room_name="",key="",password="";
    private  LocationManager lm;

    @Override
    protected void onPause() {
//if(!fromStart)
//{
 //   root.child(room_name).child(key).setValue(null);
 //   finish();
//}
        super.onPause();

        //arrayAdapter.setNotifyOnChange(true);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

       root.child(room_name).child(key).setValue(null);
        rootChat.child(room_name).setValue(null);


        arrayAdapter.setNotifyOnChange(true);
    }



    @Override
    protected void onRestart() {
        super.onRestart();

          /*  Intent intent = new Intent(travel_together.this, travel_room.class);
            intent.putExtra("user", user_name);
            startActivity(intent); */
            arrayAdapter.setNotifyOnChange(true);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_together);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        txtYourRoom = (TextView) findViewById(R.id.txtYourRoom);
        txtJoin = (TextView) findViewById(R.id.txtJoin);
        btnStart = (Button) findViewById(R.id.btnStart);
        listFriends = (ListView) findViewById(R.id.listFriends);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2724525791873215~2614474885");
        AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        arrayAdapter = new ArrayAdapter<String>(
                travel_together.this, android.R.layout.simple_list_item_1, list_of_friends);
        listFriends.setAdapter(arrayAdapter);

       lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        key = getIntent().getExtras().get("key").toString();
        password=getIntent().getExtras().get("password").toString();
        txtYourRoom.setText(room_name);
txtJoin.setText("Hello "+user_name+"!\nShare password and ask friends to join your group.\n\nGroup name - "+room_name+"\nPassword - "+password);

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

                      /*  for (DataSnapshot newFriend : friends.getChildren()) {


                            friend = newFriend.child("Name").getValue(String.class);

                            setFriend.add(friend);


                        }*/


                 /*   for (DataSnapshot newFriend : friends.getChildren()) {
                        Friend sortFriend = newFriend.getValue(Friend.class);
                        for (Map.Entry<String, Friend> waypoint : sortFriend.getName().en()) {
                            String key = waypoint.getKey();
                            Waypoints value = waypoint.getValue();

                            double lat = value.getLatitude();
                            double lang = value.getLongitude();

                            latLngRealList.add(latLng);
                        }


                    }*/





/*
                    list_of_keys.clear();
                    list_of_keys.addAll(setKey);
                    list_of_friends.clear();
                    list_of_friends.addAll(setFriend);
                    */

                    arrayAdapter.notifyDataSetChanged();

            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.feedback) {
            Intent i=new Intent(this,feedback.class);
            i.putExtra("user_name", user_name);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.instructions) {
            instructionDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void instructionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();
        builder.setTitle("Instructions");

// Set up the input
        final TextView passRoom = new TextView(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //  input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inflater.inflate(R.layout.alert_dialogue_instructions, null));

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void startJourneyAlertDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
       alertDialogBuilder.setMessage("Are you sure you want to start journey with "+listFriends.getAdapter().getCount()+" members?\nMake sure all your friends have joined the group.")
.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        fromStart=true;
                        Intent i = new Intent(travel_together.this, travel_journey.class);
                        i.putExtra("room_name", room_name);
                        i.putExtra("user_name", user_name);
                        i.putExtra("key", key);

                        i.putStringArrayListExtra("list_of_friends", list_of_friends);
                        i.putStringArrayListExtra("list_of_keys", list_of_keys);

                        startActivityForResult(i,1);
                       // finish();


                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void startJourney(View view)
    {
    if(isLocationEnabled(getBaseContext()) == true)
    startJourneyAlertDialog();
        else
    Toast.makeText(travel_together.this, "Turn ON location", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
               result=data.getStringExtra("fromJourney");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

}
