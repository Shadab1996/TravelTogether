package com.stinkinsweet.traveltogether;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.contextmanager.internal.TimeFilterImpl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class travel_room extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private TextView txtTravelRoom,txtInternet;
    private EditText txtName, txtRoomName;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listRooms;
    private String name = "", temp_key = "";
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private DatabaseReference rootTime = FirebaseDatabase.getInstance().getReference().getRoot().child("RoomTime");
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms");
    private DatabaseReference rootChat = FirebaseDatabase.getInstance().getReference().getRoot().child("Chats");
    private String user = " ";
    private String roomName="";
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private Context context;
    private String pass="",inputPass="";
private EditText txtPass;
    private Boolean passwordCorrect=false;
    private String selectedFromList="";
   private int delay = 3000;
    private  Handler h;
    private boolean isConnected=false;
    private int count=0;
    private String nameFromDevice="";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final long TEN_MINUTES = 1 * 60 * 1000;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_room);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        txtTravelRoom = (TextView) findViewById(R.id.txtTravelRoom);
        txtInternet = (TextView) findViewById(R.id.txtInternet);

        txtRoomName = (EditText) findViewById(R.id.txtRooms);
        listRooms = (ListView) findViewById(R.id.listRooms);
        arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, list_of_rooms);
        progressDialog=new ProgressDialog(this);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
           nameFromDevice = prefs.getString("name", "default");//"No name defined" is the default value.

        }

        if(getIntent()!=null)

            user=getIntent().getStringExtra("user");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(user==null) {
                    if(nameFromDevice.equals(""))
                    inputName();
                    else {
                        name = nameFromDevice;
                        setTitle("Welcome " + name + "!");
                    }
                }
                else {

                    name = user;
                    setTitle("Welcome "+name+"!");
                }
            }
        }, 500);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2724525791873215~2614474885");
        /*AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("6E04907F287FD77E92655D86E1206BE1").build();

        mAdView.loadAd(adRequest);*/

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);








        txtPass = (EditText) findViewById(R.id.txtPass);
        txtPass.setTypeface(Typeface.DEFAULT);
        txtPass.setTransformationMethod(new PasswordTransformationMethod());

        listRooms.setAdapter(arrayAdapter);



        rootTime.orderByChild("timestamp").limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());

                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                    txtInternet.setText("");



                count++;
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        arrayAdapter.notifyDataSetChanged();

}
                arrayAdapter.notifyDataSetChanged();

              //  long tenAgo = System.currentTimeMillis() - TEN_MINUTES;
              //  if (serverTimeStamp[0] < tenAgo) {
              //      System.out.println("searchTimestamp is older than 1 minutes");
              //  }
               /* if (Math.abs(serverTimeStamp[0] -System.currentTimeMillis())>60000){
                    arrayAdapter.notifyDataSetChanged();

                    //server timestamp is within 5 minutes of current system time
                } else {
                    //server is not within 5 minutes of current system time
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFromList = (String) adapterView.getItemAtPosition(i);

                rootTime.child(selectedFromList).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pass = dataSnapshot.child("password").getValue(String.class);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isConnected)
                        checkPassword(pass);
                        else
    Toast.makeText(travel_room.this, "Connect to internet", Toast.LENGTH_SHORT).show();
                    }
                }, 500);


            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 101);
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }



    private void checkPassword(final String dataPass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

// Set up the input
        final EditText passRoom = new EditText(this);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //  input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passRoom);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (passRoom.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                }
                inputPass = passRoom.getText().toString();
                if(dataPass.equals(inputPass))
                {

                    temp_key = root.child(selectedFromList).push().getKey();

                    DatabaseReference friend_root = root.child(selectedFromList).child(temp_key);
                    HashMap<String, Object> mapFriend = new HashMap<String, Object>();
                    mapFriend.put("Name", name);
                    mapFriend.put("Latitude", 0.0);
                    mapFriend.put("Longitude", 0.0);
                    mapFriend.put("timestamp", ServerValue.TIMESTAMP);
                    friend_root.updateChildren(mapFriend);
                    Intent intent = new Intent(getApplicationContext(), travel_together.class);
                    intent.putExtra("room_name", selectedFromList);
                    intent.putExtra("user_name", name);
                    intent.putExtra("key", temp_key);
                    intent.putExtra("password",inputPass);
                    // Toast.makeText(getApplicationContext(), name + " " + selectedFromList + " " + temp_key, Toast.LENGTH_LONG).show();

                    startActivity(intent);
                }

                else
                {
                    Toast.makeText(travel_room.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }




            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        builder.setNeutralButton("HELP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(travel_room.this,"Password is set while adding a new group.\nAsk the person who created the group.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dialog.cancel();

            }

        });


        builder.setCancelable(false);
        builder.show();


    }

    public boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }
    /*if (ContextCompat.checkSelfPermission(travel_room.this,
    android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(travel_room.this,
    android.Manifest.permission.READ_CONTACTS)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

    } else {

        // No explanation needed, we can request the permission.

        ActivityCompat.requestPermissions(travel_room.class,
                new String[]{android.Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem( R.id.action_search);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search group");

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
            i.putExtra("user_name", name);
            startActivity(i);
            return true;
        }

        if (id == R.id.nameChange) {
           inputName();
            return true;
        }
        if (id == R.id.instructions) {
            instructionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void instructionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    boolean doubleBackToExitPressedOnce = false;

   /* @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit Room", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;


            }
        }, 2000);
    }
*/

    private void inputName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Name?");

// Set up the input
        final EditText input = new EditText(this);
        input.setText(user);
        input.setText(nameFromDevice);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //  input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().equals("")) {
                    Toast.makeText(travel_room.this, "Is your name Mr. Blank?", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    inputName();
                }
                name = input.getText().toString();
                setTitle("Welcome "+name+"!");

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name",name);
                editor.apply();


                        try {
                            if(isConnected()) {
                                if((list_of_rooms.size()==0)&&!name.isEmpty()) {

                                    if (!progressDialog.isShowing()) {
                                        progressDialog.setMessage("Loading Groups...");
                                        progressDialog.show();
                                    }
                                 /*   final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms

                                        }
                                    }, );*/
                                }
                            }
                            else {
                                txtInternet.setText("No Internet Connection");
                                txtInternet.setTextColor(Color.RED);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }

        });
        builder.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getApplicationContext(), "Press EXIT Button", Toast.LENGTH_SHORT).show();


                }
                return true;
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    public void addRoom(View view) {
        if(isConnected) {
            String password = txtPass.getText().toString();
            if ((!txtRoomName.getText().toString().trim().equals("")) && (!password.equals(""))) {

                if (!list_of_rooms.contains(txtRoomName.getText().toString().trim()))
                {

                    Map<String, Object> mapRoom = new HashMap<String, Object>();
                mapRoom.put(txtRoomName.getText().toString().trim(), "");
                roomName = txtRoomName.getText().toString().trim();
                root.updateChildren(mapRoom);
                rootTime.updateChildren(mapRoom);
                rootChat.updateChildren(mapRoom);
                rootTime.child(roomName).child("timestamp").setValue(ServerValue.TIMESTAMP);
                rootTime.child(roomName).child("password").setValue(password);
                temp_key = root.child(txtRoomName.getText().toString().trim()).push().getKey();
                DatabaseReference friend_root = root.child(txtRoomName.getText().toString().trim()).child(temp_key);
                Map<String, Object> mapFriend = new HashMap<String, Object>();
                mapFriend.put("Name", name);
                mapFriend.put("Latitude", 0.0);
                mapFriend.put("Longitude", 0.0);
                mapFriend.put("timestamp", ServerValue.TIMESTAMP);
                friend_root.updateChildren(mapFriend);

                Intent intent = new Intent(getApplicationContext(), travel_together.class);
                intent.putExtra("room_name", txtRoomName.getText().toString());
                intent.putExtra("user_name", name);
                intent.putExtra("key", temp_key);
                intent.putExtra("password", password);
                txtRoomName.setText("");
                txtPass.setText("");
                startActivity(intent);
            }
                else
                    Toast.makeText(travel_room.this, "This group already exists", Toast.LENGTH_SHORT).show();
            } else {
                if(txtRoomName.getText().toString().trim().equals("")) {
                    Toast toast = Toast.makeText(travel_room.this, "Enter Group name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else
                    if (txtPass.getText().toString().trim().equals("")) {
                        Toast toast = Toast.makeText(travel_room.this, "Enter Password", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
            }
        }
        else
            Toast.makeText(travel_room.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onStart() {
        super.onStart();

        try {
            if(isConnected())
            {
                isConnected=true;

            }
            else {
                txtInternet.setText("No Internet Connection");
                txtInternet.setTextColor(Color.RED);
                isConnected=false;
                count=0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // new checkInternetAsync().execute();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "travel_room Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.stinkinsweet.traveltogether/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "travel_room Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.stinkinsweet.traveltogether/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(list_of_rooms.size()!=0)
        arrayAdapter.getFilter().filter(newText);
        return false;
    }

    /*class checkInternetAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            //set message of the dialog
            h = new Handler();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {




            h.postDelayed(new Runnable(){
                public void run(){
                    try {

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    h.postDelayed(this, delay);
                }
            }, delay);
            //don't touch dialog here it'll break the application
            //do some lengthy stuff like calling login webservice
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog


            super.onPostExecute(result);
        }

    }*/
}
