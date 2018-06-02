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
    public ArrayList<KamusModel> getDataByKata(String kata){
        String result = "";
        //Cursor cursor = database.query(TABLE_ENGLISH,null,KATA+" LIKE ?",new String[]{kata},null,null,"id" + " ASC",null);
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_ENGLISH+" WHERE KATA LIKE '"+kata+"%'", null);
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
    public ArrayList<KamusModel> getAllData(){
        Cursor cursor = database.query(TABLE_ENGLISH,null,null,null,null,null,"id"+ " ASC",null);
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

    /**
     * Gunakan method ini untuk query insert di dalam transaction
     * @param mahasiswaModel inputan model mahasiswa
     */
    public void insertTransaction(KamusModel kamusModel){
        String sql = "INSERT INTO "+TABLE_ENGLISH+" ("+KATA+", "+ARTI
                +") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, kamusModel.getKata());
        stmt.bindString(2, kamusModel.getArti());
        stmt.execute();
        stmt.clearBindings();

    }

    /**
     * Gunakan method ini untuk update data mahasiswa yang ada di dalam database
     * @param mahasiswaModel inputan model mahasiswa
     * @return jumlah mahasiswa yang ter-update
     */
    public int update(KamusModel kamusModel){
        ContentValues args = new ContentValues();
        args.put(KATA, kamusModel.getKata());
        args.put(ARTI, kamusModel.getArti());
        return database.update(TABLE_ENGLISH, args, _ID + "= '" + kamusModel.getId() + "'", null);
    }


    /**
     * Gunakan method ini untuk menghapus data mahasiswa yang ada di dalam database
     * @param id id mahasiswa yang akan di hapus
     * @return jumlah mahasiswa yang di-delete
     */
    public int delete(int id){
        return database.delete(TABLE_ENGLISH, _ID + " = '"+id+"'", null);
    }
}
