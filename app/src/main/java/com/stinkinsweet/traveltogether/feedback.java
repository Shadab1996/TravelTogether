package com.stinkinsweet.traveltogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class feedback extends AppCompatActivity {
    private Button btnFeedback;
    private TextView txtTitle,txtFeedback;
    private EditText txtDesc;
    private String user_name="";
    private DatabaseReference rootFeedback= FirebaseDatabase.getInstance().getReference().getRoot().child("Feedback");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnFeedback=(Button)findViewById(R.id.btnFeedback);
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        txtFeedback=(TextView)findViewById(R.id.txtFeedback);

        txtDesc=(EditText)findViewById(R.id.txtDesc);

        user_name = getIntent().getExtras().get("user_name").toString();



    }
    public void sendFeedback(View view)
    {
        String report;

        report=txtDesc.getText().toString();
        rootFeedback.push().setValue(user_name+" - "+report);
        Toast.makeText(feedback.this, "Thank You "+user_name, Toast.LENGTH_SHORT).show();
        Intent i=new Intent(this,travel_room.class);
        startActivity(i);
        finish();
    }
}
