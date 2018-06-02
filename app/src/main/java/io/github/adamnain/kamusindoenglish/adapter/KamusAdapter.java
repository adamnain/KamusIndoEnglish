package io.github.adamnain.kamusindoenglish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.adamnain.kamusindoenglish.R;
import io.github.adamnain.kamusindoenglish.model.KamusModel;

import static android.text.method.TextKeyListener.clear;

public class KamusAdapter extends RecyclerView.Adapter<KamusAdapter.KamusHolder>{
    private ArrayList<KamusModel> mData = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;

    public void replaceAll(ArrayList<KamusModel> items) {
        mData = items;
        notifyDataSetChanged();
    }

    public KamusAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public KamusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kamus_row, parent, false);
        return new KamusHolder(view);
    }

    public void addItem(ArrayList<KamusModel> mData) {

        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(KamusHolder holder, int position) {
        holder.textViewKata.setText(mData.get(position).getKata());
        holder.textViewArti.setText(mData.get(position).getArti());

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class KamusHolder extends RecyclerView.ViewHolder {
        private TextView textViewKata;
        private TextView textViewArti;

        public KamusHolder(View itemView) {
            super(itemView);

            textViewKata = (TextView) itemView.findViewById(R.id.txt_kata);
            textViewArti = (TextView) itemView.findViewById(R.id.txt_arti);
        }

    }
}
