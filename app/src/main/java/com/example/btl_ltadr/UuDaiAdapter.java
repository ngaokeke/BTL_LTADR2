
package com.example.btl_ltadr;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UuDaiAdapter extends RecyclerView.Adapter<UuDaiAdapter.UuDaiViewHolder> {

    private List<UuDai> danhSachUuDai;

    public UuDaiAdapter(List<UuDai> danhSachUuDai) {
        this.danhSachUuDai = danhSachUuDai;
    }

    public static class UuDaiViewHolder extends RecyclerView.ViewHolder {

        TextView txtMaUuDai;
        TextView txtTenUuDai;
        TextView txtGiamGia;
        TextView txtMoTaUuDai;
        TextView btnDungNgay;

        public UuDaiViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMaUuDai = itemView.findViewById(R.id.txtMaUuDai);
            txtTenUuDai = itemView.findViewById(R.id.txtTenUuDai);
            txtGiamGia = itemView.findViewById(R.id.txtGiamGia);
            txtMoTaUuDai = itemView.findViewById(R.id.txtMoTaUuDai);
            btnDungNgay = itemView.findViewById(R.id.btnDungNgay);
        }
    }

    @NonNull
    @Override
    public UuDaiViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_uu_dai_trang, parent, false);

        return new UuDaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull UuDaiViewHolder holder, int position) {

        UuDai uuDai = danhSachUuDai.get(position);

        holder.txtMaUuDai.setText(
                "Mã ưu đãi: " + uuDai.getMaUuDai()
        );

        holder.txtTenUuDai.setText(
                "Tên ưu đãi: " + uuDai.getTenUuDai()
        );

        holder.txtGiamGia.setText(
                "Giảm: " + uuDai.getPhanTramGiam() + "%"
        );

        holder.txtMoTaUuDai.setText(uuDai.getMoTa());

        // NUT DUNG NGAY
        holder.btnDungNgay.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    ThucDonActivity.class
            );

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachUuDai.size();
    }

    public void capNhatDanhSach(List<UuDai> danhSachMoi) {
        danhSachUuDai.clear();
        danhSachUuDai.addAll(danhSachMoi);
        notifyDataSetChanged();
    }
}
