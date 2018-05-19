package com.example.nimet.bestday;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nimet.bestday.data.BestdayContract.NotlarEntry;
import com.example.nimet.bestday.data.BestdayContract.KategoriEntry;
import com.example.nimet.bestday.data.BestdayQueryHandler;

public class KategoriActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView kategoriListesi;
    EditText kategoriAdi;
    KategoriCursorAdapter adapter;
    Cursor cursor;
    long secilenKategoriID= -1;
    BestdayQueryHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        handler=new BestdayQueryHandler(this.getContentResolver());

        getLoaderManager().initLoader(5, null, this);

        kategoriListesi= (ListView) findViewById(R.id.lvKategoriListesi);
        adapter=new KategoriCursorAdapter(this, cursor, false);
        kategoriListesi.setAdapter(adapter);


        kategoriAdi= (EditText) findViewById(R.id.etKategoriAdi);

        kategoriListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                secilenKategoriID=id;
                Cursor c= (Cursor) kategoriListesi.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), "id:"+secilenKategoriID+" pos:"+position, Toast.LENGTH_SHORT).show();
                kategoriAdi.setText(c.getString(1));
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id==5){

            String[] projection= {KategoriEntry._ID, KategoriEntry.COLUMN_KATEGORI};
            return new CursorLoader(this, KategoriEntry.CONTENT_URI, projection, null, null, KategoriEntry._ID+" DESC");

        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);
    }

    public void yeniKategoriOlustur(View view) {

        kategoriAdi.setText("");
        secilenKategoriID=-1;
    }

    public void kaydetveyaGuncelle(View view) {


        String yeniKategoriAdi=kategoriAdi.getText().toString();

        ContentValues values=new ContentValues();
        values.put(KategoriEntry.COLUMN_KATEGORI, yeniKategoriAdi);

        if(secilenKategoriID==-1){
            //insert işlemi
            handler.startInsert(1, null, KategoriEntry.CONTENT_URI, values);
            kategoriAdi.setText("");
            Toast.makeText(this, "Yeni kategori eklendi", Toast.LENGTH_SHORT).show();

        }else{
            String selection=KategoriEntry._ID+"=?";
            String[] args={String.valueOf(secilenKategoriID)};
            handler.startUpdate(1, null, KategoriEntry.CONTENT_URI, values, selection, args);
            Toast.makeText(this, "Kategori güncellendi", Toast.LENGTH_SHORT).show();

        }


    }


    public void kategoriSil(View view) {

        String selection=KategoriEntry._ID+"=?";
        String[] args={String.valueOf(secilenKategoriID)};

        handler.startDelete(1, null,KategoriEntry.CONTENT_URI, selection, args);
        kategoriAdi.setText("");
        Toast.makeText(this, "Kategori silindi", Toast.LENGTH_SHORT).show();

    }




}