package com.example.btl_ltadr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DonHangActivity extends AppCompatActivity {

    private LinearLayout layoutDanhSachDonHang;

    private TextView tabTatCa;
    private TextView tabDatThanhCong;
    private TextView tabDangGiao;
    private TextView tabHoanTat;
    private TextView tabDaHuy;

    private String trangThaiDangChon = "Tất cả";

    private static final String TEN_PREF = "FOODIE_DON_HANG";
    private static final String KEY_DON_HANG = "DANH_SACH";

    private static final String TRANG_THAI_DAT_THANH_CONG =
            "Đặt hàng thành công";

    private static final String TRANG_THAI_DANG_GIAO =
            "Đang giao hàng";

    private static final String TRANG_THAI_HOAN_TAT =
            "Hoàn tất đơn hàng";

    private static final String TRANG_THAI_DA_HUY =
            "Đơn hàng đã hủy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);

        anhXa();
        xuLyTab();
        xuLyDieuHuong();

        hienThiDanhSachDonHang();
    }

    // ANH XA CAC VIEW THEO XML
    private void anhXa() {

        layoutDanhSachDonHang =
                findViewById(R.id.layoutDanhSachDonHang);

        tabTatCa = findViewById(R.id.tabTatCa);

        tabDatThanhCong =
                findViewById(R.id.tabDatThanhCong);

        tabDangGiao =
                findViewById(R.id.tabDangGiao);

        tabHoanTat =
                findViewById(R.id.tabHoanTat);

        tabDaHuy =
                findViewById(R.id.tabDaHuy);
    }

    // LUU DON HANG SAU KHI DAT HANG THANH CONG
    public static void luuDonHang(
            Context context,
            String maDon,
            String tongTien,
            String phuongThuc,
            String diaChi,
            String cuaHang,
            String nguoiNhan
    ) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        TEN_PREF,
                        Context.MODE_PRIVATE
                );

        JSONArray danhSach;

        try {
            danhSach = new JSONArray(
                    preferences.getString(KEY_DON_HANG, "[]")
            );
        } catch (JSONException e) {
            danhSach = new JSONArray();
        }

        // KIEM TRA TRUNG MA DON
        for (int i = 0; i < danhSach.length(); i++) {

            JSONObject don = danhSach.optJSONObject(i);

            if (don != null &&
                    maDon.equals(don.optString("maDon"))) {
                return;
            }
        }

        JSONObject donHang = new JSONObject();

        try {

            donHang.put("maDon", maDon);
            donHang.put("tongTien", tongTien);
            donHang.put("phuongThuc", phuongThuc);
            donHang.put("diaChi", diaChi);
            donHang.put("cuaHang", cuaHang);
            donHang.put("nguoiNhan", nguoiNhan);

            // TRANG THAI BAN DAU
            donHang.put(
                    "trangThai",
                    TRANG_THAI_DAT_THANH_CONG
            );

            danhSach.put(donHang);

            preferences.edit()
                    .putString(KEY_DON_HANG, danhSach.toString())
                    .apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // CAP NHAT TRANG THAI DON HANG
    public static void capNhatTrangThai(
            Context context,
            String maDon,
            String trangThaiMoi
    ) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        TEN_PREF,
                        Context.MODE_PRIVATE
                );

        try {

            JSONArray danhSach = new JSONArray(
                    preferences.getString(KEY_DON_HANG, "[]")
            );

            for (int i = 0; i < danhSach.length(); i++) {

                JSONObject don = danhSach.optJSONObject(i);

                if (don != null &&
                        maDon.equals(don.optString("maDon"))) {

                    don.put("trangThai", trangThaiMoi);
                    break;
                }
            }

            preferences.edit()
                    .putString(KEY_DON_HANG, danhSach.toString())
                    .apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // LAY DANH SACH DON HANG DA LUU
    private JSONArray layDanhSachDonHang() {

        SharedPreferences preferences =
                getSharedPreferences(TEN_PREF, MODE_PRIVATE);

        try {

            return new JSONArray(
                    preferences.getString(KEY_DON_HANG, "[]")
            );

        } catch (JSONException e) {

            return new JSONArray();
        }
    }

    // XU LY 5 TAB
    private void xuLyTab() {

        tabTatCa.setOnClickListener(
                v -> chonTab("Tất cả")
        );

        tabDatThanhCong.setOnClickListener(
                v -> chonTab(TRANG_THAI_DAT_THANH_CONG)
        );

        tabDangGiao.setOnClickListener(
                v -> chonTab(TRANG_THAI_DANG_GIAO)
        );

        tabHoanTat.setOnClickListener(
                v -> chonTab(TRANG_THAI_HOAN_TAT)
        );

        tabDaHuy.setOnClickListener(
                v -> chonTab(TRANG_THAI_DA_HUY)
        );

        capNhatGiaoDienTab();
    }

    private void chonTab(String trangThai) {

        trangThaiDangChon = trangThai;

        capNhatGiaoDienTab();
        hienThiDanhSachDonHang();
    }

    // CAP NHAT GIAO DIEN TAB DUOC CHON
    private void capNhatGiaoDienTab() {

        capNhatMotTab(
                tabTatCa,
                trangThaiDangChon.equals("Tất cả")
        );

        capNhatMotTab(
                tabDatThanhCong,
                trangThaiDangChon.equals(TRANG_THAI_DAT_THANH_CONG)
        );

        capNhatMotTab(
                tabDangGiao,
                trangThaiDangChon.equals(TRANG_THAI_DANG_GIAO)
        );

        capNhatMotTab(
                tabHoanTat,
                trangThaiDangChon.equals(TRANG_THAI_HOAN_TAT)
        );

        capNhatMotTab(
                tabDaHuy,
                trangThaiDangChon.equals(TRANG_THAI_DA_HUY)
        );
    }

    private void capNhatMotTab(
            TextView tab,
            boolean duocChon
    ) {

        GradientDrawable nen = new GradientDrawable();
        nen.setCornerRadius(dp(20));

        if (duocChon) {

            nen.setColor(Color.WHITE);

            tab.setTextColor(
                    Color.parseColor("#E9232D")
            );

            tab.setTypeface(null, Typeface.BOLD);

        } else {

            nen.setColor(Color.TRANSPARENT);

            tab.setTextColor(Color.WHITE);

            tab.setTypeface(null, Typeface.NORMAL);
        }

        tab.setBackground(nen);
    }

    // HIEN THI DANH SACH DON HANG
    private void hienThiDanhSachDonHang() {

        layoutDanhSachDonHang.removeAllViews();

        JSONArray danhSach = layDanhSachDonHang();

        int soDonHienThi = 0;

        // DON MOI NHAT HIEN THI TRUOC
        for (int i = danhSach.length() - 1; i >= 0; i--) {

            JSONObject don = danhSach.optJSONObject(i);

            if (don == null) {
                continue;
            }

            String trangThai = don.optString("trangThai");

            // LOC DON THEO TAB
            if (!trangThaiDangChon.equals("Tất cả")
                    && !trangThaiDangChon.equals(trangThai)) {
                continue;
            }

            taoTheDonHang(don);

            soDonHienThi++;
        }

        // KHONG CO DON HANG
        if (soDonHienThi == 0) {
            hienThiDonHangTrong();
        }
    }

    // TAO GIAO DIEN MOT DON HANG
    private void taoTheDonHang(JSONObject don) {

        String maDon = don.optString("maDon");
        String tongTien = don.optString("tongTien");
        String phuongThuc = don.optString("phuongThuc");
        String diaChi = don.optString("diaChi");
        String cuaHang = don.optString("cuaHang");
        String nguoiNhan = don.optString("nguoiNhan");
        String trangThai = don.optString("trangThai");

        LinearLayout the = new LinearLayout(this);
        the.setOrientation(LinearLayout.VERTICAL);
        the.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(10)
        );

        GradientDrawable nen = new GradientDrawable();
        nen.setColor(Color.WHITE);
        nen.setCornerRadius(dp(12));
        nen.setStroke(dp(1), Color.parseColor("#DDDDDD"));

        the.setBackground(nen);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 0, 0, dp(12));

        the.setLayoutParams(params);

        // DONG MA DON VA TRANG THAI
        LinearLayout dongDau = new LinearLayout(this);
        dongDau.setGravity(Gravity.CENTER_VERTICAL);
        dongDau.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtMaDon = taoText(
                "Mã đơn: " + maDon,
                13,
                "#333333",
                true
        );

        dongDau.addView(
                txtMaDon,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        TextView txtTrangThai = taoText(
                trangThai,
                11,
                mauChuTrangThai(trangThai),
                true
        );

        txtTrangThai.setPadding(
                dp(9),
                dp(5),
                dp(9),
                dp(5)
        );

        GradientDrawable nenTrangThai = new GradientDrawable();
        nenTrangThai.setColor(mauNenTrangThai(trangThai));
        nenTrangThai.setCornerRadius(dp(20));

        txtTrangThai.setBackground(nenTrangThai);

        dongDau.addView(txtTrangThai);
        the.addView(dongDau);

        themDuongKe(the);

        // CUA HANG
        TextView txtCuaHang = taoText(
                "Cửa hàng: " + cuaHang,
                13,
                "#222222",
                true
        );

        txtCuaHang.setPadding(0, dp(5), 0, dp(4));
        the.addView(txtCuaHang);

        // NGUOI NHAN
        TextView txtNguoiNhan = taoText(
                "Người nhận: " + nguoiNhan,
                12,
                "#555555",
                false
        );

        the.addView(txtNguoiNhan);

        // DIA CHI
        TextView txtDiaChi = taoText(
                "Địa chỉ: " + diaChi,
                12,
                "#555555",
                false
        );

        txtDiaChi.setPadding(0, dp(4), 0, dp(4));
        the.addView(txtDiaChi);

        // PHUONG THUC THANH TOAN
        TextView txtPhuongThuc = taoText(
                "Thanh toán: " + phuongThuc,
                12,
                "#555555",
                false
        );

        the.addView(txtPhuongThuc);

        themDuongKe(the);

        // DONG TONG TIEN VA NUT DAT LAI
        LinearLayout dongCuoi = new LinearLayout(this);

        dongCuoi.setGravity(Gravity.CENTER_VERTICAL);
        dongCuoi.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtTongTien = taoText(
                "Tổng tiền: " + tongTien,
                14,
                "#E9232D",
                true
        );

        dongCuoi.addView(
                txtTongTien,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        Button btnDatLai = new Button(this);

        btnDatLai.setText("Đặt lại");
        btnDatLai.setTextSize(12);
        btnDatLai.setTextColor(
                Color.parseColor("#E9232D")
        );

        btnDatLai.setAllCaps(false);

        GradientDrawable nenNut = new GradientDrawable();
        nenNut.setColor(Color.WHITE);
        nenNut.setCornerRadius(dp(20));
        nenNut.setStroke(
                dp(1),
                Color.parseColor("#E9232D")
        );

        btnDatLai.setBackground(nenNut);

        btnDatLai.setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Chức năng đặt lại cần kết nối với giỏ hàng.",
                    Toast.LENGTH_SHORT
            ).show();

        });

        dongCuoi.addView(btnDatLai);

        the.addView(dongCuoi);

        layoutDanhSachDonHang.addView(the);
    }

    // TAO TEXTVIEW
    private TextView taoText(
            String noiDung,
            int kichThuoc,
            String mau,
            boolean dam
    ) {

        TextView text = new TextView(this);

        text.setText(noiDung);
        text.setTextSize(kichThuoc);

        text.setTextColor(
                Color.parseColor(mau)
        );

        if (dam) {
            text.setTypeface(null, Typeface.BOLD);
        }

        return text;
    }

    // THEM DUONG KE
    private void themDuongKe(LinearLayout cha) {

        View duongKe = new View(this);

        duongKe.setBackgroundColor(
                Color.parseColor("#EEEEEE")
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(1)
                );

        params.setMargins(0, dp(8), 0, dp(8));

        duongKe.setLayoutParams(params);

        cha.addView(duongKe);
    }

    // MAU NEN TRANG THAI
    private int mauNenTrangThai(String trangThai) {

        if (trangThai.equals(TRANG_THAI_DANG_GIAO)) {
            return Color.parseColor("#FFF0D6");
        }

        if (trangThai.equals(TRANG_THAI_DA_HUY)) {
            return Color.parseColor("#FDE8E8");
        }

        if (trangThai.equals(TRANG_THAI_HOAN_TAT)) {
            return Color.parseColor("#E5F5E9");
        }

        return Color.parseColor("#E5F5E9");
    }

    // MAU CHU TRANG THAI
    private String mauChuTrangThai(String trangThai) {

        if (trangThai.equals(TRANG_THAI_DANG_GIAO)) {
            return "#C47A00";
        }

        if (trangThai.equals(TRANG_THAI_DA_HUY)) {
            return "#D71920";
        }

        if (trangThai.equals(TRANG_THAI_HOAN_TAT)) {
            return "#238443";
        }

        return "#238443";
    }

    // HIEN THI KHI CHUA CO DON HANG
    private void hienThiDonHangTrong() {

        LinearLayout khung = new LinearLayout(this);

        khung.setGravity(Gravity.CENTER);
        khung.setOrientation(LinearLayout.VERTICAL);

        khung.setPadding(
                dp(15),
                dp(65),
                dp(15),
                dp(20)
        );

        TextView bieuTuong = taoText(
                "🛍",
                45,
                "#E9232D",
                false
        );

        TextView tieuDe = taoText(
                "Bạn chưa có đơn hàng",
                17,
                "#333333",
                true
        );

        tieuDe.setPadding(0, dp(12), 0, dp(5));

        TextView moTa = taoText(
                "Các đơn hàng của bạn sẽ xuất hiện tại đây.",
                13,
                "#888888",
                false
        );

        khung.addView(bieuTuong);
        khung.addView(tieuDe);
        khung.addView(moTa);

        layoutDanhSachDonHang.addView(khung);
    }

    // DIEU HUONG CAC TRANG
    private void xuLyDieuHuong() {

        ImageView btnQuayLai =
                findViewById(R.id.btnQuayLaiDonHang);

        btnQuayLai.setOnClickListener(v -> finish());

        findViewById(R.id.navTrangChu).setOnClickListener(v -> {

            startActivity(
                    new Intent(this, TrangChuActivity.class)
            );

            finish();
        });

        findViewById(R.id.navThucDon).setOnClickListener(v -> {

            startActivity(
                    new Intent(this, ThucDonActivity.class)
            );
        });

        findViewById(R.id.navGioHang).setOnClickListener(v -> {

            startActivity(
                    new Intent(this, GioHangActivity.class)
            );
        });

        findViewById(R.id.navTaiKhoan).setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Tài khoản",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // DOI DP SANG PIXEL
    private int dp(float giaTri) {

        return (int) (
                giaTri * getResources()
                        .getDisplayMetrics().density + 0.5f
        );
    }

    // CAP NHAT LAI DANH SACH KHI QUAY TRO LAI
    @Override
    protected void onResume() {
        super.onResume();

        if (layoutDanhSachDonHang != null) {
            hienThiDanhSachDonHang();
        }
    }
}
