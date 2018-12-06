package com.example.it_lab_local.pet_db;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.it_lab_local.pet_db.AddDogActivity;
import com.example.it_lab_local.pet_db.DatabaseConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent addDog = new Intent(this, AddDogActivity.class);
            startActivity(addDog);

            return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected void onResume() {
        DatabaseConnector dc = new DatabaseConnector(this);
        dc.open();
        Cursor c = dc.getAllDogs();
        if(c!=null)
        {
            final ListView lst = (ListView)findViewById(R.id.lst);
            final ArrayList<String> list = new ArrayList<String>();

            if(c.moveToFirst())
            {
                do{
                    String breed = c.getString(c.getColumnIndex("breed"));
                    String name = c.getString(c.getColumnIndex("name"));
                    int age = c.getInt(c.getColumnIndex("age"));
                    list.add(breed + " " + name + " " + String.valueOf(age));
                }while (c.moveToNext());
            }
            final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1,list);
            lst.setAdapter(adapter);
        }

        super.onResume();
    } // end onResume

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String,Integer> midMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects){
            super(context, textViewResourceId,objects);
            for(int i=0;i<objects.size();++i){
                midMap.put(objects.get(i),i);
            }
        }
        @Override
        public long getItemId(int position){
            String item = getItem(position);
            return midMap.get(item);
        }

        @Override
        public boolean hasStableIds(){
            return true;
        }
    }

} // end class
