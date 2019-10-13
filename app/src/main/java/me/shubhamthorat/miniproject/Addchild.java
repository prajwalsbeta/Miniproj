package me.shubhamthorat.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Addchild extends AppCompatActivity {

    private EditText addname;
    private EditText adddob;
    private EditText addbloodgroup;
    private EditText addphone;
    private EditText addgender;
    private Button addbutton;
    FirebaseFirestore db;

    String name, dob, bloodgroup, phone, gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchild);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String user_id = pref.getString("user_id", "");
        addname = (EditText) findViewById(R.id.addname);
        adddob = (EditText) findViewById(R.id.adddob);
        addbloodgroup = (EditText) findViewById(R.id.addbloodgroup);
        addphone = (EditText) findViewById(R.id.addphone);
        addgender = (EditText) findViewById(R.id.addgender);
        addbutton = (Button) findViewById(R.id.addbutton);
        db = FirebaseFirestore.getInstance();
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = addname.getText().toString();
                dob = adddob.getText().toString();
                bloodgroup = addbloodgroup.getText().toString();
                phone = addphone.getText().toString();
                gender = addgender.getText().toString();


                DocumentReference docRef = db.collection("users").document(user_id).collection("Children").document(name);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> child = new HashMap<>();
                                child.put("Name", name);
                                child.put("DOB", dob);
                                child.put("Bloodgroup", bloodgroup);
                                child.put("Gender", phone);
                                child.put("Phone", gender);

                                db.collection("users").document(user_id).collection("Children").document(name)
                                        .set(child)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error writing document", e);
                                            }
                                        });
                            }
                        }
                    }
                });
                Intent intent = new Intent(Addchild.this, Selectchild.class);
                startActivity(intent);



            }
        });

    }


    public String getdate(String oDate, int days) {
        String oldDate = oDate;
        //Specifying date format that matches the given date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(oldDate));
        } catch (ParseException e) {
            //        Log.i("TAG",e.getMessage());
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, days);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        //Displaying the new Date after addition of Days
        return newDate;
    }

}



