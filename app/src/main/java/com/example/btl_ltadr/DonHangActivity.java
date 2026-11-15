
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

    // LẤY SỐ ĐIỆN THOẠI TÀI KHOẢN ĐANG ĐĂNG NHẬP
    private static String laySoDienThoaiDangNhap(Context context) {

        SharedPreferences session = context.getSharedPreferences(
                "FOODIE_SESSION",
                Context.MODE_PRIVATE
        );

        boolean daDangNhap = session.getBoolean(
                "DANG_DANG_NHAP",
                false
        );

        if (!daDangNhap) {
            return "";
        }

        String soDienThoai = session.getString(
                "SO_DIEN_THOAI",
                ""
        );

        return soDienThoai == null ? "" : soDienThoai.trim();
    }

    // ÁNH XẠ CÁC VIEW THEO XML
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

    // LƯU ĐƠN HÀNG SAU KHI ĐẶT HÀNG THÀNH CÔNG
    public static void luuDonHang(
            Context context,
            String maDon,
            String tongTien,
            String phuongThuc,
            String diaChi,
            String cuaHang,
            String nguoiNhan
    ) {

        luuDonHang(
                context,
                maDon,
                tongTien,
                phuongThuc,
                diaChi,
                cuaHang,
                nguoiNhan,
                "[]"
        );
    }

    // LƯU ĐƠN HÀNG KÈM DANH SÁCH COMBO
    public static void luuDonHang(
            Context context,
            String maDon,
            String tongTien,
            String phuongThuc,
            String diaChi,
            String cuaHang,
            String nguoiNhan,
            String danhSachMonJson
    ) {

        // KIỂM TRA MÃ ĐƠN
        if (maDon == null || maDon.trim().isEmpty()) {
            return;
        }

        // LẤY TÀI KHOẢN ĐANG ĐĂNG NHẬP
        String soDienThoaiChuDon =
                laySoDienThoaiDangNhap(context);

        // CHƯA ĐĂNG NHẬP THÌ KHÔNG LƯU ĐƠN
        if (soDienThoaiChuDon.isEmpty()) {
            return;
        }

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

        // KIỂM TRA TRÙNG MÃ ĐƠN TRONG CÙNG TÀI KHOẢN
        for (int i = 0; i < danhSach.length(); i++) {

            JSONObject don = danhSach.optJSONObject(i);

            if (don != null &&
                    maDon.equals(don.optString("maDon")) &&
                    soDienThoaiChuDon.equals(
                            don.optString("soDienThoaiChuDon", "")
                    )) {

                return;
            }
        }

        JSONObject donHang = new JSONObject();

        try {

            donHang.put("maDon", maDon);
            donHang.put("tongTien", tongTien == null ? "0" : tongTien);
            donHang.put("phuongThuc", phuongThuc == null ? "Tiền mặt" : phuongThuc);
            donHang.put("diaChi", diaChi == null ? "" : diaChi);
            donHang.put("cuaHang", cuaHang == null ? "" : cuaHang);
            donHang.put("nguoiNhan", nguoiNhan == null ? "" : nguoiNhan);

            donHang.put(
                    "trangThai",
                    TRANG_THAI_DAT_THANH_CONG
            );

            // LƯU DANH SÁCH COMBO
            JSONArray danhSachMon;

            try {
                danhSachMon = new JSONArray(
                        danhSachMonJson == null ||
                                danhSachMonJson.trim().isEmpty()
                                ? "[]"
                                : danhSachMonJson
                );
            } catch (JSONException e) {
                danhSachMon = new JSONArray();
            }

            donHang.put("danhSachMon", danhSachMon);

            // GẮN ĐƠN HÀNG VỚI TÀI KHOẢN ĐẶT HÀNG
            donHang.put(
                    "soDienThoaiChuDon",
                    soDienThoaiChuDon
            );

            danhSach.put(donHang);

            preferences.edit()
                    .putString(KEY_DON_HANG, danhSach.toString())
                    .apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG CỦA TÀI KHOẢN HIỆN TẠI
    public static void capNhatTrangThai(
            Context context,
            String maDon,
            String trangThaiMoi
    ) {

        if (maDon == null || maDon.trim().isEmpty() ||
                trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) {
            return;
        }

        String soDienThoaiChuDon =
                laySoDienThoaiDangNhap(context);

        if (soDienThoaiChuDon.isEmpty()) {
            return;
        }

        SharedPreferences preferences =
                context.getSharedPreferences(
                        TEN_PREF,
                        Context.MODE_PRIVATE
                );

        try {

            JSONArray danhSach = new JSONArray(
                    preferences.getString(KEY_DON_HANG, "[]")
            );

            boolean daCapNhat = false;

            for (int i = 0; i < danhSach.length(); i++) {

                JSONObject don = danhSach.optJSONObject(i);

                if (don != null &&
                        maDon.equals(don.optString("maDon")) &&
                        soDienThoaiChuDon.equals(
                                don.optString("soDienThoaiChuDon", "")
                        )) {

                    don.put("trangThai", trangThaiMoi);
                    daCapNhat = true;
                    break;
                }
            }

            if (daCapNhat) {
                preferences.edit()
                        .putString(KEY_DON_HANG, danhSach.toString())
                        .apply();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // LẤY DANH SÁCH ĐƠN HÀNG ĐÃ LƯU
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

    // XỬ LÝ 5 TAB
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

    // CẬP NHẬT GIAO DIỆN TAB ĐƯỢC CHỌN
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

    // HIỂN THỊ DANH SÁCH ĐƠN HÀNG CỦA TÀI KHOẢN HIỆN TẠI
    private void hienThiDanhSachDonHang() {

        layoutDanhSachDonHang.removeAllViews();

        String soDienThoaiChuDon =
                laySoDienThoaiDangNhap(this);

        if (soDienThoaiChuDon.isEmpty()) {

            hienThiDonHangTrong();
            return;
        }

        JSONArray danhSach = layDanhSachDonHang();

        int soDonHienThi = 0;

        // ĐƠN MỚI NHẤT HIỂN THỊ TRƯỚC
        for (int i = danhSach.length() - 1; i >= 0; i--) {

            JSONObject don = danhSach.optJSONObject(i);

            if (don == null) {
                continue;
            }

            // KIỂM TRA CHỦ SỞ HỮU ĐƠN HÀNG
            String soDienThoaiDon = don.optString(
                    "soDienThoaiChuDon",
                    ""
            );

            if (!soDienThoaiChuDon.equals(soDienThoaiDon)) {
                continue;
            }

            String trangThai = don.optString("trangThai");

            // LỌC ĐƠN THEO TAB
            if (!trangThaiDangChon.equals("Tất cả")
                    && !trangThaiDangChon.equals(trangThai)) {
                continue;
            }

            taoTheDonHang(don);

            soDonHienThi++;
        }

        // KHÔNG CÓ ĐƠN HÀNG
        if (soDonHienThi == 0) {
            hienThiDonHangTrong();
        }
    }

    // TẠO GIAO DIỆN MỘT ĐƠN HÀNG
    private void taoTheDonHang(JSONObject don) {

        String maDon = don.optString("maDon");
        String tongTien = don.optString("tongTien");
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

        // DÒNG MÃ ĐƠN VÀ TRẠNG THÁI
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

        // DANH SÁCH COMBO
        JSONArray danhSachMon = don.optJSONArray("danhSachMon");

        if (danhSachMon != null && danhSachMon.length() > 0) {

            for (int i = 0; i < danhSachMon.length(); i++) {

                JSONObject mon = danhSachMon.optJSONObject(i);

                if (mon == null) {
                    continue;
                }

                LinearLayout dongMon = new LinearLayout(this);
                dongMon.setGravity(Gravity.CENTER_VERTICAL);
                dongMon.setOrientation(LinearLayout.HORIZONTAL);
                dongMon.setPadding(0, dp(5), 0, dp(5));

                // ẢNH COMBO
                ImageView imgMon = new ImageView(this);

                int hinhAnh = mon.optInt("hinhAnh", 0);

                if (hinhAnh != 0) {
                    try {
                        imgMon.setImageResource(hinhAnh);
                    } catch (Exception e) {
                        imgMon.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } else {
                    imgMon.setImageResource(android.R.drawable.ic_menu_gallery);
                }

                imgMon.setScaleType(ImageView.ScaleType.CENTER_CROP);

                LinearLayout.LayoutParams imgParams =
                        new LinearLayout.LayoutParams(
                                dp(75),
                                dp(75)
                        );

                imgParams.setMargins(0, 0, dp(10), 0);

                dongMon.addView(imgMon, imgParams);

                // TÊN VÀ GIÁ COMBO
                LinearLayout thongTin = new LinearLayout(this);
                thongTin.setOrientation(LinearLayout.VERTICAL);

                String tenMon = mon.optString(
                        "tenMonAn",
                        "Combo"
                );

                String gia = mon.optString(
                        "giaMotCombo",
                        mon.optString("gia", "0")
                );

                TextView txtTenMon = taoText(
                        tenMon,
                        14,
                        "#222222",
                        true
                );

                TextView txtGia = taoText(
                        gia,
                        13,
                        "#E9232D",
                        true
                );

                thongTin.addView(txtTenMon);
                thongTin.addView(txtGia);

                dongMon.addView(
                        thongTin,
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        )
                );

                the.addView(dongMon);
            }

        } else {

            TextView txtTrong = taoText(
                    "Chưa có thông tin chi tiết combo",
                    13,
                    "#888888",
                    false
            );

            txtTrong.setPadding(0, dp(10), 0, dp(10));
            the.addView(txtTrong);
        }

        themDuongKe(the);

        // DÒNG TỔNG TIỀN VÀ NÚT ĐẶT LẠI
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
                    "Chức năng đặt lại sẽ được kết nối với giỏ hàng.",
                    Toast.LENGTH_SHORT
            ).show();

        });

        dongCuoi.addView(btnDatLai);

        the.addView(dongCuoi);

        layoutDanhSachDonHang.addView(the);
    }

    // TẠO TEXTVIEW
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

    // THÊM ĐƯỜNG KẺ
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

    // MÀU NỀN TRẠNG THÁI
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

    // MÀU CHỮ TRẠNG THÁI
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

    // HIỂN THỊ KHI CHƯA CÓ ĐƠN HÀNG
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

    // ĐIỀU HƯỚNG CÁC TRANG
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

            startActivity(
                    new Intent(this, TaiKhoanActivity.class)
            );
        });
    }

    // ĐỔI DP SANG PIXEL
    private int dp(float giaTri) {

        return (int) (
                giaTri * getResources()
                        .getDisplayMetrics().density + 0.5f
        );
    }

    // CẬP NHẬT LẠI DANH SÁCH KHI QUAY TRỞ LẠI
    @Override
    protected void onResume() {
        super.onResume();

        if (layoutDanhSachDonHang != null) {
            hienThiDanhSachDonHang();
        }
    }
}
