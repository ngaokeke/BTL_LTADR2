package com.example.btl_ltadr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UuDaiActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachUuDai;
    private UuDaiAdapter uuDaiAdapter;
    private ArrayList<UuDai> danhSachUuDai;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uu_dai);

        databaseHelper = new DatabaseHelper(this);

        anhXa();
        khoiTaoDanhSachUuDai();
        cauHinhRecyclerView();
        xuLyThanhDieuHuong();
    }

    private void anhXa() {
        rvDanhSachUuDai = findViewById(R.id.rvDanhSachUuDai);
    }

    // Thêm dữ liệu mẫu vào SQLite nếu chưa có.
    private void khoiTaoDanhSachUuDai() {

        databaseHelper.themUuDai(new UuDai(
                "FOODIE20",
                "Giảm giá đặc biệt",
                "Áp dụng cho đơn hàng đủ điều kiện",
                20,
                0
        ));

        databaseHelper.themUuDai(new UuDai(
                "FOODIE30",
                "Ưu đãi thành viên",
                "Áp dụng cho đơn hàng từ 200.000đ",
                30,
                0
        ));

        databaseHelper.themUuDai(new UuDai(
                "COMBO15",
                "Ưu đãi combo",
                "Áp dụng khi mua các combo",
                15,
                0
        ));

        // Đọc danh sách từ SQLite.
        danhSachUuDai = databaseHelper.layDanhSachUuDai();
    }

    private void cauHinhRecyclerView() {

        rvDanhSachUuDai.setLayoutManager(
                new LinearLayoutManager(this)
        );

        uuDaiAdapter = new UuDaiAdapter(danhSachUuDai);
        rvDanhSachUuDai.setAdapter(uuDaiAdapter);
    }

    private void xuLyThanhDieuHuong() {

        View navTrangChu = findViewById(R.id.navTrangChu);
        View navThucDon = findViewById(R.id.navThucDon);
        View navGioHang = findViewById(R.id.navGioHang);
        View navDonHang = findViewById(R.id.navDonHang);
        View navTaiKhoan = findViewById(R.id.navTaiKhoan);

        if (navTrangChu != null) {
            navTrangChu.setOnClickListener(v ->
                    chuyenManHinh(TrangChuActivity.class)
            );
        }

        if (navThucDon != null) {
            navThucDon.setOnClickListener(v ->
                    chuyenManHinh(ThucDonActivity.class)
            );
        }

        if (navGioHang != null) {
            navGioHang.setOnClickListener(v ->
                    chuyenManHinh(GioHangActivity.class)
            );
        }

        if (navDonHang != null) {
            navDonHang.setOnClickListener(v ->
                    chuyenManHinh(DonHangActivity.class)
            );
        }

        if (navTaiKhoan != null) {
            navTaiKhoan.setOnClickListener(v ->
                    chuyenManHinh(TaiKhoanActivity.class)
            );
        }
    }

    private void chuyenManHinh(Class<?> activity) {
        Intent intent = new Intent(UuDaiActivity.this, activity);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
