package me.shubhamthorat.miniproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class chartActivity extends AppCompatActivity {
//    TextView child_n, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15;
//    ListView listView;
    String vName[]= {"asdf000","asdfff"};
    String vDate[]={"10/02/1999","10/02/1999"};
    String child_id ;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Ref ;
    private ChartAdapter adapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String user_id = pref.getString("user_id", "");
        child_id = getIntent().getStringExtra("CHILD_ID");
//        final String DOB = pref.getString("DOB", "");
        Ref=db.collection("users/"+user_id+"/Children/"+child_id+"/Vaccines");
        Log.d("================","users/"+user_id+"/Children/"+child_id+"/Vaccines");



        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        Query query = Ref.orderBy("priority", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Chart> options = new FirestoreRecyclerOptions.Builder<Chart>()
                .setQuery(query, Chart.class)
                .build();

        adapter = new ChartAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_chart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
//    class Myaddapter extends ArrayAdapter<String>{
//
//        Context context;
//        String tName[];
//        String tDate[];
//
//        Myaddapter(Context c, String tname[],String tdate[]){
//            super(c, R.layout.vaclist, R.id.vaccname, tname);
//            this.context = c;
//            this.tName = tname;
//            this.tDate = tdate;
//
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View vaclist = layoutInflater.inflate(R.layout.vaclist, parent, false);
//            TextView vacName = vaclist.findViewById(R.id.vaccname);
//            TextView vacDate = vaclist.findViewById(R.id.vaccdate);
//
//            vacName.setText(tName[position]);
//            vacDate.setText(tDate[position]);
//
//
//            return vaclist;
//        }
//    }
}





