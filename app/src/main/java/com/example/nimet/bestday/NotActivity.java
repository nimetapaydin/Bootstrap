package com.example.nimet.bestday;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nimet.bestday.data.BestdayContract.KategoriEntry;
import com.example.nimet.bestday.data.BestdayContract.NotlarEntry;
import com.example.nimet.bestday.data.BestdayQueryHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotActivity extends AppCompatActivity {

    EditText etNot, etBaslamaTarihi, etBitisTarihi;
    Button btnSil;
    CheckBox cbYapildi;
    String notID, notIcerik, notOlusturulma, notBitis, notYapildi, notKategoriID, notKategoriAdi;
    int yil, ay, gun;
    Spinner kategoriSpinner;
    SimpleCursorAdapter kategoriAdapter;
    Cursor kategoriCursor;
    String yeniMi;
    Calendar takvim;
    BestdayQueryHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        handler=new BestdayQueryHandler(this.getContentResolver());

        etNot= (EditText) findViewById(R.id.etNot);
        etBaslamaTarihi= (EditText) findViewById(R.id.txtDate);
        etBitisTarihi= (EditText) findViewById(R.id.txtDateFinish);
        cbYapildi= (CheckBox) findViewById(R.id.checkBox);
        kategoriSpinner= (Spinner) findViewById(R.id.spinnerKat);
        btnSil= (Button) findViewById(R.id.btnNotuSil);



        String[] projection={KategoriEntry._ID, KategoriEntry.COLUMN_KATEGORI};
        kategoriCursor=getContentResolver().query(KategoriEntry.CONTENT_URI,projection, null, null,null);
        kategoriAdapter=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, kategoriCursor,
                new String[]{"kategori"}, new int[]{android.R.id.text1}, 0 );
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategoriSpinner.setAdapter(kategoriAdapter);


        Intent gelenNot=getIntent();

        yeniMi=gelenNot.getStringExtra(NotlarEntry._ID);

        if(yeniMi!=null){

            notID = gelenNot.getStringExtra(NotlarEntry._ID);
            notIcerik = gelenNot.getStringExtra(NotlarEntry.COLUMN_NOT_ICERIK);
            notOlusturulma = gelenNot.getStringExtra(NotlarEntry.COLUMN_OLUSTURULMA_TARIHI);
            notBitis = gelenNot.getStringExtra(NotlarEntry.COLUMN_BITIS_TARIHI);
            notYapildi = gelenNot.getStringExtra(NotlarEntry.COLUMN_YAPILDI);
            notKategoriID = gelenNot.getStringExtra(NotlarEntry.COLUMN_KATEGORI_ID);
            notKategoriAdi=gelenNot.getStringExtra(KategoriEntry.COLUMN_KATEGORI);


            etNot.setText(notIcerik);
            etBaslamaTarihi.setText(notOlusturulma);
            etBitisTarihi.setText(notBitis);
            if(notYapildi.equals("1")){
                cbYapildi.setChecked(true);
            }else cbYapildi.setChecked(false);
            kategoriSpinner.setSelection(Integer.parseInt(notKategoriID)-1);


        }else{

            btnSil.setVisibility(View.GONE);
            etNot.setHint("Yeni notunuzu giriniz");

        }

        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fabKaydet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues yeniKayit=new ContentValues();
                //yeni bir not ekleniyor
                yeniKayit.put(NotlarEntry.COLUMN_NOT_ICERIK, etNot.getText().toString());
                yeniKayit.put(NotlarEntry.COLUMN_KATEGORI_ID, kategoriSpinner.getSelectedItemId());
                yeniKayit.put(NotlarEntry.COLUMN_OLUSTURULMA_TARIHI, etBaslamaTarihi.getText().toString());
                yeniKayit.put(NotlarEntry.COLUMN_BITIS_TARIHI, etBitisTarihi.getText().toString());
                yeniKayit.put(NotlarEntry.COLUMN_YAPILDI, cbYapildi.isChecked() ? 1 : 0);

                if(yeniMi==null){

                    handler.startInsert(1, null, NotlarEntry.CONTENT_URI,yeniKayit);
                    Toast.makeText(getBaseContext(), "Not eklendi", Toast.LENGTH_SHORT).show();

                }else{
                    //var olan bir not güncelleniyor
                    String where=NotlarEntry._ID+"=?";
                    String[] args={notID};

                    handler.startUpdate(1, null, NotlarEntry.CONTENT_URI, yeniKayit, where, args);
                    Toast.makeText(getBaseContext(), "Not güncellendi", Toast.LENGTH_SHORT).show();

                }


            }
        });






    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void baslamaTarihiSec(View view) {

        takvimGoster();

    }

    private void takvimGoster(){

        takvim=Calendar.getInstance();
        yil=takvim.get(Calendar.YEAR);
        ay=takvim.get(Calendar.MONTH);
        gun=takvim.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog tarihSec=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy", new Locale("tr"));
                etBaslamaTarihi.setText(gun+"-"+ay+"-"+yil);
                etBitisTarihi.setText(gun+"-"+ay+"-"+yil);

            }
        },yil,ay,gun);

        tarihSec.show();


    }

    public void notSil(View view) {
        String where=NotlarEntry._ID+"=?";
        String[] args={notID};


        handler.startDelete(1, null, NotlarEntry.CONTENT_URI, where, args);
        Toast.makeText(getBaseContext(), "Not slilindi", Toast.LENGTH_SHORT).show();



    }
}
