package com.ggpc.spkpengamatan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ggpc.spkpengamatan.Model.TK;
import com.ggpc.spkpengamatan.R;

import java.util.ArrayList;
import java.util.List;


public class SelectTKAdapter extends RecyclerView.Adapter<SelectTKAdapter.ViewHolder> {

    Context context;
    static ArrayList<TK> tkselect = new ArrayList<>();

    public SelectTKAdapter(){

    }

    public SelectTKAdapter(Context context, ArrayList<TK> tkselect) {
        this.context = context;
        this.tkselect = tkselect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listselecttk, parent, false);
        return new SelectTKAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TK m_tkSelect = tkselect.get(position);

        holder.namatkselect.setText(m_tkSelect.getNama());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                tkselect.get(position).setValue(tkselect.get(position).getKit());
                tkselect.get(position).setName(tkselect.get(position).getNama());
//                Toast.makeText(context,"berhasil ",Toast.LENGTH_SHORT).show();
            } else {
                tkselect.get(position).setValue(null);
                tkselect.get(position).setName(null);
//                Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static ArrayList<TK> getTkselect() {
        return tkselect;
    }

    @Override
    public int getItemCount() {
        return tkselect.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView namatkselect;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namatkselect = itemView.findViewById(R.id.NamaTKSelect);
            checkBox = itemView.findViewById(R.id.checkboxTK);


        }
    }


}
