package com.example.btl_ltadr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class MonAnAdapter extends RecyclerView.Adapter<MonAnAdapter.MonAnViewHolder> {

    private List<MonAn> danhSachMonAn;
    private OnMonAnClickListener suKienClick;

    public interface OnMonAnClickListener {
        void onClick(MonAn monAn);
    }

    public MonAnAdapter(List<MonAn> danhSachMonAn,
                        OnMonAnClickListener suKienClick) {
        this.danhSachMonAn = danhSachMonAn;
        this.suKienClick = suKienClick;
    }

    public class MonAnViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMonAn;
        TextView txtTenMonAn;
        TextView txtGiaMonAn;
        TextView txtGiaCu;
        TextView btnCong;

        public MonAnViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMonAn = itemView.findViewById(R.id.imgMonAn);
            txtTenMonAn = itemView.findViewById(R.id.txtTenMonAn);
            txtGiaMonAn = itemView.findViewById(R.id.txtGiaMonAn);
            txtGiaCu = itemView.findViewById(R.id.txtGiaCu);
            btnCong = itemView.findViewById(R.id.btnCong);
        }
    }

    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_monan, parent, false);

        return new MonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MonAnViewHolder holder, int position) {

        MonAn monAn = danhSachMonAn.get(position);

        DecimalFormat dinhDangTien = new DecimalFormat("#,###");

        holder.txtTenMonAn.setText(monAn.getTenMonAn());

        holder.txtGiaMonAn.setText(
                dinhDangTien.format(monAn.getGiaMonAn()) + " đ"
        );

        holder.txtGiaCu.setText(
                dinhDangTien.format(monAn.getGiaCu()) + " đ"
        );

        holder.imgMonAn.setImageResource(monAn.getHinhAnh());



        int soLuongTrongGio = GioHang.laySoLuong(
                monAn.getMaMonAn()
        );

        if (soLuongTrongGio > 0) {

            holder.btnCong.setText(
                    String.valueOf(soLuongTrongGio)
            );

            holder.btnCong.setBackgroundResource(
                    R.drawable.bg_vong_tron_so_luong
            );

            holder.btnCong.setTextColor(Color.BLACK);

        } else {

            holder.btnCong.setText("+");

            holder.btnCong.setBackgroundResource(
                    R.drawable.bg_nut_cong
            );


        }

        holder.btnCong.setGravity(Gravity.CENTER);


        // Sự kiện nhấn nút +
        holder.btnCong.setOnClickListener(v -> {

            SharedPreferences session = v.getContext().getSharedPreferences(
                    "FOODIE_SESSION",
                    android.content.Context.MODE_PRIVATE
            );

            boolean daDangNhap = session.getBoolean(
                    "DANG_DANG_NHAP",
                    false
            );

            if (!daDangNhap) {
                Toast.makeText(
                        v.getContext(),
                        "Vui lòng đăng nhập để thêm món vào giỏ hàng!",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        v.getContext(),
                        DangNhapActivity.class
                );

                v.getContext().startActivity(intent);
                return;
            }

            if (suKienClick != null) {
                suKienClick.onClick(monAn);
            }
        });

// Nhấn vào món ăn vẫn xem chi tiết bình thường
        holder.itemView.setOnClickListener(v -> {
            if (suKienClick != null) {
                suKienClick.onClick(monAn);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (suKienClick != null) {
                suKienClick.onClick(monAn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachMonAn.size();
    }
}