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
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.ggpc.spkpengamatan.Model.SPK_SRV;
import com.ggpc.spkpengamatan.Model.TK;
import com.ggpc.spkpengamatan.R;
import com.ggpc.spkpengamatan.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySRVAdapter extends RecyclerView.Adapter<ActivitySRVAdapter.ListViewHolder> {

    private final Context context;
    private List<SPK_SRV> list;
    private ArrayList<String> arrayTK = new ArrayList<>();
    private static ArrayList<String> kitTK = new ArrayList<>();
    private static ArrayList<String> itemSPK = new ArrayList<>();
    private static ArrayList<String> namaTK = new ArrayList<>();
    private static TK tk;
    private SPK_SRV spk_real;
    ArrayList<String> name_tk = new ArrayList<String>();
    private String[] tk_nama;
    private String[] tk_kit;
    //private static String index_mandor, nama_tk, kit_tk;
    private String name_tk_sp;

    public ActivitySRVAdapter(Context context, List<SPK_SRV> list, String[] tk_nama, ArrayList<String> arrayTK) {
        this.context = context;
        this.list = list;
        this.tk_nama = tk_nama;
        this.arrayTK = arrayTK;
        //getTK();
        //getProfileTK();
    }

    public ActivitySRVAdapter(Context context, List<SPK_SRV> list) {
        this.context = context;
        this.list = list;
    }

    public ActivitySRVAdapter(Context context, String[] tk_nama, String[] tk_kit){
        this.context = context;
        this.tk_nama = tk_nama;
        this.tk_kit = tk_kit;
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
        //index_mandor = spk_real.getMandor_Operator();
        kitTK.clear();
        namaTK.clear();
        itemSPK.clear();

        holder.tvMandor.setText(spk_real.getMandor_Operator());
        holder.tvActivity.setText(spk_real.getDeskripsi());
        holder.tvLocation.setText(spk_real.getLokasi());

        if (!list.get(holder.getAbsoluteAdapterPosition()).getPengamat().equals("NULL")){
            holder.tvObserver.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.VISIBLE);
            name_tk_sp = list.get(holder.getAbsoluteAdapterPosition()).getNama_Pengamat();
        } else {
            name_tk_sp = list.get(holder.getAbsoluteAdapterPosition()).getNama_Pengamat();
            holder.tvObserver.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_spinner,tk_nama);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(0, false);
        holder.spinner.setBackgroundColor(R.drawable.bg_edit_text_white);

        if (name_tk_sp!=null){
            holder.spinner.setSelection(adapter.getPosition(list.get(holder.getAbsoluteAdapterPosition()).getNama_Pengamat()), false);
        }

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context,arrayTK.get(position) + " _ " + list.get(holder.getAbsoluteAdapterPosition()).getID(),Toast.LENGTH_SHORT).show();
                //sendObserver(list.get(holder.getAbsoluteAdapterPosition()).getItem(), spk_real.getSPK_ID(), arrayTK.get(position), tk_nama[position]);

                /*kitTK.remove(arrayTK.get(position));
                namaTK.remove(tk_nama[position]);
                itemSPK.add(list.get(holder.getAbsoluteAdapterPosition()).getItem());
                kitTK.add(arrayTK.get(position));
                namaTK.add(tk_nama[position]);*/

                if (itemSPK.contains(list.get(holder.getAbsoluteAdapterPosition()).getItem())){
                    /*kitTK.remove(arrayTK.get(position));
                    namaTK.remove(tk_nama[position]);
                    itemSPK.add(list.get(holder.getAbsoluteAdapterPosition()).getItem());
                    kitTK.add(arrayTK.get(position));
                    namaTK.add(tk_nama[position]);*/
                    kitTK.remove(itemSPK.indexOf(list.get(holder.getAbsoluteAdapterPosition()).getItem()));
                    namaTK.remove(itemSPK.indexOf(list.get(holder.getAbsoluteAdapterPosition()).getItem()));
                } else {
                    itemSPK.add(list.get(holder.getAbsoluteAdapterPosition()).getItem());
                }
                kitTK.add(itemSPK.indexOf(list.get(holder.getAbsoluteAdapterPosition()).getItem()), arrayTK.get(position));
                namaTK.add(itemSPK.indexOf(list.get(holder.getAbsoluteAdapterPosition()).getItem()), tk_nama[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*if (spk_real.getPengamat().equals("0")){
            holder.tvObserver.setText(R.string.observer_null);
        } else {
            holder.tvObserver.setText(spk_real.getNama_Pengamat());
            //Toast.makeText(context, "Hasil : " + getTk().getNama(), Toast.LENGTH_SHORT).show();
        }
        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));*/
        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));

    }

    public static ArrayList<String> getKitTK() {
        return kitTK;
    }

    public static ArrayList<String> getNamaTK() {
        return namaTK;
    }

    public static ArrayList<String> getItemSPK() {
        return itemSPK;
    }

    private void sendObserver(String item, String spk_id, String tk_kit, String tk_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_OBSERVER_SRV,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(context, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            sendNotifTK(tk_kit);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Item_No", item);
                params.put("SPK_ID", spk_id);
                params.put("Kasie", SharedPrefManager.getInstance(context).getDataMandor().getKasieIndex());
                params.put("Pengamat", tk_kit);
                params.put("Nama_Pengamat", tk_name);
                params.put("SPK_Status", "NEW");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void sendNotifTK(String kit_tk) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIF_TK,
                response -> {
                    Toast.makeText(context, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    //finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", "SPK Hari ini");
                params.put("message", spk_real.getDeskripsi());
                params.put("index", kit_tk);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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
