
package com.example.btl_ltadr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class DatHangThanhCongActivity extends AppCompatActivity {

    private TextView txtMaDon;
    private TextView txtTongThanhToan;
    private TextView txtPhuongThuc;
    private TextView txtDiaChi;
    private TextView txtCuaHang;
    private TextView txtNguoiNhan;

    private TextView btnDong;
    private TextView btnSaoChep;
    private TextView btnHuyDon;
    private TextView btnVeTrangChu;

    private String maDon;
    private String danhSachMonJson = "[]";

    private String phuongThuc;
    private String diaChi;
    private String cuaHang;
    private String nguoiNhan;
    private int tongTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang_thanh_cong);

        anhXa();
        hienThiThongTinDonHang();
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
        btnHuyDon = findViewById(R.id.btnHuyDon);
        btnVeTrangChu = findViewById(R.id.btnVeTrangChu);
    }

    private void hienThiThongTinDonHang() {

        Intent intent = getIntent();

        maDon = intent.getStringExtra("maDon");

        if (maDon == null || maDon.trim().isEmpty()) {
            maDon = "#261108" + (System.currentTimeMillis() % 100000);
        }

        tongTien = intent.getIntExtra("tongTien", 0);

        phuongThuc = intent.getStringExtra("phuongThuc");
        diaChi = intent.getStringExtra("diaChi");
        cuaHang = intent.getStringExtra("cuaHang");
        nguoiNhan = intent.getStringExtra("nguoiNhan");

        // LẤY DANH SÁCH COMBO TỪ MÀN HÌNH TRƯỚC
        danhSachMonJson = intent.getStringExtra("danhSachMonJson");

        // Hỗ trợ trường hợp màn hình trước sử dụng key danhSachMon
        if (danhSachMonJson == null || danhSachMonJson.trim().isEmpty()) {
            danhSachMonJson = intent.getStringExtra("danhSachMon");
        }

        if (danhSachMonJson == null || danhSachMonJson.trim().isEmpty()) {
            danhSachMonJson = "[]";
        }

        if (phuongThuc == null) {
            phuongThuc = "Tiền mặt";
        }

        if (diaChi == null) {
            diaChi = "Chưa cập nhật địa chỉ";
        }

        if (cuaHang == null) {
            cuaHang = "TAN MAI";
        }

        if (nguoiNhan == null) {
            nguoiNhan = "Khách hàng";
        }

        txtMaDon.setText(maDon);

        txtTongThanhToan.setText(
                new DecimalFormat("#,###").format(tongTien) + " đ"
        );

        txtPhuongThuc.setText(phuongThuc);
        txtDiaChi.setText("Giao đến: " + diaChi);
        txtCuaHang.setText("Cửa hàng: " + cuaHang);
        txtNguoiNhan.setText(nguoiNhan);

        // LƯU ĐƠN HÀNG KÈM DANH SÁCH COMBO
        DonHangActivity.luuDonHang(
                this,
                maDon,
                new DecimalFormat("#,###").format(tongTien) + " đ",
                phuongThuc,
                diaChi,
                cuaHang,
                nguoiNhan,
                danhSachMonJson
        );
    }

    private void xuLySuKien() {

        btnDong.setOnClickListener(v -> veTrangChu());

        btnVeTrangChu.setOnClickListener(v -> veTrangChu());

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

        btnHuyDon.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Hủy đơn hàng")
                    .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                    .setNegativeButton("Không", null)
                    .setPositiveButton("Đồng ý", (dialog, which) -> {

                        // CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
                        DonHangActivity.capNhatTrangThai(
                                DatHangThanhCongActivity.this,
                                maDon,
                                "Đơn hàng đã hủy"
                        );

                        Intent intent = new Intent(
                                DatHangThanhCongActivity.this,
                                DonHangDaHuyActivity.class
                        );

                        intent.putExtra("maDon", maDon);
                        intent.putExtra("tongTien", tongTien);
                        intent.putExtra("phuongThuc", phuongThuc);
                        intent.putExtra("diaChi", diaChi);
                        intent.putExtra("cuaHang", cuaHang);
                        intent.putExtra("nguoiNhan", nguoiNhan);
                        intent.putExtra("danhSachMonJson", danhSachMonJson);

                        // TẠO THÔNG BÁO
                        ThongBaoDonHang.taoKenhThongBao(this);

                        ThongBaoDonHang.guiThongBao(
                                DatHangThanhCongActivity.this,
                                "Đã hủy đơn hàng",
                                "Đơn hàng " + maDon + " đã được hủy thành công.",
                                new Intent(
                                        DatHangThanhCongActivity.this,
                                        DonHangDaHuyActivity.class
                                ).putExtras(intent.getExtras()),
                                1002
                        );

                        startActivity(intent);
                        finish();
                    })
                    .show();
        });
    }

    private void veTrangChu() {

        Intent intent = new Intent(
                DatHangThanhCongActivity.this,
                TrangChuActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
        finish();
    }
}
