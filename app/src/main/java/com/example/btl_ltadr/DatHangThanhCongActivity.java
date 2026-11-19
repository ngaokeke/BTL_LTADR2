package com.example.btl_ltadr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private DatabaseHelper databaseHelper;

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

        databaseHelper = new DatabaseHelper(this);

        if (hienThiThongTinDonHang()) {
            xuLySuKien();
        }
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

    private boolean hienThiThongTinDonHang() {

        Intent intent = getIntent();

        // Lấy mã khách hàng hoặc số điện thoại đang đăng nhập
        SharedPreferences preferences = getSharedPreferences(
                "FOODIE_SESSION", MODE_PRIVATE
        );

        String sdt = preferences.getString("SO_DIEN_THOAI", "");

        // Truy vấn thông tin khách hàng từ SQLite
        KhachHang khachHang = databaseHelper.layKhachHang("KH" + sdt);

        if (khachHang == null) {
            Toast.makeText(this,
                    "Không tìm thấy thông tin khách hàng!",
                    Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        // Lấy tên và địa chỉ trực tiếp từ SQLite
        nguoiNhan = khachHang.getTenKhachHang();
        diaChi = khachHang.getDiaChiGiaoHang();

        if (diaChi == null || diaChi.trim().isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng cập nhật địa chỉ giao hàng!",
                    Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, DiaChiGiaoHangActivity.class));
            finish();
            return false;
        }

        if (nguoiNhan == null || nguoiNhan.trim().isEmpty()) {
            nguoiNhan = "Khách hàng";
        }

        // Lấy thông tin đơn hàng từ màn hình trước
        maDon = intent.getStringExtra("maDon");

        if (maDon == null || maDon.trim().isEmpty()) {
            maDon = "#261108" + (System.currentTimeMillis() % 100000);
        }

        tongTien = intent.getIntExtra("tongTien", 0);

        phuongThuc = intent.getStringExtra("phuongThuc");
        cuaHang = intent.getStringExtra("cuaHang");

        // Lấy danh sách combo
        danhSachMonJson = intent.getStringExtra("danhSachMonJson");

        if (danhSachMonJson == null || danhSachMonJson.trim().isEmpty()) {
            danhSachMonJson = intent.getStringExtra("danhSachMon");
        }

        if (danhSachMonJson == null || danhSachMonJson.trim().isEmpty()) {
            danhSachMonJson = "[]";
        }

        if (phuongThuc == null || phuongThuc.trim().isEmpty()) {
            phuongThuc = "Tiền mặt";
        }

        if (cuaHang == null || cuaHang.trim().isEmpty()) {
            cuaHang = "TAN MAI";
        }

        // Hiển thị thông tin
        txtMaDon.setText(maDon);

        txtTongThanhToan.setText(
                new DecimalFormat("#,###").format(tongTien) + " đ"
        );

        txtPhuongThuc.setText(phuongThuc);

        // Địa chỉ lấy từ SQLite
        txtDiaChi.setText("Giao đến: " + diaChi);

        txtCuaHang.setText("Cửa hàng: " + cuaHang);

        // Tên khách hàng lấy từ SQLite
        txtNguoiNhan.setText(nguoiNhan);

        // Lưu đơn hàng
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

        return true;
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
