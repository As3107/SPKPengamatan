package com.ggpc.spkpengamatan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.EndPoints;
import com.ggpc.spkpengamatan.Model.SPK_SRV;
import com.ggpc.spkpengamatan.Model.TK;
import com.ggpc.spkpengamatan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySRVAdapter2 extends RecyclerView.Adapter<ActivitySRVAdapter2.ListViewHolder> {

    private final Context context;
    private List<SPK_SRV> list;
    private ArrayList<String> arrayTK = new ArrayList<>();
    private static TK tk;
    private SPK_SRV spk_real;
    ArrayList<String> name_tk = new ArrayList<String>();
    private String[] tk_nama;
    private String[] tk_kit;
    //private static String index_mandor, nama_tk, kit_tk;
    private String name_tk_sp;

    public ActivitySRVAdapter2(Context context, List<SPK_SRV> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity2, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        spk_real = list.get(position);

        holder.tvMandor.setText(spk_real.getMandor_Operator());
        holder.tvActivity.setText(spk_real.getDeskripsi());
        holder.tvLocation.setText(spk_real.getLokasi());

        holder.tvObserver.setVisibility(View.VISIBLE);
        holder.spinner.setVisibility(View.GONE);
        holder.tvObserver.setText(spk_real.getNama_Pengamat());

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvMandor, tvActivity, tvLocation, tvObserver;
        Spinner spinner;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMandor = itemView.findViewById(R.id.tv_mandor);
            tvActivity = itemView.findViewById(R.id.tv_activity);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvObserver = itemView.findViewById(R.id.tv_observer);
            spinner = itemView.findViewById(R.id.sp_list_tk);
        }
    }

    private OnItemClickCallback onItemClickCallback;

    public void onItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(SPK_SRV spk);
    }

}
