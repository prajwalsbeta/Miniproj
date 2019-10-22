package me.shubhamthorat.miniproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class chart extends AppCompatActivity {
    TextView child_n, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15;
    ListView listView;
    String vName[]= {"asdf000","asdfff"};
    String vDate[]={"10/02/1999","10/02/1999"};



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String child_id = pref.getString("child_id", "");
        final String DOB = pref.getString("DOB", "");

        listView= findViewById(R.id.listview);
        Myaddapter myaddapter = new Myaddapter(this,vName,vDate);
        listView.setAdapter(myaddapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Toast.makeText(chart.this,"Clicked",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    class Myaddapter extends ArrayAdapter<String>{

        Context context;
        String tName[];
        String tDate[];

        Myaddapter(Context c, String tname[],String tdate[]){
            super(c, R.layout.vaclist, R.id.vaccname, tname);
            this.context = c;
            this.tName = tname;
            this.tDate = tdate;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vaclist = layoutInflater.inflate(R.layout.vaclist, parent, false);
            TextView vacName = vaclist.findViewById(R.id.vaccname);
            TextView vacDate = vaclist.findViewById(R.id.vaccdate);

            vacName.setText(tName[position]);
            vacDate.setText(tDate[position]);


            return vaclist;
        }
    }
}





//    public String getdate(String oDate, int days) {
//        String oldDate = oDate;
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(sdf.parse(oldDate));
//        } catch (ParseException e) {
//                    Log.i("TAG",e.getMessage());
//        }
//
//        c.add(Calendar.DAY_OF_MONTH, days);
//        String newDate = sdf.format(c.getTime());
//        return newDate;
//    }
