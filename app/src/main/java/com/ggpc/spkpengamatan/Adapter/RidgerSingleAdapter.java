package com.ggpc.spkpengamatan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggpc.spkpengamatan.Model.RidgerSingle;
import com.ggpc.spkpengamatan.R;

import java.util.List;

public class RidgerSingleAdapter extends RecyclerView.Adapter<RidgerSingleAdapter.ListViewHolder> {

    private Context context;
    private List<RidgerSingle> list;

    public RidgerSingleAdapter(Context context, List<RidgerSingle> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ridger_single, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        RidgerSingle ridgerSingle = list.get(position);

        if (position%2==0){
            holder.tvNo1.setBackgroundResource(R.color.dark_green);
            holder.tvPorosGulud.setBackgroundResource(R.color.dark_green);
            holder.tvNo2.setBackgroundResource(R.color.dark_green);
            holder.tvKukuRidger.setBackgroundResource(R.color.dark_green);
        }

        holder.tvNo1.setText(ridgerSingle.getNo_sampel());
        holder.tvPorosGulud.setText(ridgerSingle.getPoros_gulud());
        holder.tvNo2.setText(ridgerSingle.getNo_sampel());
        holder.tvKukuRidger.setText(ridgerSingle.getKuku_ridger());

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAbsoluteAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvNo1, tvPorosGulud, tvNo2, tvKukuRidger;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNo1 = itemView.findViewById(R.id.tv_no1);
            tvPorosGulud = itemView.findViewById(R.id.tv_poros_gulud);
            tvNo2 = itemView.findViewById(R.id.tv_no2);
            tvKukuRidger = itemView.findViewById(R.id.tv_kuku_ridger);
        }
    }

    private RidgerSingleAdapter.OnItemClickCallback onItemClickCallback;

    public void onItemClickCallback (RidgerSingleAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(RidgerSingle ridgerSingle);
    }

}
