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

        int tongTien = intent.getIntExtra("tongTien", 0);

        String phuongThuc = intent.getStringExtra("phuongThuc");
        String diaChi = intent.getStringExtra("diaChi");
        String cuaHang = intent.getStringExtra("cuaHang");
        String nguoiNhan = intent.getStringExtra("nguoiNhan");

        txtMaDon.setText(maDon);

        txtTongThanhToan.setText(
                new DecimalFormat("#,###").format(tongTien) + " đ"
        );

        txtPhuongThuc.setText(
                phuongThuc == null ? "Tiền mặt" : phuongThuc
        );

        txtDiaChi.setText(
                "Giao đến: " +
                        (diaChi == null ? "Chưa cập nhật địa chỉ" : diaChi)
        );

        txtCuaHang.setText(
                "Cửa hàng: " +
                        (cuaHang == null ? "TAN MAI" : cuaHang)
        );

        txtNguoiNhan.setText(
                nguoiNhan == null ? "Khách hàng" : nguoiNhan
        );
        // Lưu đơn hàng để hiển thị ở trang Đơn hàng
        DonHangActivity.luuDonHang(
                this,
                maDon,
                new DecimalFormat("#,###").format(tongTien) + " đ",
                phuongThuc == null ? "Tiền mặt" : phuongThuc,
                diaChi == null ? "Chưa cập nhật địa chỉ" : diaChi,
                cuaHang == null ? "TAN MAI" : cuaHang,
                nguoiNhan == null ? "Khách hàng" : nguoiNhan
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

                        // Cập nhật trạng thái đơn hàng
                        DonHangActivity.capNhatTrangThai(
                                DatHangThanhCongActivity.this,
                                maDon,
                                "Đơn hàng đã hủy"
                        );

                        Intent intent = new Intent(
                                DatHangThanhCongActivity.this,
                                DonHangDaHuyActivity.class
                        );

                        // Các đoạn code còn lại giữ nguyên

                        // Giữ nguyên thông tin đơn hàng
                        intent.putExtra("maDon", maDon);

                        String tongTienText =
                                txtTongThanhToan.getText().toString();

                        tongTienText = tongTienText.replaceAll("[^0-9]", "");

                        int tongTien = 0;

                        try {
                            tongTien = Integer.parseInt(tongTienText);
                        } catch (NumberFormatException e) {
                            tongTien = 0;
                        }

                        intent.putExtra("tongTien", tongTien);

                        intent.putExtra(
                                "phuongThuc",
                                txtPhuongThuc.getText().toString()
                        );

                        intent.putExtra(
                                "diaChi",
                                txtDiaChi.getText().toString()
                        );

                        intent.putExtra("cuaHang", "TAN MAI");

                        intent.putExtra(
                                "nguoiNhan",
                                txtNguoiNhan.getText().toString()
                        );

                        // Tạo thông báo khi hủy đơn hàng
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
