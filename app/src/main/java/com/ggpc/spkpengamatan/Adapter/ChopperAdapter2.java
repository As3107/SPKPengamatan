package com.ggpc.spkpengamatan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggpc.spkpengamatan.Model.Chopper;
import com.ggpc.spkpengamatan.R;

import java.text.DecimalFormat;
import java.util.List;

public class ChopperAdapter2 extends RecyclerView.Adapter<ChopperAdapter2.ListViewHolder> {

    private Context context;
    private List<Chopper> list;
    double bt, th, ar, total;

    public ChopperAdapter2(Context context, List<Chopper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chopper, parent, false);
        return new ChopperAdapter2.ListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Chopper chopper = list.get(position);

        if (position%2==0){
            holder.tvPlot.setBackgroundResource(R.color.dark_green);
            holder.tvBt.setBackgroundResource(R.color.dark_green);
            holder.tvTh.setBackgroundResource(R.color.dark_green);
            holder.tvAr.setBackgroundResource(R.color.dark_green);
            holder.tvTotal.setBackgroundResource(R.color.dark_green);
        }

        holder.tvPlot.setText(chopper.getPlot());
        holder.tvBt.setText(chopper.getBt());
        holder.tvTh.setText(chopper.getTh());
        holder.tvAr.setText(chopper.getAr());
        //holder.tvTotal.setText(chopper.getTotal());

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlot, tvBt, tvTh, tvAr, tvTotal;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlot = itemView.findViewById(R.id.tv_plot);
            tvBt = itemView.findViewById(R.id.tv_bt);
            tvTh = itemView.findViewById(R.id.tv_th);
            tvAr = itemView.findViewById(R.id.tv_ar);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }
    }

    private ChopperAdapter2.OnItemClickCallback onItemClickCallback;

    public void onItemClickCallback (ChopperAdapter2.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Chopper chopper);
    }

    public interface dataChopper{
        void chopper(Chopper chopper);
    }
}
