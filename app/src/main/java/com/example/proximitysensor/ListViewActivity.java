package com.example.proximitysensor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    SQLiteDatabase SQLITEDATABASE;
    SQLiteHelper SQLITEHELPER;
    SQLiteListAdapter ListAdapter;

    Cursor cursor;

    ArrayList<String> ID_ArrayList = new ArrayList<>();
    ArrayList<String> TITLE_ArrayList = new ArrayList<>();
    ArrayList<String> value_ArrayList = new ArrayList<>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = findViewById(R.id.list);

        SQLITEHELPER = new SQLiteHelper(this);
    }

    @Override
    protected void onResume() {

        ViewSQLiteDBData();
        super.onResume();
    }

    private void ViewSQLiteDBData() {
        SQLITEDATABASE = SQLITEHELPER.getWritableDatabase();
        cursor = SQLITEDATABASE.rawQuery("SELECT * FROM Nama_Tabel", null);

        ID_ArrayList.clear();
        TITLE_ArrayList.clear();
        value_ArrayList.clear();

        if (cursor.moveToFirst()) {
            do {
                ID_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ID)));
                TITLE_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_TITLE)));
                value_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_VALUE)));
            } while (cursor.moveToNext());
        }

        ListAdapter = new SQLiteListAdapter(ListViewActivity.this, ID_ArrayList, TITLE_ArrayList,value_ArrayList);

        listView.setAdapter(ListAdapter);
        cursor.close();
    }

}
