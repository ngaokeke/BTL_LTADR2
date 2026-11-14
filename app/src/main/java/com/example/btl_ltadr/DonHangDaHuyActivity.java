
package com.example.btl_ltadr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DonHangDaHuyActivity extends AppCompatActivity {

    private TextView txtMaDon;
    private TextView txtTongThanhToan;
    private TextView txtPhuongThuc;
    private TextView txtDiaChi;
    private TextView txtCuaHang;
    private TextView txtNguoiNhan;

    private TextView btnDong;
    private TextView btnSaoChep;
    private TextView btnTiepTucDatHang;

    private String maDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_da_huy);

        anhXa();
        hienThiThongTin();
        xuLySuKien();
    }

    private void anhXa() {

        txtMaDon = findViewById(R.id.txtMaDon);
        txtTongThanhToan = findViewById(R.id.txtTongThanhToan);
        txtPhuongThuc = findViewById(R.id.txtPhuongThuc);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtCuaHang = findViewById(R.id.txtCuaHang);
        txtNguoiNhan = findViewById(R.id.txtNguoiNhan);

        btnDong = findViewById(R.id.btnDong);
        btnSaoChep = findViewById(R.id.btnSaoChep);
        btnTiepTucDatHang = findViewById(R.id.btnTiepTucDatHang);
    }

    private void hienThiThongTin() {

        Intent intent = getIntent();

        maDon = intent.getStringExtra("maDon");

        if (maDon == null) {
            maDon = "#26110812345";
        }

        int tongTien = intent.getIntExtra("tongTien", 0);

        String phuongThuc = intent.getStringExtra("phuongThuc");
        String diaChi = intent.getStringExtra("diaChi");
        String cuaHang = intent.getStringExtra("cuaHang");
        String nguoiNhan = intent.getStringExtra("nguoiNhan");

        txtMaDon.setText(maDon);

        txtTongThanhToan.setText(
                new java.text.DecimalFormat("#,###")
                        .format(tongTien) + " đ"
        );

        txtPhuongThuc.setText(
                phuongThuc == null ? "Tiền mặt" : phuongThuc
        );

        txtDiaChi.setText(
                diaChi == null
                        ? "Giao đến: Chưa cập nhật địa chỉ"
                        : diaChi
        );

        txtCuaHang.setText(
                "Cửa hàng: " +
                        (cuaHang == null ? "TAN MAI" : cuaHang)
        );

        txtNguoiNhan.setText(
                nguoiNhan == null ? "Khách hàng" : nguoiNhan
        );
    }

    private void xuLySuKien() {

        btnDong.setOnClickListener(v -> veTrangThucDon());

        btnTiepTucDatHang.setOnClickListener(v -> veTrangThucDon());

        btnSaoChep.setOnClickListener(v -> {

            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(
                            Context.CLIPBOARD_SERVICE
                    );

            if (clipboard != null) {

                clipboard.setPrimaryClip(
                        ClipData.newPlainText("Mã đơn hàng", maDon)
                );

                Toast.makeText(
                        this,
                        "Đã sao chép mã đơn hàng",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void veTrangThucDon() {

        Intent intent = new Intent(
                DonHangDaHuyActivity.this,
                ThucDonActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
        finish();
    }
}
