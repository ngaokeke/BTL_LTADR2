package com.example.btl_ltadr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaiKhoanActivity extends AppCompatActivity {

    TextView txtTenTaiKhoan, txtSoDienThoaiTaiKhoan;

    LinearLayout navTrangChu, navThucDon, navGioHang,
            navDonHang, navTaiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        anhXa();
        hienThiThongTin();
        xuLySuKien();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienThiThongTin();
    }

    private void anhXa() {
        txtTenTaiKhoan = findViewById(R.id.txtTenTaiKhoan);
        txtSoDienThoaiTaiKhoan = findViewById(R.id.txtSoDienThoaiTaiKhoan);

        navTrangChu = findViewById(R.id.navTrangChu);
        navThucDon = findViewById(R.id.navThucDon);
        navGioHang = findViewById(R.id.navGioHang);
        navDonHang = findViewById(R.id.navDonHang);
        navTaiKhoan = findViewById(R.id.navTaiKhoan);
    }

    private void hienThiThongTin() {

        // Lấy thông tin tài khoản đang đăng nhập
        SharedPreferences session = getSharedPreferences(
                "FOODIE_SESSION",
                MODE_PRIVATE
        );

        boolean daDangNhap = session.getBoolean(
                "DANG_DANG_NHAP",
                false
        );

        if (daDangNhap) {

            String ten = session.getString(
                    "TEN_KHACH_HANG",
                    ""
            );

            String soDienThoai = session.getString(
                    "SO_DIEN_THOAI",
                    ""
            );

            if (ten == null || ten.trim().isEmpty()) {
                ten = "Khách hàng";
            }

            txtTenTaiKhoan.setText(ten);
            txtSoDienThoaiTaiKhoan.setText(soDienThoai);

        } else {
            txtTenTaiKhoan.setText("Khách hàng");
            txtSoDienThoaiTaiKhoan.setText("");
        }
    }

    private void xuLySuKien() {

        navTrangChu.setOnClickListener(v ->
                chuyenManHinh(TrangChuActivity.class));

        navThucDon.setOnClickListener(v ->
                chuyenManHinh(ThucDonActivity.class));

        navGioHang.setOnClickListener(v ->
                chuyenManHinh(GioHangActivity.class));

        navDonHang.setOnClickListener(v ->
                chuyenManHinh(DonHangActivity.class));

        navTaiKhoan.setOnClickListener(v -> {
            // Đang ở trang tài khoản
        });

        findViewById(R.id.btnDonHangTaiKhoan).setOnClickListener(v ->
                chuyenManHinh(DonHangActivity.class));

        findViewById(R.id.btnDangXuatTaiKhoan).setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {

                        // Chỉ xóa phiên đăng nhập, không xóa danh sách tài khoản
                        getSharedPreferences(
                                "FOODIE_SESSION",
                                MODE_PRIVATE
                        ).edit()
                                .putBoolean("DANG_DANG_NHAP", false)
                                .remove("TEN_KHACH_HANG")
                                .remove("SO_DIEN_THOAI")
                                .apply();

                        Intent intent = new Intent(
                                TaiKhoanActivity.this,
                                TrangChuActivity.class
                        );

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                        );

                        startActivity(intent);
                        finish();
                    })
                    .show();
        });

        findViewById(R.id.btnMonYeuThich).setOnClickListener(v ->
                thongBao("Món yêu thích"));

        findViewById(R.id.btnDiaChiGiaoHang).setOnClickListener(v ->
                thongBao("Địa chỉ giao hàng"));

        findViewById(R.id.btnQuanLyThe).setOnClickListener(v ->
                thongBao("Quản lý thẻ"));

        findViewById(R.id.btnTaiKhoanMangXaHoi).setOnClickListener(v ->
                thongBao("Tài khoản mạng xã hội"));

        findViewById(R.id.btnGopY).setOnClickListener(v ->
                thongBao("Góp ý"));

        findViewById(R.id.btnChinhSach).setOnClickListener(v ->
                thongBao("Chính sách"));

        findViewById(R.id.btnThongTinLienHe).setOnClickListener(v ->
                thongBao("Thông tin liên hệ"));

        findViewById(R.id.btnTimCuaHang).setOnClickListener(v ->
                thongBao("Tìm cửa hàng"));

        findViewById(R.id.btnSuaThongTinTaiKhoan).setOnClickListener(v ->
                thongBao("Chỉnh sửa thông tin tài khoản"));

        findViewById(R.id.btnMemberTaiKhoan).setOnClickListener(v ->
                thongBao("Thông tin thành viên"));

        findViewById(R.id.btnUuDaiTaiKhoan).setOnClickListener(v ->
                thongBao("Ưu đãi"));
    }

    private void chuyenManHinh(Class<?> activity) {
        Intent intent = new Intent(TaiKhoanActivity.this, activity);
        startActivity(intent);
    }

    private void thongBao(String noiDung) {
        Toast.makeText(this, noiDung, Toast.LENGTH_SHORT).show();
    }

}