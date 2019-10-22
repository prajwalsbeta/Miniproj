package me.shubhamthorat.miniproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class Selectchild extends AppCompatActivity {
    Button btn1,btn2;
    FirebaseFirestore db;
//    private Task<QuerySnapshot> ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectchild);


        ListView listView;
        final ArrayList<String> vName = new ArrayList<>();
//        String vDate[]={"10/02/1999","10/02/1999"};
        final ArrayList<String> vDate = new ArrayList<>();
        final ArrayList<String> vGender = new ArrayList<>();
        listView= findViewById(R.id.listviewselect);
        final Selectchild.Myaddapter1 myaddapter1 = new Selectchild.Myaddapter1(Selectchild.this,vName,vDate);
        listView.setAdapter(myaddapter1);

//        btn1 = (Button)findViewById(R.id.button2);
//        btn2 = (Button)findViewById(R.id.button3);
        vName.add("test");
        vDate.add("new");
        vGender.add("m");
        db = FirebaseFirestore.getInstance();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String user_id=pref.getString("user_id","");
//        db.collection("users/"+user_id+"/Chidren/")
        db.collectionGroup("Children").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // [START_EXCLUDE]
                        for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                            if(snap.getData().keySet().contains("id")) {
                                vGender.add( snap.getData().get("Gender").toString());
                                vDate.add( snap.getData().get("DOB").toString());
                                vName.add( snap.getId());
//                                vDate.add( snap.getData().get("DOB").toString());
                                Log.d("test************",snap.getData().get("Name").toString());
                                Log.d("test************",snap.getData().get("Gender").toString());

                                Log.d("Select child ***", snap.getId() + " => " + snap.getData());
                            }
                        }
                       // myaddapter1.clear();
                        myaddapter1.notifyDataSetChanged();
                        Log.d("test************",vDate.toString());
                        Log.d("test************",vName.toString());
                        Log.d("test************",vGender.toString());

                        // [END_EXCLUDE]
                    }
                });
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Select Child *****", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d("Select Child *****", "Error getting documents: ", task.getException());
//                        }
//                    }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Selectchild.this, "Error!", Toast.LENGTH_SHORT).show();
//                Log.d("Profile",e.toString());
//            }
//        });





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Toast.makeText(Selectchild.this,"Clicked",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
// //               db.collection("users/"+user_id+"");
//
//                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Selectchild.this);
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putString("DOB", "19/06/1999");
//                edit.commit();
//                edit.putString("child_id","nameb");
//                edit.commit();
//                Intent intent = new Intent(Selectchild.this, chart.class);
//                startActivity(intent);
//
//            }
//        });
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Selectchild.this);
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putString("child_id","name1");
//                edit.commit();
//                edit.putString("DOB", "19/06/1999");
//                edit.commit();
//                Intent intent = new Intent(Selectchild.this, chart.class);
//                startActivity(intent);
//
//            }
//        });

    }

    class Myaddapter1 extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> tname;
        ArrayList<String> tdate;

        Myaddapter1(Context c, ArrayList<String> tname, ArrayList<String> tdate) {
            super(c, R.layout.vaclist, R.id.vaccname, tname);
            this.context = c;
            this.tname = tname;
            this.tdate = tdate;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vaclist = layoutInflater.inflate(R.layout.vaclist, parent, false);
            TextView vacName = vaclist.findViewById(R.id.vaccname);
            TextView vacDate = vaclist.findViewById(R.id.vaccdate);

            vacName.setText(tname.get(position));
            vacDate.setText(tdate.get(position));


            return vaclist;
        }
    }


}
