package nure.ananiev.nestorian.pract_task_4;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "name.txt";

    EditText nameEditText, ageEditText;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    SQLiteDatabase db;
    PeopleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        db = new DBHelper(this).getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();

        nameEditText = findViewById(R.id.name_edit_text);
        ageEditText = findViewById(R.id.age_edit_text);
        recyclerView = findViewById(R.id.recycler_view);

        readDataFromSharedPreferences();
        readDataFromSqlite();
    }

    private String getEnteredName() {
        return nameEditText.getText().toString();
    }

    private int getEnteredAge() {
        String stringAge = ageEditText.getText().toString();
        return stringAge.isBlank() ? 0 : Integer.parseInt(stringAge);
    }

    public void saveDataToSharedPreferences(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", getEnteredName());
        editor.putInt("age", getEnteredAge());
        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    private void readDataFromSharedPreferences() {
        String name = sharedPreferences.getString("name", "");
        int age = sharedPreferences.getInt("age", -1);

        nameEditText.setText(name);

        if (age != -1) {
            ageEditText.setText(Integer.toString(age));
        }
    }

    public void addDataToSqlite(View view) {
        ContentValues values = new ContentValues();
        values.put("name", getEnteredName());
        values.put("age", getEnteredAge());
        db.insert("users", null, values);

        adapter.addPerson(new Person(getEnteredName(), getEnteredAge()));
    }

    @SuppressLint("Range")
    private void readDataFromSqlite() {
        Cursor cursor = db.query("users", null, null, null, null, null, null);
        List<Person> people = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            people.add(new Person(name, age));
        }
        cursor.close();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PeopleAdapter(people);
        recyclerView.setAdapter(adapter);
    }

    public void readNameFromFile(View view) throws IOException {
        FileInputStream fis = openFileInput(FILE_NAME);
        int c;
        String name = "";
        while ((c = fis.read()) != -1) {
            name += (char) c;
        }
        fis.close();
        nameEditText.setText(name);
    }

    public void writeNameToFile(View view) throws IOException {
        FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fos.write(getEnteredName().getBytes());
        fos.close();
    }
}