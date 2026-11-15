package com.example.btl_ltadr;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class DangKyActivity extends AppCompatActivity {

    private EditText edtTenNguoiNhan, edtSoDienThoaiDangKy;
    private Button btnHoanThanhDangKy;
    private TextView btnDongDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        anhXa();
        xuLySuKien();
    }

    private void anhXa() {
        edtTenNguoiNhan = findViewById(R.id.edtTenNguoiNhan);
        edtSoDienThoaiDangKy = findViewById(R.id.edtSoDienThoaiDangKy);
        btnHoanThanhDangKy = findViewById(R.id.btnHoanThanhDangKy);
        btnDongDangKy = findViewById(R.id.btnDongDangKy);
    }

    private void xuLySuKien() {

        btnDongDangKy.setOnClickListener(v -> finish());

        btnHoanThanhDangKy.setOnClickListener(v -> {

            String ten = edtTenNguoiNhan.getText().toString().trim();
            String soDienThoai = edtSoDienThoaiDangKy.getText().toString().trim();

            if (ten.isEmpty()) {
                edtTenNguoiNhan.setError("Vui lòng nhập tên người nhận");
                edtTenNguoiNhan.requestFocus();
                return;
            }

            if (!soDienThoai.matches("^0[0-9]{9}$")) {
                edtSoDienThoaiDangKy.setError(
                        "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0"
                );
                edtSoDienThoaiDangKy.requestFocus();
                return;
            }

            SharedPreferences preferences = getSharedPreferences(
                    "FOODIE_DATA",
                    MODE_PRIVATE
            );

            String duLieuCu = preferences.getString("DANH_SACH_TAI_KHOAN", "[]");

            try {
                JSONArray danhSach = new JSONArray(duLieuCu);

                // Kiểm tra số điện thoại đã đăng ký
                for (int i = 0; i < danhSach.length(); i++) {
                    JSONObject taiKhoan = danhSach.getJSONObject(i);

                    if (soDienThoai.equals(taiKhoan.optString("SO_DIEN_THOAI"))) {
                        Toast.makeText(
                                this,
                                "Số điện thoại đã được đăng ký!",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                }

                // Thêm tài khoản mới
                JSONObject taiKhoanMoi = new JSONObject();
                taiKhoanMoi.put("TEN_KHACH_HANG", ten);
                taiKhoanMoi.put("SO_DIEN_THOAI", soDienThoai);

                danhSach.put(taiKhoanMoi);

                preferences.edit()
                        .putString("DANH_SACH_TAI_KHOAN", danhSach.toString())
                        .apply();

                Intent intent = new Intent();
                intent.putExtra("TEN_NGUOI_NHAN", ten);
                intent.putExtra("TEN_KHACH_HANG", ten);
                intent.putExtra("SO_DIEN_THOAI", soDienThoai);

                Toast.makeText(
                        this,
                        "Đăng ký thông tin thành công!",
                        Toast.LENGTH_SHORT
                ).show();

                setResult(RESULT_OK, intent);
                finish();

            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Không thể lưu thông tin tài khoản!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

}