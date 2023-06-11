package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnInsert, btnUpdate, btnDelete, btnLoad;
    private EditText edtId, edtName, edtAge;
    private ListView lv;
    private ArrayList<Person> myPersonList;
    private ArrayAdapter<Person> myAdapter;
    SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Find Id
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        edtId = (EditText) findViewById(R.id.edtId);
        edtName = (EditText) findViewById(R.id.edtName);
        edtAge = (EditText) findViewById(R.id.edtAge);

        //Create listview
        lv = (ListView) findViewById(R.id.lv);
        myPersonList = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myPersonList);
        lv.setAdapter(myAdapter);

        //Crate or open database
        myDatabase = openOrCreateDatabase("PeopleManagement.db", MODE_PRIVATE, null);
        //Create table
        try{
            String sql = "CREATE TABLE people(id INTEGER primary key, name TEXT, age INTEGER)";
            myDatabase.execSQL(sql);
        }catch (Exception e){
            Log.e("Error", "Table already exists");
        }

        //Onclick Button
        btnInsert.setOnClickListener(v -> {
            int id = Integer.parseInt(edtId.getText().toString());
            String name = edtName.getText().toString();
            int age = Integer.parseInt(edtAge.getText().toString());
            ContentValues myValue = new ContentValues();
            myValue.put("id", id);
            myValue.put("name", name);
            myValue.put("age", age);
            String msg = "";
            if(myDatabase.insert("people", null, myValue) == -1){
                msg = "Insert Unsuccessful";
            }else{
                msg = "Insert Successfully";
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            ShowList();
        });

        btnUpdate.setOnClickListener(v -> {
            String id = edtId.getText().toString();
            String name = edtName.getText().toString();
            int age = Integer.parseInt(edtAge.getText().toString());
            ContentValues myValue = new ContentValues();
            myValue.put("name", name);
            myValue.put("age", age);
            String msg = "";
            if(myDatabase.update("people", myValue, "id = ?", new String[]{id}) == 0){
                msg = "Update Unsuccessful";
            }else{
                msg = "Update Successfully";
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            ShowList();
        });

        btnDelete.setOnClickListener(v -> {
            String id = edtId.getText().toString();
            String msg = "";
            if(myDatabase.delete("people", "id = ?", new String[]{id}) == 0){
                msg = "Delete Unsuccessful";
            }else{
                msg = "Delete Successfully";
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            ShowList();
        });

        //lv onItemClick
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idItem = String.valueOf(myPersonList.get(position).getId());
                String ageItem = String.valueOf(myPersonList.get(position).getAge());
                edtId.setText(idItem);
                edtName.setText(myPersonList.get(position).getName());
                edtAge.setText(ageItem);
            }
        });

        btnLoad.setOnClickListener(v -> {
            ShowList();
        });


    }

    public void ShowList(){
        myPersonList.clear();
        Cursor c = myDatabase.query("people", null, null, null, null, null, null);
        c.moveToNext();
        Person person;
        while (!c.isAfterLast()){
            person = new Person(c.getInt(0), c.getString(1), c.getInt(2));
            c.moveToNext();
            myPersonList.add(person);
        }
        c.close();
        myAdapter.notifyDataSetChanged();
    }
}