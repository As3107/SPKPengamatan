package com.ggpc.spkpengamatan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggpc.spkpengamatan.Model.SPK;
import com.ggpc.spkpengamatan.R;

import java.util.List;

public class SPKAdapter extends RecyclerView.Adapter<SPKAdapter.ListViewHolder> {

    private Context context;
    private List<SPK> list;
    int sum;

    public SPKAdapter(Context context, List<SPK> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_spk, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        SPK spk = list.get(position);

        holder.tvNoSPK.setText(spk.getNo_spk());
        //holder.tvObservation.setText(spk.getPengamatan());
        holder.tvLocation.setText(spk.getLokasi());
        //holder.tvRegion.setText(spk.getWilayah());
        //holder.ivAgree.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvNoSPK, tvObservation, tvLocation, tvRegion;
        ImageView ivAgree;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNoSPK = itemView.findViewById(R.id.tv_no_spk);
            //tvObservation = itemView.findViewById(R.id.tv_observation);
            tvLocation = itemView.findViewById(R.id.tv_location);
            //tvRegion = itemView.findViewById(R.id.tv_region);
            //ivAgree = itemView.findViewById(R.id.iv_agree);
        }
    }

    private OnItemClickCallback onItemClickCallback;

    public void onItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(SPK spk);
    }

}
