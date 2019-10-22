package me.shubhamthorat.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private TextView adddob;
    private EditText addbloodgroup;
    private EditText addphone;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addbutton;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    FirebaseFirestore db;

    String name, dob, bloodgroup, phone, gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchild);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String user_id = pref.getString("user_id", "");
        addname = (EditText) findViewById(R.id.addname);
        adddob = (TextView) findViewById(R.id.adddob);
        addbloodgroup = (EditText) findViewById(R.id.addbloodgroup);
        addphone = (EditText) findViewById(R.id.addphone);
        radioGroup = (RadioGroup)findViewById(R.id.bGroup);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        adddob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Addchild.this,
                        android.R.style.Theme_DeviceDefault_Light,onDateSetListener,year,month,day);
               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                year = year - 1900;
                Date d = new Date(year, month, day);
                SimpleDateFormat dateFormatter = new SimpleDateFormat(
                        "MM/dd/yyyy");
                String strDate = dateFormatter.format(d);
                adddob.setText(strDate);
            }
        };

        addbutton = (Button) findViewById(R.id.addbutton);
        db = FirebaseFirestore.getInstance();
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = addname.getText().toString();
                dob = adddob.getText().toString();
                bloodgroup = addbloodgroup.getText().toString();
                phone = addphone.getText().toString();
                gender = radioButton.getText().toString();


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



}



