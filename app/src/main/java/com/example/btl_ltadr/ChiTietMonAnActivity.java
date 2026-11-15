package com.example.btl_ltadr;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.content.SharedPreferences;

public class ChiTietMonAnActivity extends AppCompatActivity {
    private ImageView imgHinhAnh;
    private TextView btnQuayLai;
    private TextView btnYeuThich;
    private TextView txtTietKiem;
    private boolean daYeuThich = false;
    private TextView txtTenMonAn;
    private TextView txtGiaMonAn;
    private TextView txtGiaCu;
    private TextView txtMoTa;
    private TextView btnGiamSoLuong;
    private TextView btnTangSoLuong;
    private TextView txtSoLuong;
    private TextView btnThemGioHang;
    private LinearLayout llNhomTuyChon;
    private MonAn monAn;
    private int soLuongCombo = 1;
    private HashMap<TuyChonMon, Integer> soLuongTuyChon =
            new HashMap<>();
    private DecimalFormat dinhDangTien =
            new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietmonan);

        anhXa();

        layThongTinMonAn();

        if (monAn == null) {
            Toast.makeText(
                    this,
                    "Không tìm thấy sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        hienThiThongTinMonAn();
        khoiTaoSoLuongTuyChon();
        hienThiNhomTuyChon();
        capNhatTongTien();
        xuLySuKien();
    }

    // ÁNH XẠ VIEW
    private void anhXa() {

        imgHinhAnh = findViewById(R.id.imgHinhAnh);

        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnYeuThich = findViewById(R.id.btnYeuThich);
        txtTietKiem = findViewById(R.id.txtTietKiem);

        txtTenMonAn = findViewById(R.id.txtTenMonAn);
        txtGiaMonAn = findViewById(R.id.txtGiaMonAn);
        txtGiaCu = findViewById(R.id.txtGiaCu);
        txtMoTa = findViewById(R.id.txtMoTa);

        llNhomTuyChon = findViewById(R.id.llNhomTuyChon);

        btnGiamSoLuong = findViewById(R.id.btnGiamSoLuong);
        btnTangSoLuong = findViewById(R.id.btnTangSoLuong);
        txtSoLuong = findViewById(R.id.txtSoLuong);

        btnThemGioHang = findViewById(R.id.btnThemGioHang);
    }

    // LẤY SẢN PHẨM
    private void layThongTinMonAn() {

        int maMonAn = getIntent().getIntExtra("maMonAn", -1);

        ArrayList<MonAn> danhSach =
                DuLieuMonAn.taoDanhSachTatCaMonAn();

        for (MonAn item : danhSach) {

            if (item.getMaMonAn() == maMonAn) {
                monAn = item;
                break;
            }
        }
    }

    // HIỂN THỊ THÔNG TIN SẢN PHẨM
    private void hienThiThongTinMonAn() {

        txtTenMonAn.setText(monAn.getTenMonAn());

        txtGiaMonAn.setText(
                dinhDangTien.format(monAn.getGiaMonAn()) + " đ"
        );

        txtGiaMonAn.setTextColor(
                Color.rgb(220, 25, 35)
        );

        txtGiaCu.setText(
                dinhDangTien.format(monAn.getGiaCu()) + " đ"
        );

        txtGiaCu.setTextColor(Color.GRAY);

        txtGiaCu.setPaintFlags(
                txtGiaCu.getPaintFlags() |
                        Paint.STRIKE_THRU_TEXT_FLAG
        );

        txtMoTa.setText(monAn.getMoTa());

        if (monAn.getHinhAnh() != 0) {
            imgHinhAnh.setImageResource(monAn.getHinhAnh());
        }
        SharedPreferences prefs = getSharedPreferences("MonAnYeuThich", MODE_PRIVATE);
        daYeuThich = prefs.getBoolean("fav_" + monAn.getMaMonAn(), false);
        capNhatIconYeuThich();
    }

    // KHỞI TẠO SỐ LƯỢNG MẶC ĐỊNH
    private void khoiTaoSoLuongTuyChon() {

        soLuongTuyChon.clear();

        List<NhomTuyChon> danhSachNhom =
                monAn.getDanhSachNhomTuyChon();

        if (danhSachNhom == null) {
            return;
        }

        for (NhomTuyChon nhom : danhSachNhom) {

            for (TuyChonMon tuyChon :
                    nhom.getDanhSachTuyChon()) {

                soLuongTuyChon.put(
                        tuyChon,
                        tuyChon.getSoLuongMacDinh()
                );
            }
        }
    }

    // HIỂN THỊ CÁC NHÓM TÙY CHỌN
    private void hienThiNhomTuyChon() {

        llNhomTuyChon.removeAllViews();

        List<NhomTuyChon> danhSachNhom =
                monAn.getDanhSachNhomTuyChon();

        if (danhSachNhom == null) {
            return;
        }

        for (NhomTuyChon nhom : danhSachNhom) {

            LinearLayout khuVucNhom = new LinearLayout(this);

            khuVucNhom.setOrientation(
                    LinearLayout.VERTICAL
            );

            khuVucNhom.setPadding(
                    0,
                    dp(12),
                    0,
                    dp(10)
            );

            // TIÊU ĐỀ NHÓM
            LinearLayout header = new LinearLayout(this);

            header.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            header.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            TextView txtTenNhom = new TextView(this);

            txtTenNhom.setText(nhom.getTenNhom().toUpperCase());
            txtTenNhom.setTextSize(16);
            txtTenNhom.setTextSize(14);
            txtTenNhom.setTextColor(Color.BLACK);
            txtTenNhom.setTypeface(null, Typeface.BOLD);

            LinearLayout.LayoutParams tenParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );

            header.addView(txtTenNhom, tenParams);

            TextView txtGioiHan = new TextView(this);

            txtGioiHan.setText(
                    taoChuoiGioiHan(nhom)
            );

            txtGioiHan.setTextSize(10);
            txtGioiHan.setTextColor(Color.DKGRAY);

            header.addView(txtGioiHan);

            khuVucNhom.addView(header);

            // CÁC HÀNG TÙY CHỌN
            for (TuyChonMon tuyChon :
                    nhom.getDanhSachTuyChon()) {

                LinearLayout dong = taoDongTuyChon(
                        nhom,
                        tuyChon
                );

                khuVucNhom.addView(dong);
            }

            // ĐƯỜNG KẺ
            View duongKe = new View(this);

            duongKe.setBackgroundColor(
                    Color.rgb(130, 130, 130)
            );

            LinearLayout.LayoutParams lineParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dp(1)
                    );

            lineParams.topMargin = dp(8);

            khuVucNhom.addView(
                    duongKe,
                    lineParams
            );

            llNhomTuyChon.addView(khuVucNhom);
        }
    }

    // HIỂN THỊ GIỚI HẠN LỰA CHỌN
    private String taoChuoiGioiHan(NhomTuyChon nhom) {

        int toiThieu = nhom.getSoLuongToiThieu();
        int toiDa = nhom.getSoLuongToiDa();

        if (toiThieu == 0) {
            return "Tối đa " + toiDa + " món";
        }

        return "Chọn ít nhất " + toiThieu +
                " món, tối đa " + toiDa;
    }

    // TẠO HÀNG TÙY CHỌN
    private LinearLayout taoDongTuyChon(NhomTuyChon nhom, TuyChonMon tuyChon) {
        int soLuong = laySoLuong(tuyChon);

        LinearLayout dong = new LinearLayout(this);
        dong.setOrientation(LinearLayout.HORIZONTAL);
        dong.setGravity(Gravity.CENTER_VERTICAL);
        dong.setMinimumHeight(dp(42));
        dong.setPadding(0, dp(7), 0, dp(7));
        dong.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // 1. SỐ LƯỢNG
        TextView txtSoLuongDong = new TextView(this);
        txtSoLuongDong.setTextSize(14);
        txtSoLuongDong.setWidth(dp(34));
        capNhatTextSoLuong(txtSoLuongDong, tuyChon);

        // 2. TÊN MÓN
        TextView txtTen = new TextView(this);
        txtTen.setText(tuyChon.getTenTuyChon());
        txtTen.setTextSize(15);
        txtTen.setTextColor(Color.rgb(34, 34, 34));

        LinearLayout.LayoutParams tenParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        );
        tenParams.rightMargin = dp(8);

        // 3. GIÁ THÊM
        TextView txtGiaThem = new TextView(this);
        txtGiaThem.setText("+ " + dinhDangTien.format(tuyChon.getGiaThem()) + " đ");
        txtGiaThem.setTextSize(13);
        txtGiaThem.setTextColor(Color.rgb(110, 110, 110));

        // 4. NÚT TRÒN DẤU X MÀU XÁM ĐỂ HỦY CHỌN MÓN NÀY (CHỈ HIỆN KHI > 0)
        TextView btnXoaTuyChon = new TextView(this);
        btnXoaTuyChon.setText("✕");
        btnXoaTuyChon.setTextSize(11);
        btnXoaTuyChon.setTextColor(Color.WHITE);
        btnXoaTuyChon.setTypeface(null, Typeface.BOLD);
        btnXoaTuyChon.setGravity(Gravity.CENTER);

        GradientDrawable bgXoa = new GradientDrawable();
        bgXoa.setShape(GradientDrawable.OVAL);
        bgXoa.setColor(Color.rgb(185, 185, 185));
        btnXoaTuyChon.setBackground(bgXoa);

        LinearLayout.LayoutParams xoaParams = new LinearLayout.LayoutParams(dp(20), dp(20));
        xoaParams.leftMargin = dp(10);
        btnXoaTuyChon.setVisibility(soLuong > 0 ? View.VISIBLE : View.GONE);

        btnXoaTuyChon.setOnClickListener(v -> {
            soLuongTuyChon.put(tuyChon, 0);
            hienThiNhomTuyChon();
            capNhatTongTien();
        });

        dong.addView(txtSoLuongDong);
        dong.addView(txtTen, tenParams);
        dong.addView(txtGiaThem);
        dong.addView(btnXoaTuyChon, xoaParams);

        // NHẤN VÀO HÀNG ĐỂ CHỌN THÊM
        dong.setOnClickListener(v -> xuLyChonTuyChon(nhom, tuyChon));

        return dong;
    }

    // XỬ LÝ KHI NHẤN VÀO MỘT LỰA CHỌN
    private void xuLyChonTuyChon(
            NhomTuyChon nhom,
            TuyChonMon tuyChon
    ) {

        int soLuongHienTai = laySoLuong(tuyChon);
        int tongNhom = tinhTongSoLuongNhom(nhom);
        int gioiHanToiDa = nhom.getSoLuongToiDa();

        // Đã đạt tối đa ở chính lựa chọn này:
        // nhấn tiếp thì reset toàn bộ nhóm.
        if (soLuongHienTai >= gioiHanToiDa) {

            for (TuyChonMon item : nhom.getDanhSachTuyChon()) {
                soLuongTuyChon.put(item, 0);
            }

        } else if (tongNhom < gioiHanToiDa) {

            // Chưa đủ số lượng tối đa:
            // tăng lựa chọn vừa nhấn, giữ nguyên lựa chọn khác.
            soLuongTuyChon.put(
                    tuyChon,
                    soLuongHienTai + 1
            );

        } else {

            // Đã đủ số lượng tối đa:
            // chuyển toàn bộ sang lựa chọn vừa nhấn.
            for (TuyChonMon item : nhom.getDanhSachTuyChon()) {
                soLuongTuyChon.put(item, 0);
            }

            soLuongTuyChon.put(
                    tuyChon,
                    soLuongHienTai + 1
            );
        }

        hienThiNhomTuyChon();
        capNhatTongTien();
    }



    // LẤY SỐ LƯỢNG ĐÃ CHỌN
    private int laySoLuong(TuyChonMon tuyChon) {

        Integer soLuong = soLuongTuyChon.get(tuyChon);

        return soLuong == null ? 0 : soLuong;
    }

    // CẬP NHẬT SỐ LƯỢNG VÀ ĐỔI MÀU ĐỎ TRÊN GIAO DIỆN
    private void capNhatTextSoLuong(TextView txt, TuyChonMon tuyChon) {
        int sl = laySoLuong(tuyChon);
        txt.setText("x" + sl);
        if (sl > 0) {
            txt.setTextColor(Color.rgb(215, 25, 32));
            txt.setTypeface(null, Typeface.BOLD);
        } else {
            txt.setTextColor(Color.rgb(175, 175, 175));
            txt.setTypeface(null, Typeface.NORMAL);
        }
    }

    // TÍNH TỔNG SỐ LƯỢNG TRONG MỘT NHÓM
    private int tinhTongSoLuongNhom(NhomTuyChon nhom) {

        int tong = 0;

        for (TuyChonMon tuyChon :
                nhom.getDanhSachTuyChon()) {

            tong += laySoLuong(tuyChon);
        }

        return tong;
    }

    // TÍNH GIÁ MỘT COMBO
    private int tinhGiaMotCombo() {

        int tong = monAn.getGiaMonAn();

        List<NhomTuyChon> danhSachNhom =
                monAn.getDanhSachNhomTuyChon();

        if (danhSachNhom != null) {

            for (NhomTuyChon nhom : danhSachNhom) {

                for (TuyChonMon tuyChon :
                        nhom.getDanhSachTuyChon()) {

                    tong += laySoLuong(tuyChon) *
                            tuyChon.getGiaThem();
                }
            }
        }

        return tong;
    }

    // CẬP NHẬT TỔNG TIỀN
    private void capNhatTongTien() {
        int tongTien = tinhGiaMotCombo() * soLuongCombo;

        txtSoLuong.setText(String.valueOf(soLuongCombo));

        btnThemGioHang.setText(
                "Thêm giỏ hàng: " + dinhDangTien.format(tongTien) + " đ"
        );

        int tietKiemMotPhan = Math.max(0, monAn.getGiaCu() - monAn.getGiaMonAn());
        int tongTietKiem = tietKiemMotPhan * soLuongCombo;

        if (tongTietKiem > 0) {
            txtTietKiem.setVisibility(View.VISIBLE);
            String soTienStr = dinhDangTien.format(tongTietKiem) + " đ";
            String fullText = "Bạn tiết kiệm được " + soTienStr + " sau khi giảm giá.";
            SpannableString spannable = new SpannableString(fullText);

            int start = fullText.indexOf(soTienStr);
            int end = start + soTienStr.length();
            spannable.setSpan(new ForegroundColorSpan(Color.rgb(215, 25, 32)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtTietKiem.setText(spannable);
        } else {
            txtTietKiem.setVisibility(View.GONE);
        }
    }

    // KIỂM TRA SỐ LƯỢNG TRƯỚC KHI THÊM GIỎ HÀNG
    private boolean kiemTraLuaChon() {

        List<NhomTuyChon> danhSachNhom =
                monAn.getDanhSachNhomTuyChon();

        if (danhSachNhom == null) {
            return true;
        }

        for (NhomTuyChon nhom : danhSachNhom) {

            int tong = tinhTongSoLuongNhom(nhom);

            if (tong < nhom.getSoLuongToiThieu()) {

                Toast.makeText(
                        this,
                        "Vui lòng chọn đủ " +
                                nhom.getTenNhom(),
                        Toast.LENGTH_SHORT
                ).show();

                return false;
            }

            if (tong > nhom.getSoLuongToiDa()) {

                Toast.makeText(
                        this,
                        "Đã vượt quá số lượng " +
                                nhom.getTenNhom(),
                        Toast.LENGTH_SHORT
                ).show();

                return false;
            }
        }

        return true;
    }

    // XỬ LÝ SỰ KIỆN
    private void xuLySuKien() {

        // QUAY LẠI
        btnQuayLai.setOnClickListener(v -> finish());

        // YÊU THÍCH
        btnYeuThich.setOnClickListener(v -> {
            daYeuThich = !daYeuThich;
            SharedPreferences prefs = getSharedPreferences("MonAnYeuThich", MODE_PRIVATE);
            prefs.edit().putBoolean("fav_" + monAn.getMaMonAn(), daYeuThich).apply();
            capNhatIconYeuThich();

            Toast.makeText(
                    this,
                    daYeuThich ? "Đã thêm vào yêu thích" : "Đã bỏ yêu thích",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // GIẢM SỐ LƯỢNG COMBO
        btnGiamSoLuong.setOnClickListener(v -> {

            if (soLuongCombo > 1) {
                soLuongCombo--;
                capNhatTongTien();
            }
        });

        // TĂNG SỐ LƯỢNG COMBO
        btnTangSoLuong.setOnClickListener(v -> {

            soLuongCombo++;
            capNhatTongTien();
        });

        // THÊM GIỎ HÀNG

        btnThemGioHang.setOnClickListener(v -> {

            if (!kiemTraLuaChon()) {
                return;
            }

            int giaMotCombo = tinhGiaMotCombo();

            // Lưu sản phẩm và số lượng vào giỏ hàng
            GioHang.themMon(
                    monAn.getMaMonAn(),
                    monAn.getTenMonAn(),
                    soLuongCombo,
                    tinhGiaMotCombo(),
                    monAn.getHinhAnh()
            );

            Toast.makeText(
                    this,
                    "Đã thêm " + soLuongCombo + " " +
                            monAn.getTenMonAn() + " vào giỏ hàng",
                    Toast.LENGTH_SHORT
            ).show();

            // Quay về trang Thực đơn
            finish();
        });
    }

    private void capNhatIconYeuThich() {
        if (daYeuThich) {
            btnYeuThich.setText("♥");
            btnYeuThich.setTextColor(Color.rgb(215, 25, 32));
        } else {
            btnYeuThich.setText("♡");
            btnYeuThich.setTextColor(Color.rgb(51, 51, 51));
        }
    }

    // ĐỔI DP SANG PX
    private int dp(int value) {

        return (int) (
                value * getResources()
                        .getDisplayMetrics().density + 0.5f
        );
    }
}
