package me.shubhamthorat.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Selectchild extends AppCompatActivity {
    Button btn1,btn2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Ref;
    String user_id;
    private ChildAdapter adapter;
//    private Task<QuerySnapshot> ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectchild);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = pref.getString("user_id", "");
        Ref = db.collection("users/"+user_id+"/Children");
        setRecycler();
    }



    private void setRecycler(){
        Query query = Ref;

        FirestoreRecyclerOptions<Child> options = new FirestoreRecyclerOptions.Builder<Child>()
                .setQuery(query, Child.class)
                .build();

        adapter = new ChildAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Intent intent = new Intent(Selectchild.this, chartActivity.class);
                intent.putExtra("CHILD_ID",id);
                Log.d("***********",id);
                intent.putExtra("DOB",documentSnapshot.getData().get("DOB").toString());
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
