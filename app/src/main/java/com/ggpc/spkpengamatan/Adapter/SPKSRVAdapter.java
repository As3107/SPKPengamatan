package com.ggpc.spkpengamatan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggpc.spkpengamatan.Model.SPK_HEADER_SRV;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.ggpc.spkpengamatan.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SPKSRVAdapter extends RecyclerView.Adapter<SPKSRVAdapter.ListViewHolder> {

    private Context context;
    private List<SPK_HEADER_SRV> list;

    public SPKSRVAdapter(Context context, List<SPK_HEADER_SRV> list) {
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
        SPK_HEADER_SRV spk = list.get(position);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = spk.getTanggal_SPK();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        holder.tvNoSPK.setText(spk.getSPK_ID());
        holder.tvDate.setText(outputDateStr);

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAbsoluteAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvNoSPK, tvDate;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNoSPK = itemView.findViewById(R.id.tv_no_spk);
            tvDate = itemView.findViewById(R.id.tv_date_spk);
        }
    }

    private OnItemClickCallback onItemClickCallback;

    public void onItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(SPK_HEADER_SRV spk);
    }

}
