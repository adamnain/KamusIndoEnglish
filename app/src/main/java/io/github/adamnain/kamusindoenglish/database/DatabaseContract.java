package io.github.adamnain.kamusindoenglish.database;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_ENGLISH = "english";
    static String TABLE_INDONESIA = "indonesia";

    static final class MahasiswaColumns implements BaseColumns {

        // Mahasiswa nama
        static String KATA = "kata";
        // Mahasiswa nim
        static String ARTI = "arti";

    }
}
