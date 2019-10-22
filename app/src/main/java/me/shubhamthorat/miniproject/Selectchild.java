package me.shubhamthorat.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

public class Selectchild extends AppCompatActivity {
    Button btn1,btn2;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectchild);
        btn1 = (Button)findViewById(R.id.button2);
        btn2 = (Button)findViewById(R.id.button3);
        db = FirebaseFirestore.getInstance();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String user_id=pref.getString("user_id","");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

 //               db.collection("users/"+user_id+"");

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Selectchild.this);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("DOB", "19/06/1999");
                edit.commit();
                edit.putString("child_id","nameb");
                edit.commit();
                Intent intent = new Intent(Selectchild.this, chart.class);
                startActivity(intent);

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Selectchild.this);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("child_id","name1");
                edit.commit();
                edit.putString("DOB", "19/06/1999");
                edit.commit();
                Intent intent = new Intent(Selectchild.this, chart.class);
                startActivity(intent);

            }
        });

    }




}
