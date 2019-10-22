package me.shubhamthorat.miniproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Profile extends AppCompatActivity {

    private TextView pname,pemail;
    private Button button;
    private EditText ppincode;
    private FirebaseFirestore db;
    private DocumentReference noteRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Profile.this);
        final String user_id=pref.getString("user_id","");
        setContentView(R.layout.activity_profile);
        db = FirebaseFirestore.getInstance();
        noteRef = db.collection("users").document(user_id);
        pname = (TextView) findViewById(R.id.pname);
        pemail = (TextView) findViewById(R.id.pemail);
        ppincode = (EditText) findViewById(R.id.ppincode);
        button = findViewById(R.id.pbutton);

        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);



        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    String pincode = documentSnapshot.getString("pincode");
                    String url = documentSnapshot.getString("photo_url");
                    pname.setText(username);
                    pemail.setText(email);
                    ppincode.setText(pincode);
                    progressDialog.dismiss();

                }else {
                    Toast.makeText(Profile.this, "Profile error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("Profile",e.toString());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pincode = ppincode.getText().toString();
                Map<String, Object> user = new HashMap<>();
                user.put("pincode", pincode);

                noteRef.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Profile.this, "Pin Code udated!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Error! Try again", Toast.LENGTH_SHORT).show();
                        Log.d("Profile",e.toString());
                    }
                });
//                noteRef.update("pincode",pincode);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(Profile.this, "Loading Error!", Toast.LENGTH_SHORT).show();
                    Log.d("Profile",e.toString());
                    return;
                }
                if (documentSnapshot.exists()){
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    String pincode = documentSnapshot.getString("pincode");
                    pname.setText(username);
                    pemail.setText(email);
                    ppincode.setText(pincode);
                    progressDialog.dismiss();
                }
            }
        });
    }
}
