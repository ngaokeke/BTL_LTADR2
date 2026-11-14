package com.example.btl_ltadr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UuDaiAdapter extends RecyclerView.Adapter<UuDaiAdapter.UuDaiViewHolder> {

    private List<UuDai> danhSachUuDai;

    public UuDaiAdapter(List<UuDai> danhSachUuDai) {
        this.danhSachUuDai = danhSachUuDai;
    }

    public class UuDaiViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUuDai;
        TextView txtTenUuDai;

        public UuDaiViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUuDai = itemView.findViewById(R.id.imgUuDai);
            txtTenUuDai = itemView.findViewById(R.id.txtTenUuDai);
        }
    }

    @NonNull
    @Override
    public UuDaiViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_uudai, parent, false);

        return new UuDaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull UuDaiViewHolder holder, int position) {

        UuDai uuDai = danhSachUuDai.get(position);

        holder.txtTenUuDai.setText(uuDai.getTenUuDai());

        holder.imgUuDai.setImageResource(uuDai.getHinhAnh());
    }

    @Override
    public int getItemCount() {
        return danhSachUuDai.size();
    }
}