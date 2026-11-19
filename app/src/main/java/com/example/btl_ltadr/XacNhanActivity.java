package com.example.btl_ltadr;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class XacNhanActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "FOODIE_OTP";
    private static final int REQUEST_NOTIFICATION = 101;
    private static final int NOTIFICATION_ID = 1001;

    private EditText[] cacOOTP;
    private TextView txtThongBaoOTP;
    private TextView btnDongXacNhan;
    private TextView txtGuiLaiMa;
    private Button btnHoanThanhOTP;

    private String maOTP;
    private String soDienThoai;
    private String tenKhachHang;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan);

        databaseHelper = new DatabaseHelper(this);

        anhXa();

        if (!nhanThongTinKhachHang()) {
            return;
        }

        taoKenhThongBao();
        ganSuKienNhapOTP();
        xuLySuKien();
        taoVaGuiOTP();
    }

    private void anhXa() {
        cacOOTP = new EditText[]{
                findViewById(R.id.edtOTP1),
                findViewById(R.id.edtOTP2),
                findViewById(R.id.edtOTP3),
                findViewById(R.id.edtOTP4),
                findViewById(R.id.edtOTP5),
                findViewById(R.id.edtOTP6)
        };

        txtThongBaoOTP = findViewById(R.id.txtThongBaoOTP);
        btnDongXacNhan = findViewById(R.id.btnDongXacNhan);
        txtGuiLaiMa = findViewById(R.id.txtGuiLaiMa);
        btnHoanThanhOTP = findViewById(R.id.btnHoanThanhOTP);
    }

    // Lấy thông tin khách hàng từ SQLite
    private boolean nhanThongTinKhachHang() {
        Intent intent = getIntent();

        soDienThoai = intent.getStringExtra("SO_DIEN_THOAI");

        if (soDienThoai == null || !soDienThoai.matches("^0[0-9]{9}$")) {
            Toast.makeText(this,
                    "Số điện thoại không hợp lệ!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        // Mã khách hàng được tạo theo quy tắc KH + số điện thoại
        String maKhachHang = "KH" + soDienThoai;

        KhachHang khachHang = databaseHelper.layKhachHang(maKhachHang);

        if (khachHang == null) {
            Toast.makeText(this,
                    "Số điện thoại chưa đăng ký tài khoản!",
                    Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        tenKhachHang = khachHang.getTenKhachHang();

        txtThongBaoOTP.setText(
                "Mã xác thực đã được gửi đến " + soDienThoai
        );

        return true;
    }

    private void taoKenhThongBao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Thông báo mã OTP Foodie",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription(
                    "Thông báo mã xác thực OTP của ứng dụng Foodie"
            );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void taoVaGuiOTP() {
        maOTP = String.format(
                "%06d",
                new Random().nextInt(1000000)
        );

        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION
            );
        } else {
            guiThongBaoOTP();
        }
    }

    private void guiThongBaoOTP() {
        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (manager == null) {
            Toast.makeText(this,
                    "Không thể tạo thông báo OTP.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Foodie - Mã xác thực")
                .setContentText("Mã OTP kiểm thử: " + maOTP)
                .setStyle(
                        new Notification.BigTextStyle().bigText(
                                "Mã OTP kiểm thử: " + maOTP
                        )
                )
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify(NOTIFICATION_ID, builder.build());
    }

    private void ganSuKienNhapOTP() {
        for (int i = 0; i < cacOOTP.length - 1; i++) {
            final int viTri = i;

            cacOOTP[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(
                        CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(
                        CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        cacOOTP[viTri + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private String layMaOTPDaNhap() {
        StringBuilder builder = new StringBuilder();

        for (EditText o : cacOOTP) {
            builder.append(o.getText().toString().trim());
        }

        return builder.toString();
    }

    private void xoaCacOOTP() {
        for (EditText o : cacOOTP) {
            o.setText("");
        }

        cacOOTP[0].requestFocus();
    }

    private void xuLySuKien() {
        btnDongXacNhan.setOnClickListener(v -> finish());

        txtGuiLaiMa.setOnClickListener(v -> {
            xoaCacOOTP();
            taoVaGuiOTP();

            Toast.makeText(this,
                    "Đã tạo mã OTP mới. Kiểm tra thông báo!",
                    Toast.LENGTH_SHORT).show();
        });

        btnHoanThanhOTP.setOnClickListener(v -> {
            String maNhap = layMaOTPDaNhap();

            if (maNhap.length() != 6) {
                Toast.makeText(this,
                        "Vui lòng nhập đủ 6 chữ số OTP",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!maNhap.equals(maOTP)) {
                Toast.makeText(this,
                        "Mã OTP không chính xác!",
                        Toast.LENGTH_SHORT).show();

                xoaCacOOTP();
                return;
            }

            // Lưu trạng thái phiên đăng nhập
            // Thông tin khách hàng vẫn được lưu trong SQLite
            SharedPreferences session = getSharedPreferences(
                    "FOODIE_SESSION",
                    MODE_PRIVATE
            );

            session.edit()
                    .putBoolean("DANG_DANG_NHAP", true)
                    .putString("SO_DIEN_THOAI", soDienThoai)
                    .putString("TEN_KHACH_HANG", tenKhachHang)
                    .apply();

            Toast.makeText(this,
                    "Xác nhận thành công!",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(
                    XacNhanActivity.this,
                    TrangChuActivity.class
            );

            intent.putExtra("TEN_KHACH_HANG", tenKhachHang);
            intent.putExtra("SO_DIEN_THOAI", soDienThoai);

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == REQUEST_NOTIFICATION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                guiThongBaoOTP();
            } else {
                Toast.makeText(this,
                        "Quyền thông báo bị từ chối. OTP kiểm thử: " + maOTP,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
