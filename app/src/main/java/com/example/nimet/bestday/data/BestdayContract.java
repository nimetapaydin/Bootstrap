package com.example.nimet.bestday.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BestdayContract {

    public static final String CONTENT_AUTHORITY="com.example.nimet.bestday.bestdayprovider";
    public static final String PATH_NOTLAR="notlar";
    public static final String PATH_KATEGORILER="kategoriler";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" +CONTENT_AUTHORITY);



    public static final class NotlarEntry implements BaseColumns {

        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTLAR);


        public static final String TABLE_NAME = "notlar";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NOT_ICERIK = "notIcerik";
        public static final String COLUMN_OLUSTURULMA_TARIHI = "olusturulmaTarihi";
        public static final String COLUMN_BITIS_TARIHI = "bitisTarihi";
        public static final String COLUMN_YAPILDI = "yapildi";
        public static final String COLUMN_KATEGORI_ID = "kategoriID";
    }

    public static final class KategoriEntry implements BaseColumns {

        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, PATH_KATEGORILER);


        public static final String TABLE_NAME = "kategoriler";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_KATEGORI = "kategori";
    }

}
