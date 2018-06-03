package io.github.adamnain.kamusindoenglish.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import io.github.adamnain.kamusindoenglish.model.KamusModel;

import static android.provider.BaseColumns._ID;
import static io.github.adamnain.kamusindoenglish.database.DatabaseContract.MahasiswaColumns.ARTI;
import static io.github.adamnain.kamusindoenglish.database.DatabaseContract.MahasiswaColumns.KATA;
import static io.github.adamnain.kamusindoenglish.database.DatabaseContract.TABLE_ENGLISH;
import static io.github.adamnain.kamusindoenglish.database.DatabaseContract.TABLE_INDONESIA;

public class KamusHelper {
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public KamusHelper(Context context){
        this.context = context;
    }

    public KamusHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    /**
     * Gunakan method ini untuk cari NIM berdasarkan nama mahasiswa
     * @param nama nama yang dicari
     * @return NIM dari mahasiswa
     */
    public ArrayList<KamusModel> getDataByKata(String kata, boolean english){
        String table = "";
        if (!english){
            table = TABLE_INDONESIA;
        }
        else {
            table = TABLE_ENGLISH;
        }

        Cursor cursor = database.rawQuery("SELECT * FROM "+table+" WHERE KATA LIKE '"+kata+"%'", null);
        cursor.moveToFirst();
        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;
        if (cursor.getCount()>0) {
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                kamusModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamusModel.setArti(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));

                arrayList.add(kamusModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk mendapatkan semua data mahasiswa
     * @return hasil query mahasiswa model di dalam arraylist
     */
    public ArrayList<KamusModel> getAllData(boolean english){
        String table = "";
        if (!english){
            table = TABLE_INDONESIA;
        }
        else {
            table = TABLE_ENGLISH;
        }

        Cursor cursor = database.query(table,null,null,null,null,null,"id"+ " ASC",null);
        cursor.moveToFirst();
        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;
        if (cursor.getCount()>0) {
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                kamusModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamusModel.setArti(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));


                arrayList.add(kamusModel);
                cursor.moveToNext();


            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk query insert
     * @param mahasiswaModel inputan model mahasiswa
     * @return id dari data mahasiswa yang baru saja dimasukkan
     */
    public long insert(KamusModel mahasiswaModel){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(KATA, mahasiswaModel.getKata());
        initialValues.put(ARTI, mahasiswaModel.getArti());
        return database.insert(TABLE_ENGLISH, null, initialValues);
    }

    /**
     * Gunakan method ini untuk memulai sesi query transaction
     */
    public void beginTransaction(){
        database.beginTransaction();
    }

    /**
     * Gunakan method ini jika query transaction berhasil, jika error jangan panggil method ini
     */
    public void setTransactionSuccess(){
        database.setTransactionSuccessful();
    }

    /**
     * Gunakan method ini untuk mengakhiri sesi query transaction
     */
    public void endTransaction(){
        database.endTransaction();
    }


    public void insertTransaction(ArrayList<KamusModel> kamusModels, boolean english){
        String table = "";
        if (!english){
            table = TABLE_INDONESIA;
        }
        else{
            table = TABLE_ENGLISH;
        }


        String sql = "INSERT INTO "+table+" ("+KATA+", "+ARTI
                +") VALUES (?, ?)";

        database.beginTransaction();

        SQLiteStatement stmt = database.compileStatement(sql);
        for (int i = 0; i < kamusModels.size(); i++) {
            stmt.bindString(1, kamusModels.get(i).getKata());
            stmt.bindString(2, kamusModels.get(i).getArti());
            stmt.execute();
            stmt.clearBindings();
        }

        database.setTransactionSuccessful();
        database.endTransaction();

    }


}
