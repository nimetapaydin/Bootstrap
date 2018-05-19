package com.example.nimet.bestday;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class kisilistesiActivity extends AppCompatActivity {

    ListView kisilerListesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kisilerListesi= (ListView) findViewById(R.id.lvKisiler);
    }

    public void tumKisileriGoster(View view) {

        ArrayList<String> tumKisiler=new ArrayList<>();

        ContentResolver resolver= getContentResolver();
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection={ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection=null;
        String[] selectionArgs=null;
        String sortOrder=null;

        Cursor cursor=resolver.query(uri,projection, selection, selectionArgs,sortOrder);

        if(cursor!=null & cursor.getCount()>0){

            while(cursor.moveToNext()) {
                String isim = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String numara=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                tumKisiler.add(isim + " - " +numara);
            }
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tumKisiler);
        kisilerListesi.setAdapter(adapter);

    }
}