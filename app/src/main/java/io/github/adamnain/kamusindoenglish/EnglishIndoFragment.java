package io.github.adamnain.kamusindoenglish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.adamnain.kamusindoenglish.adapter.KamusAdapter;
import io.github.adamnain.kamusindoenglish.database.KamusHelper;
import io.github.adamnain.kamusindoenglish.model.KamusModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnglishIndoFragment extends Fragment  {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.svEnglish)
    SearchView svEnglish;

    View v;
    KamusAdapter kamusAdapter;
    KamusHelper kamusHelper;

    public EnglishIndoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_english_indo, container, false);

        ButterKnife.bind(this, v);

        kamusHelper = new KamusHelper(getActivity());
        kamusAdapter = new KamusAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(kamusAdapter);

        //svEnglishIndo.setOnSearchActionListener(this);
        svEnglish.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                kamusHelper.open();
                ArrayList<KamusModel> list = kamusHelper.getDataByKata(String.valueOf(query), true);
                kamusHelper.close();

                kamusAdapter.replaceAll(list);
                //recyclerView.setAdapter(kamusAdapter);
                //kamusAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        kamusHelper.open();

        // Ambil semua data mahasiswa di database
        ArrayList<KamusModel> mahasiswaModels = kamusHelper.getAllData(true);

        kamusHelper.close();

        kamusAdapter.addItem(mahasiswaModels);

        return v;
    }

}
