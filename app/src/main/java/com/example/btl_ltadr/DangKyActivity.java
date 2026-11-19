package com.example.btl_ltadr;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DangKyActivity extends AppCompatActivity {

    private EditText edtTenNguoiNhan, edtSoDienThoaiDangKy;
    private Button btnHoanThanhDangKy;
    private TextView btnDongDangKy;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        databaseHelper = new DatabaseHelper(this);

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

            // 1. Kiểm tra tên
            if (ten.isEmpty()) {
                edtTenNguoiNhan.setError("Vui lòng nhập tên người nhận");
                edtTenNguoiNhan.requestFocus();
                return;
            }

            // 2. Kiểm tra định dạng số điện thoại
            if (!soDienThoai.matches("^0[0-9]{9}$")) {
                edtSoDienThoaiDangKy.setError(
                        "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0"
                );
                edtSoDienThoaiDangKy.requestFocus();
                return;
            }

            // 3. Kiểm tra số điện thoại đã tồn tại trong SQLite
            if (databaseHelper.kiemTraSoDienThoai(soDienThoai)) {

                Toast.makeText(
                        DangKyActivity.this,
                        "Số điện thoại đã tồn tại! Vui lòng sử dụng số khác.",
                        Toast.LENGTH_LONG
                ).show();

                edtSoDienThoaiDangKy.setError("Số điện thoại đã được đăng ký");
                edtSoDienThoaiDangKy.requestFocus();

                return;
            }

            // 4. Tạo mã khách hàng
            String maKhachHang = "KH" + soDienThoai;

            // 5. Tạo đối tượng khách hàng
            KhachHang khachHang = new KhachHang(
                    maKhachHang,
                    ten,
                    soDienThoai,
                    ""
            );

            // 6. Thêm khách hàng vào SQLite
            boolean ketQua = databaseHelper.themKhachHang(khachHang);

            if (ketQua) {

                Intent intent = new Intent();
                intent.putExtra("MA_KHACH_HANG", maKhachHang);
                intent.putExtra("TEN_NGUOI_NHAN", ten);
                intent.putExtra("TEN_KHACH_HANG", ten);
                intent.putExtra("SO_DIEN_THOAI", soDienThoai);

                Toast.makeText(
                        DangKyActivity.this,
                        "Đăng ký thông tin thành công!",
                        Toast.LENGTH_SHORT
                ).show();

                setResult(RESULT_OK, intent);
                finish();

            } else {

                Toast.makeText(
                        DangKyActivity.this,
                        "Đăng ký thất bại! Không thể lưu dữ liệu.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
