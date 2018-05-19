package com.example.nimet.bestday.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class BestdayProvider extends ContentProvider {

    private static final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);

    private static final int URICODE_NOTLAR=1;
    private static final int URICODE_KATEGORILER=2;


    static {
        matcher.addURI(BestdayContract.CONTENT_AUTHORITY, BestdayContract.PATH_NOTLAR, URICODE_NOTLAR);
        matcher.addURI(BestdayContract.CONTENT_AUTHORITY, BestdayContract.PATH_KATEGORILER, URICODE_KATEGORILER);
    }

    private SQLiteDatabase db;
    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        helper=new DatabaseHelper(getContext());
        db=helper.getWritableDatabase();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        SQLiteQueryBuilder builder;
        String tabloBirlestir="notlar inner join kategoriler on notlar.kategoriID = kategoriler._id";

        switch (matcher.match(uri)){

            case URICODE_NOTLAR:
                builder=new SQLiteQueryBuilder();
                builder.setTables(tabloBirlestir);
                cursor=  builder.query(db, projection,selection,selectionArgs,null,null,null);
                break;


            case URICODE_KATEGORILER:
                cursor=db.query(BestdayContract.KategoriEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("BILINMEYEN QUERY URI"+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        switch (matcher.match(uri)){

            case URICODE_NOTLAR:
                return kayitEkle(uri, values, BestdayContract.NotlarEntry.TABLE_NAME);

            case URICODE_KATEGORILER:
                return kayitEkle(uri, values, BestdayContract.KategoriEntry.TABLE_NAME);

            default:
                throw new IllegalArgumentException("INSERT BILINMEYEN URI:"+uri);

        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        //Toast.makeText(getContext(), "Eslesen kod:"+matcher.match(uri)+" Link:"+uri, Toast.LENGTH_LONG).show();

        switch (matcher.match(uri)){

            case URICODE_NOTLAR:
                return kayitSil(uri, selection, selectionArgs,BestdayContract.NotlarEntry.TABLE_NAME);

            case URICODE_KATEGORILER:
                return kayitSil(uri, selection, selectionArgs,BestdayContract.KategoriEntry.TABLE_NAME);


            default:
                throw new IllegalArgumentException("BILINMEYEN DELETE URI"+uri);

        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (matcher.match(uri)){

            case URICODE_NOTLAR:
                return kayitGuncelle(uri,values,selection,selectionArgs, BestdayContract.NotlarEntry.TABLE_NAME);

            case URICODE_KATEGORILER:
                return kayitGuncelle(uri,values,selection,selectionArgs, BestdayContract.KategoriEntry.TABLE_NAME);

            default:
                throw new IllegalArgumentException("BILINMEYEN UPDATE URI"+uri);


        }
    }

    private Uri kayitEkle(Uri uri, ContentValues values, String tabloAdi){

        long id= db.insert(tabloAdi, null, values);
        if(id==-1){
            Log.e("NotDefterimProvider", "INSERT HATA"+uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private int kayitGuncelle(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tabloAdi){

        int id= db.update(tabloAdi, values, selection,selectionArgs);
        if(id==0)
        {
            Log.e("HATA", "UPDATE HATA"+uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;

    }

    private int kayitSil(Uri uri, String selection, String[] selectionArgs, String tabloAdi){

        int id= db.delete(tabloAdi, selection,selectionArgs);
        if(id==0)
        {
            Log.e("HATA", "UPDATE HATA"+uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;

    }


}