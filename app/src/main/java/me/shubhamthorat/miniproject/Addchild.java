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
                        "dd/MM/yyyy");
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
                gender = addphone.getText().toString();
                phone = radioButton.getText().toString();

                if (name.isEmpty()){
                    addname.setError("Please Enter Name");
                    addname.requestFocus();
                }
                else if (bloodgroup.isEmpty()){
                    addbloodgroup.setError("Please Enter Bloodgroup");
                    addbloodgroup.requestFocus();
                }
                else if (gender.isEmpty()){
                    addphone.setError("Please Enter phone");
                    addphone.requestFocus();
                }else if (phone.isEmpty()){
                    radioButton.setError("Please Enter gender");
                    radioButton.requestFocus();
                }
                else if (dob.equalsIgnoreCase("DOB dd/mm/yyyy")){
                    adddob.setError("Please Enter DOB");
                    adddob.requestFocus();
                }else {


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


                    DocumentReference docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("BCG");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 0);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "BCG");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 1);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("BCG")
                                            .set(vacc)
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


                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Hepb1");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 0);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HepB");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 2);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HepB1")
                                            .set(vacc)
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


                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Polio");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 0);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "Polio");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 3);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Polio")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HepB2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 28);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HepB");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 4);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HepB2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Polio2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 28);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "Polio");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 5);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Polio2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DTP");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 42);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "DTP");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 6);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DTP")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 42);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HIB");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 7);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB")
                                            .set(vacc)
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

                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 42);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "PCV");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 8);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("RV");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 42);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "RV");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 9);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("RV")
                                            .set(vacc)
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







                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DPT2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 70);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "DPT2");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 11);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DPT2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 70);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HIB");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 12);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 70);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "PCV2");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 13);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("RV2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 70);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "RV2");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 14);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("RV2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HEPB3");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 84);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HepB3");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 15);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HEPB3")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DTP3");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 98);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "DTP3");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 16);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DTP3")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB3");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 98);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HIB3");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 17);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB3")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV3");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 98);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "PCV3");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 18);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV3")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("RV3");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 98);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "RV3");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 19);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("RV3")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Typhoid");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 252);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "typhoid");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 20);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Typhoid")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("MMR");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 252);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "MMR");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 21);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("MMR")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DTP4");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 266);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "DTP4");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 22);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("DTP4")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB4");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 266);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HIB4");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 23);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HIB4")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV4");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 266);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "PCV4");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 24);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("PCV4")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Vericella");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 365);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "Vericella");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 25);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Vericella")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HEPA");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 365);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HEPA");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 26);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HEPA")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("MMR2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 420);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "MMR2");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 27);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("MMR2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Varicella2");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 448);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "Vericella2");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 28);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("Vericella2")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("TDPA");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 2548);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "TDPA");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 29);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("TDPA")
                                            .set(vacc)
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





                    docRef1 = db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HPV");
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                               Snackbar snackbar = Snackbar.make(Addchild.this,"Child alreay added!",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    String vDate = getdate(dob, 3276);
                                    Map<String, Object> vacc = new HashMap<>();
                                    vacc.put("vName", "HPV");
                                    vacc.put("vDate", vDate);
                                    vacc.put("status", false);
                                    vacc.put("priority", 30);

                                    db.collection("users").document(user_id).collection("Children").document(name).collection("Vaccines").document("HPV")
                                            .set(vacc)
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

            }
        });



    }


    public String getdate(String oDate, int days) {
        String oldDate = oDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(oldDate));
        } catch (ParseException e) {
                    Log.i("TAG",e.getMessage());
        }

        c.add(Calendar.DAY_OF_MONTH, days);
        String newDate = sdf.format(c.getTime());
        return newDate;
    }

}



