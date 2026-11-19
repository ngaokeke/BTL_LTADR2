package com.example.btl_ltadr;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DiaChiGiaoHangActivity extends AppCompatActivity {

    ImageView btnQuayLai, btnSuaDiaChi;
    TextView txtTenDiaChi, txtMacDinh, txtDiaChi;

    DatabaseHelper databaseHelper;

    private String maKhachHang;
    private KhachHang khachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_chi_giao_hang);

        databaseHelper = new DatabaseHelper(this);

        anhXa();

        // Lấy số điện thoại của khách hàng đang đăng nhập
        String soDienThoai = getSharedPreferences(
                "FOODIE_SESSION",
                MODE_PRIVATE
        ).getString("SO_DIEN_THOAI", "");

        if (soDienThoai.isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng đăng nhập!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        maKhachHang = "KH" + soDienThoai;

        hienThiDiaChi();
        suKien();
    }

    private void anhXa() {
        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnSuaDiaChi = findViewById(R.id.btnSuaDiaChi);

        txtTenDiaChi = findViewById(R.id.txtTenDiaChi);
        txtMacDinh = findViewById(R.id.txtMacDinh);
        txtDiaChi = findViewById(R.id.txtDiaChi);
    }

    // Lấy dữ liệu khách hàng từ SQLite
    private void hienThiDiaChi() {

        khachHang = databaseHelper.layKhachHang(maKhachHang);

        if (khachHang == null) {
            Toast.makeText(this,
                    "Không tìm thấy thông tin khách hàng!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtTenDiaChi.setText(khachHang.getTenKhachHang());

        String diaChi = khachHang.getDiaChiGiaoHang();

        if (diaChi == null || diaChi.trim().isEmpty()) {
            txtDiaChi.setText("Chưa cập nhật địa chỉ giao hàng");
        } else {
            txtDiaChi.setText(diaChi);
        }

        txtMacDinh.setText("Mặc định");
    }

    private void suKien() {

        btnQuayLai.setOnClickListener(v -> finish());

        btnSuaDiaChi.setOnClickListener(v -> hienThiHopThoaiSua());
    }

    private void hienThiHopThoaiSua() {

        if (khachHang == null) {
            return;
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        EditText edtTen = new EditText(this);
        edtTen.setHint("Nhập tên khách hàng");
        edtTen.setSingleLine(true);
        edtTen.setText(khachHang.getTenKhachHang());

        EditText edtDiaChi = new EditText(this);
        edtDiaChi.setHint("Nhập địa chỉ giao hàng");
        edtDiaChi.setMinLines(2);
        edtDiaChi.setGravity(Gravity.TOP);

        String diaChiHienTai = khachHang.getDiaChiGiaoHang();

        if (diaChiHienTai != null) {
            edtDiaChi.setText(diaChiHienTai);
        }

        layout.addView(edtTen);
        layout.addView(edtDiaChi);

        new AlertDialog.Builder(this)
                .setTitle("Sửa địa chỉ của tôi")
                .setView(layout)
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Lưu", (dialog, which) -> {

                    String ten = edtTen.getText().toString().trim();
                    String diaChi = edtDiaChi.getText().toString().trim();

                    if (ten.isEmpty() || diaChi.isEmpty()) {
                        Toast.makeText(this,
                                "Vui lòng nhập đầy đủ thông tin",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Cập nhật thông tin đối tượng khách hàng
                    khachHang.setTenKhachHang(ten);
                    khachHang.setDiaChiGiaoHang(diaChi);

                    // Cập nhật trực tiếp vào SQLite
                    boolean ketQua = databaseHelper.capNhatKhachHang(khachHang);

                    if (ketQua) {

                        // Đồng bộ tên trong phiên đăng nhập
                        getSharedPreferences(
                                "FOODIE_SESSION",
                                MODE_PRIVATE
                        ).edit()
                                .putString("TEN_KHACH_HANG", ten)
                                .apply();

                        hienThiDiaChi();

                        Toast.makeText(this,
                                "Cập nhật địa chỉ thành công!",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this,
                                "Cập nhật thất bại!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
