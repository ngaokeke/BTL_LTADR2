package com.example.btl_ltadr;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

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

        // Dong trang dang nhap
        btnDongDangNhap.setOnClickListener(v -> {
            finish();
        });

        // Tiep tuc dang nhap

        btnTiepTuc.setOnClickListener(v -> {
            String soDienThoai =
                    edtSoDienThoai.getText().toString().trim();

            if (!soDienThoai.matches("^0[0-9]{9}$")) {
                edtSoDienThoai.setError(
                        "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0"
                );
                edtSoDienThoai.requestFocus();
                return;
            }

            Intent intent = new Intent(
                    DangNhapActivity.this,
                    XacNhanActivity.class
            );

            intent.putExtra("SO_DIEN_THOAI", soDienThoai);
            startActivity(intent);
        });


        // Chuyen sang trang dang ky (se ket noi sau)
        btnTaoTaiKhoan.setOnClickListener(v -> {
            startActivity(new Intent(DangNhapActivity.this, DangKyActivity.class));
        });
    }
}
