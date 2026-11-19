package com.example.btl_ltadr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DangNhapActivity extends AppCompatActivity {

    private EditText edtSoDienThoai;
    private Button btnTiepTuc;
    private TextView btnTaoTaiKhoan;
    private TextView btnDongDangNhap;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        databaseHelper = new DatabaseHelper(this);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences session = getSharedPreferences(
                "FOODIE_SESSION",
                MODE_PRIVATE
        );

        boolean daDangNhap = session.getBoolean(
                "DANG_DANG_NHAP",
                false
        );

        if (daDangNhap) {
            Intent intent = new Intent(
                    DangNhapActivity.this,
                    TrangChuActivity.class
            );

            intent.putExtra(
                    "TEN_KHACH_HANG",
                    session.getString("TEN_KHACH_HANG", "")
            );

            intent.putExtra(
                    "SO_DIEN_THOAI",
                    session.getString("SO_DIEN_THOAI", "")
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);
            finish();
            return;
        }

        anhXa();
        xuLySuKien();
    }

    private void anhXa() {
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        btnTaoTaiKhoan = findViewById(R.id.btnTaoTaiKhoan);
        btnDongDangNhap = findViewById(R.id.btnDongDangNhap);
    }

    private void xuLySuKien() {

        // Đóng màn hình đăng nhập
        btnDongDangNhap.setOnClickListener(v -> {
            finish();
        });

        // Đăng nhập bằng số điện thoại
        btnTiepTuc.setOnClickListener(v -> {

            String soDienThoai = edtSoDienThoai
                    .getText()
                    .toString()
                    .trim();

            // Kiểm tra định dạng số điện thoại
            if (!soDienThoai.matches("^0[0-9]{9}$")) {
                edtSoDienThoai.setError(
                        "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0"
                );
                edtSoDienThoai.requestFocus();
                return;
            }

            // Kiểm tra số điện thoại có trong SQLite không
            boolean tonTai = databaseHelper.kiemTraSoDienThoai(
                    soDienThoai
            );

            if (!tonTai) {
                Toast.makeText(
                        DangNhapActivity.this,
                        "Số điện thoại chưa được đăng ký. Vui lòng tạo tài khoản!",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            // Nếu đã tồn tại thì chuyển sang xác nhận
            Intent intent = new Intent(
                    DangNhapActivity.this,
                    XacNhanActivity.class
            );

            intent.putExtra("SO_DIEN_THOAI", soDienThoai);
            startActivity(intent);
        });

        // Chuyển sang màn hình tạo tài khoản
        btnTaoTaiKhoan.setOnClickListener(v -> {
            startActivity(new Intent(
                    DangNhapActivity.this,
                    DangKyActivity.class
            ));
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
