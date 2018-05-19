package com.example.nimet.bestday;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nimet.bestday.data.BestdayContract.KategoriEntry;
import com.example.nimet.bestday.data.BestdayContract.NotlarEntry;
import com.example.nimet.bestday.data.BestdayQueryHandler;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TUM_NOTLAR = -1;
    private static final int TUM_KATEGORILER = -1;
    Spinner spinner;
    ListView notlarListesi;
    NotlarCursorAdapter adapter;
    KategoriCursorAdapter kategoriCursorAdapter;
    Cursor cursor, kategoriCursor;
    long secilenKategoriID=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //loader initialize edilir ve oncreateloader tetiklenir

        getLoaderManager().initLoader(150, null, this);



        spinner = (Spinner) findViewById(R.id.spinner);
        kategoriCursorAdapter=new KategoriCursorAdapter(this, kategoriCursor, false);
        spinner.setAdapter(kategoriCursorAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secilenKategoriID=id;
                getLoaderManager().restartLoader(100, null, MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        notlarListesi = (ListView) findViewById(R.id.lvNotlar);
        getLoaderManager().initLoader(100, null, MainActivity.this);
        adapter = new NotlarCursorAdapter(this, cursor, false);
        notlarListesi.setAdapter(adapter);





        notlarListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, NotActivity.class);

                Cursor tiklanilanNot = (Cursor) notlarListesi.getItemAtPosition(position);

                intent.putExtra(NotlarEntry._ID, tiklanilanNot.getString(0));
                intent.putExtra(NotlarEntry.COLUMN_NOT_ICERIK, tiklanilanNot.getString(1));
                intent.putExtra(NotlarEntry.COLUMN_OLUSTURULMA_TARIHI, tiklanilanNot.getString(2));
                intent.putExtra(NotlarEntry.COLUMN_BITIS_TARIHI, tiklanilanNot.getString(3));
                intent.putExtra(NotlarEntry.COLUMN_YAPILDI, tiklanilanNot.getString(4));
                intent.putExtra(NotlarEntry.COLUMN_KATEGORI_ID, tiklanilanNot.getString(5));
                intent.putExtra(KategoriEntry.COLUMN_KATEGORI, tiklanilanNot.getString(6));

                startActivity(intent);


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_kategori) {
            Intent intent = new Intent(this, KategoriActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_tum_kategorileri_sil) {
            kategorileriSil(TUM_KATEGORILER);
            return true;
        }

        if (id == R.id.action_tum_notlari_sil) {
            notlariSil(TUM_NOTLAR);
            return true;
        }

        if (id == R.id.action_test_kategoriler) {
            testKategorilerOlustur();
            return true;
        }
        if (id == R.id.action_test_notlar) {
            testNotlarOlustur();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void testKategorilerOlustur() {

        for (int i = 1; i <= 20; i++) {

            ContentValues values = new ContentValues();
            values.put(KategoriEntry.COLUMN_KATEGORI, "Deneme Kategori #" + i);
            BestdayQueryHandler handler = new BestdayQueryHandler(this.getContentResolver());
            handler.startInsert(1, null, KategoriEntry.CONTENT_URI, values);
        }
    }

    private void testNotlarOlustur() {

        for (int i = 1; i <= 50; i++) {

            ContentValues yeniKayit = new ContentValues();
            yeniKayit.put(NotlarEntry.COLUMN_NOT_ICERIK, "YENİ NOT #" + i);
            yeniKayit.put(NotlarEntry.COLUMN_KATEGORI_ID, (i % 20) + 1);
            yeniKayit.put(NotlarEntry.COLUMN_OLUSTURULMA_TARIHI, "06-05-2017");
            yeniKayit.put(NotlarEntry.COLUMN_BITIS_TARIHI, "19-05-2017");
            yeniKayit.put(NotlarEntry.COLUMN_YAPILDI, (i % 2 == 0) ? 1 : 0);

            BestdayQueryHandler handler = new BestdayQueryHandler(this.getContentResolver());
            handler.startInsert(1, null, NotlarEntry.CONTENT_URI, yeniKayit);

        }

    }

    private void kategoriGoster() {

        String[] projection = {"_id", "kategori"};

        Cursor cursor = getContentResolver().query(KategoriEntry.CONTENT_URI, projection, null, null, null, null);

        String tumKategoriler = "";

        while (cursor.moveToNext()) {

            String id = cursor.getString(0);
            String kategori = cursor.getString(1);

            tumKategoriler = tumKategoriler + "id:" + id + " kategori:" + kategori + "\n";

        }

        Toast.makeText(this, tumKategoriler, Toast.LENGTH_LONG).show();
        Log.d("VERI", tumKategoriler);


    }


    private void notlariGuncelle() {

        ContentValues values = new ContentValues();
        values.put(NotlarEntry.COLUMN_NOT_ICERIK, "güncellenen yeni değer");


        int id = getContentResolver().update(NotlarEntry.CONTENT_URI, values, "_id=?", new String[]{"8"});
        Toast.makeText(this, "Kayıt Güncellendi:" + id, Toast.LENGTH_LONG).show();

    }

    private void notlariSil(int silinecekID) {

        String selection = "_id=?";
        String[] args = {String.valueOf(silinecekID)};
        if (silinecekID == TUM_NOTLAR) {
            selection = null;
            args = null;
        }

        BestdayQueryHandler handler = new BestdayQueryHandler(this.getContentResolver());
        handler.startDelete(1, null, NotlarEntry.CONTENT_URI, selection, args);
    }

    private void kategorileriSil(int silinecekID) {

        String selection = "_id=?";
        String[] args = {String.valueOf(silinecekID)};
        if (silinecekID == TUM_KATEGORILER) {
            selection = null;
            args = null;
        }

        int id = getContentResolver().delete(KategoriEntry.CONTENT_URI, selection, args);
        Toast.makeText(this, "Kayıt Silindi:" + id, Toast.LENGTH_LONG).show();

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == 100) {

            String selection=NotlarEntry.COLUMN_KATEGORI_ID+"=?";
            String[] selectionArg={String.valueOf(secilenKategoriID)};


            String[] projection = {NotlarEntry.TABLE_NAME + "." + NotlarEntry._ID,
                    NotlarEntry.COLUMN_NOT_ICERIK, NotlarEntry.COLUMN_OLUSTURULMA_TARIHI,
                    NotlarEntry.COLUMN_BITIS_TARIHI, NotlarEntry.COLUMN_YAPILDI,
                    NotlarEntry.COLUMN_KATEGORI_ID, KategoriEntry.COLUMN_KATEGORI};
            return new CursorLoader(this, NotlarEntry.CONTENT_URI, projection, selection, selectionArg, NotlarEntry._ID+" DESC");
        }

        if(id==150){

            String[] projection={KategoriEntry._ID, KategoriEntry.COLUMN_KATEGORI};
            return new CursorLoader(this, KategoriEntry.CONTENT_URI, projection, null, null, KategoriEntry._ID+" DESC");

        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 100) {
            adapter.swapCursor(data);
        }if(loader.getId()==150){
            kategoriCursorAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
        kategoriCursorAdapter.swapCursor(null);
    }
}

