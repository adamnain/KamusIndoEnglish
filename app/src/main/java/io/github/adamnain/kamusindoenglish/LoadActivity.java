package io.github.adamnain.kamusindoenglish;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.github.adamnain.kamusindoenglish.database.KamusHelper;
import io.github.adamnain.kamusindoenglish.model.KamusModel;
import io.github.adamnain.kamusindoenglish.prefs.AppPreference;

public class LoadActivity extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        new LoadData().execute();
    }


    /*
    Asynctask untuk menjalankan preload data
     */
    private class LoadData extends AsyncTask<Void, Integer, Void> {
        final String TAG = LoadData.class.getSimpleName();
        KamusHelper kamusHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;

        /*
        Persiapan sebelum proses dimulai
        Berjalan di Main Thread
         */
        @Override
        protected void onPreExecute() {

            kamusHelper = new KamusHelper(LoadActivity.this);
            appPreference = new AppPreference(LoadActivity.this);
        }

        /*
        Proses background terjadi di method doInBackground
         */
        @Override
        protected Void doInBackground(Void... params) {

            /*
            Panggil preference first run
             */
            Boolean firstRun = appPreference.getFirstRun();

            /*
            Jika first run true maka melakukan proses pre load,
            jika first run false maka akan langsung menuju home
             */
            if (firstRun) {
                /*
                Load raw data dari file txt ke dalam array model mahasiswa
                 */
                ArrayList<KamusModel> mahasiswaModels = preLoadRaw();

                kamusHelper.open();

                progress = 30;
                publishProgress((int) progress);
                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size();


                /*
                Gunakan ini untuk query insert yang transactional
                 */
                kamusHelper.beginTransaction();

                try {
                    for (KamusModel model : mahasiswaModels) {
                        kamusHelper.insertTransaction(model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }
                    // Jika semua proses telah di set success maka akan di commit ke database
                    kamusHelper.setTransactionSuccess();
                } catch (Exception e) {
                    // Jika gagal maka do nothing
                    Log.e(TAG, "doInBackground: Exception");
                }
                kamusHelper.endTransaction();

                /*
                Gunakan ini untuk insert query dengan menggunakan standar query
                 */
//                for (MahasiswaModel model : mahasiswaModels) {
//                    mahasiswaHelper.insert(model);
//                    progress += progressDiff;
//                    publishProgress((int)progress);
//                }

                // Close helper ketika proses query sudah selesai
                kamusHelper.close();

                /*
                Set preference first run ke false
                Agar proses preload tidak dijalankan untuk kedua kalinya
                */
                appPreference.setFirstRun(false);

                publishProgress((int) maxprogress);

            } else {
                try {
                    synchronized (this) {
                        this.wait(2000);

                        publishProgress(50);

                        this.wait(2000);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        //Update prosesnya
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        /*
        Setelah proses selesai
        Berjalan di Main Thread
        */
        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }

    /**
     * Parsing raw data text berupa data menjadi array mahasiswa
     *
     * @return array model dari semua mahasiswa
     */
    public ArrayList<KamusModel> preLoadRaw() {
        ArrayList<KamusModel> mahasiswaModels = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(R.raw.english_indonesia);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                KamusModel mahasiswaModel;

                mahasiswaModel = new KamusModel(splitstr[0], splitstr[1]);
                mahasiswaModels.add(mahasiswaModel);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mahasiswaModels;
    }
}
