package com.example.nimet.bestday;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.nimet.bestday.data.BestdayContract;



public class NotlarCursorAdapter extends CursorAdapter {


    public NotlarCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.notlar_tek_satir, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView not= (TextView) view.findViewById(R.id.tvNot);
        int notColumnIndex=cursor.getColumnIndex(BestdayContract.NotlarEntry.COLUMN_NOT_ICERIK);
        String notIcerigi=cursor.getString(notColumnIndex);
        not.setText(notIcerigi);
    }
}
