package pro.kondratev.xlsxpoiexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Main3Activity extends Activity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemLongClickListener {
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    DatabaseReference myRef=db.getReference();
    ListView listView;
    ArrayAdapter adapter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        myRef.addChildEventListener(ch);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        listView = (ListView) findViewById(R.id.listView);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);



    }

    ChildEventListener ch =new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            adapter.add(dataSnapshot.getKey().toString());

            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            adapter.add(dataSnapshot.getKey().toString());

            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String s=adapter.getItem(position).toString();
        myRef.child(s).removeValue();
        adapter.remove(position);
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Main3Activity.this,Main3Activity.class);
        startActivity(intent);
        finish();


        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
