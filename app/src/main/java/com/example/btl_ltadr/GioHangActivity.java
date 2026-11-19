
package com.example.btl_ltadr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GioHangActivity extends AppCompatActivity {

    private ImageButton btnQuayLai;

    private RadioGroup rgThanhToan;

    private TextView txtDiaChi;
    private TextView txtSuaDiaChi;
    private TextView txtTongTien;
    private TextView txtTamTinh;
    private TextView txtPhiVanChuyen;
    private TextView txtSoSanPham;
    private TextView btnCoupon;

    private LinearLayout llDanhSachMon;

    private EditText edtMaGiamGia;
    private EditText edtGhiChu;

    private Button btnApDungMa;
    private Button btnDatHang;

    private Switch swDungCu;

    private int phiVanChuyen = 15000;
    private int giamGia = 0;

    // SQLite ưu đãi
    private DatabaseHelper databaseHelper;
    private String maGiamGiaDangApDung = "";
    private int phanTramGiamDangApDung = 0;

    private final DecimalFormat dinhDangTien =
            new DecimalFormat("#,###");

    // Xin quyền thông báo Android 13 trở lên
    private final ActivityResultLauncher<String> xinQuyenThongBao =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Toast.makeText(
                                    this,
                                    "Đã bật thông báo đơn hàng",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        anhXa();

        // Khởi tạo SQLite
        databaseHelper = new DatabaseHelper(this);

        // Tạo kênh thông báo
        ThongBaoDonHang.taoKenhThongBao(this);

        // Xin quyền thông báo
        if (Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

            xinQuyenThongBao.launch(
                    Manifest.permission.POST_NOTIFICATIONS
            );
        }

        caiDatSuKien();
        hienThiDanhSachMon();
        capNhatTongTien();
    }

    // Ánh xạ giao diện
    private void anhXa() {

        btnQuayLai = findViewById(R.id.btnQuayLai);

        rgThanhToan = findViewById(R.id.rgThanhToan);

        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtSuaDiaChi = findViewById(R.id.txtSuaDiaChi);

        txtTongTien = findViewById(R.id.txtTongTien);
        txtTamTinh = findViewById(R.id.txtTamTinh);
        txtPhiVanChuyen = findViewById(R.id.txtPhiVanChuyen);
        txtSoSanPham = findViewById(R.id.txtSoSanPham);

        btnCoupon = findViewById(R.id.btnCoupon);

        llDanhSachMon = findViewById(R.id.llDanhSachMon);

        edtMaGiamGia = findViewById(R.id.edtMaGiamGia);
        edtGhiChu = findViewById(R.id.edtGhiChu);

        btnApDungMa = findViewById(R.id.btnApDungMa);
        btnDatHang = findViewById(R.id.btnDatHang);

        swDungCu = findViewById(R.id.swDungCu);
    }

    // Cài đặt sự kiện
    private void caiDatSuKien() {

        btnQuayLai.setOnClickListener(v -> finish());

        // Sửa địa chỉ giao hàng
        txtSuaDiaChi.setOnClickListener(v -> {
            Intent intent = new Intent(
                    GioHangActivity.this,
                    DiaChiGiaoHangActivity.class
            );
            startActivity(intent);
        });

        // Mở danh sách ưu đãi
        btnCoupon.setOnClickListener(v -> hienThiUuDai());

        // Áp dụng mã giảm giá
        btnApDungMa.setOnClickListener(v -> apDungMaGiamGia());

        // Đặt hàng
        btnDatHang.setOnClickListener(v -> datHang());
    }

    // Hiển thị danh sách ưu đãi từ SQLite
    private void hienThiUuDai() {

        ArrayList<UuDai> danhSachUuDai =
                databaseHelper.layDanhSachUuDai();

        if (danhSachUuDai == null || danhSachUuDai.isEmpty()) {
            Toast.makeText(
                    this,
                    "Hiện chưa có chương trình ưu đãi",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(10), dp(20), dp(10));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Ưu đãi dành cho bạn")
                .setView(layout)
                .setNegativeButton("Đóng", null)
                .create();

        for (UuDai uuDai : danhSachUuDai) {

            LinearLayout khung = new LinearLayout(this);
            khung.setOrientation(LinearLayout.VERTICAL);
            khung.setPadding(dp(12), dp(12), dp(12), dp(12));
            khung.setBackgroundColor(Color.rgb(248, 248, 248));

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(0, dp(5), 0, dp(10));
            khung.setLayoutParams(params);

            TextView txtTen = new TextView(this);
            txtTen.setText(uuDai.getTenUuDai());
            txtTen.setTextSize(18);
            txtTen.setTextColor(Color.RED);
            txtTen.setTypeface(null, Typeface.BOLD);

            TextView txtMa = new TextView(this);
            txtMa.setText("Mã ưu đãi: " + uuDai.getMaUuDai());
            txtMa.setTextSize(14);
            txtMa.setTextColor(Color.DKGRAY);

            TextView txtMoTa = new TextView(this);
            txtMoTa.setText(uuDai.getMoTa());
            txtMoTa.setTextSize(14);
            txtMoTa.setTextColor(Color.DKGRAY);
            txtMoTa.setPadding(0, dp(5), 0, dp(5));

            TextView txtPhanTram = new TextView(this);
            txtPhanTram.setText(
                    "Giảm " + uuDai.getPhanTramGiam() + "%"
            );
            txtPhanTram.setTextSize(16);
            txtPhanTram.setTextColor(Color.RED);
            txtPhanTram.setTypeface(null, Typeface.BOLD);

            Button btnSuDung = new Button(this);
            btnSuDung.setText("Sử dụng");

            btnSuDung.setOnClickListener(v -> {

                edtMaGiamGia.setText(uuDai.getMaUuDai());

                apDungMaGiamGia();

                dialog.dismiss();
            });

            khung.addView(txtTen);
            khung.addView(txtMa);
            khung.addView(txtMoTa);
            khung.addView(txtPhanTram);
            khung.addView(btnSuDung);

            layout.addView(khung);
        }

        dialog.show();
    }

    // Hiển thị danh sách món ăn trong giỏ hàng
    private void hienThiDanhSachMon() {

        llDanhSachMon.removeAllViews();

        ArrayList<GioHang.SanPhamTrongGio> danhSach =
                GioHang.layDanhSach();

        if (danhSach.isEmpty()) {

            TextView txtTrong = new TextView(this);
            txtTrong.setText("Giỏ hàng của bạn đang trống");
            txtTrong.setTextSize(16);
            txtTrong.setTextColor(Color.GRAY);
            txtTrong.setGravity(Gravity.CENTER);
            txtTrong.setPadding(10, 35, 10, 35);

            llDanhSachMon.addView(txtTrong);
            return;
        }

        for (GioHang.SanPhamTrongGio mon : danhSach) {
            taoDongMonAn(mon);
        }
    }

    // Tạo từng dòng món ăn
    private void taoDongMonAn(GioHang.SanPhamTrongGio mon) {

        LinearLayout khung = new LinearLayout(this);
        khung.setOrientation(LinearLayout.HORIZONTAL);
        khung.setGravity(Gravity.CENTER_VERTICAL);
        khung.setBackgroundColor(Color.WHITE);
        khung.setPadding(dp(10), dp(12), dp(10), dp(12));

        LinearLayout.LayoutParams khungParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        khungParams.setMargins(0, dp(7), 0, dp(7));

        // Ảnh món ăn
        ImageView anh = new ImageView(this);

        if (mon.hinhAnh != 0) {
            anh.setImageResource(mon.hinhAnh);
        } else {
            anh.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        anh.setScaleType(ImageView.ScaleType.CENTER_CROP);

        khung.addView(
                anh,
                new LinearLayout.LayoutParams(dp(95), dp(95))
        );

        // Thông tin món ăn
        LinearLayout thongTin = new LinearLayout(this);
        thongTin.setOrientation(LinearLayout.VERTICAL);
        thongTin.setPadding(dp(12), 0, 0, 0);

        LinearLayout.LayoutParams thongTinParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        TextView txtTen = new TextView(this);
        txtTen.setText(mon.tenMonAn);
        txtTen.setTextColor(Color.BLACK);
        txtTen.setTextSize(17);
        txtTen.setTypeface(null, Typeface.BOLD);

        thongTin.addView(txtTen);

        TextView txtGia = new TextView(this);
        txtGia.setText(
                dinhDangTien.format(mon.giaMotCombo) + " đ"
        );
        txtGia.setTextColor(Color.RED);
        txtGia.setTextSize(17);
        txtGia.setTypeface(null, Typeface.BOLD);
        txtGia.setPadding(0, dp(5), 0, dp(7));

        thongTin.addView(txtGia);

        // Hàng số lượng
        LinearLayout hangSoLuong = new LinearLayout(this);
        hangSoLuong.setOrientation(LinearLayout.HORIZONTAL);
        hangSoLuong.setGravity(Gravity.CENTER_VERTICAL);

        Button btnGiam = new Button(this);
        btnGiam.setText("-");
        btnGiam.setMinWidth(dp(40));

        TextView txtSoLuong = new TextView(this);
        txtSoLuong.setText(String.valueOf(mon.soLuong));
        txtSoLuong.setTextColor(Color.BLACK);
        txtSoLuong.setTextSize(16);
        txtSoLuong.setGravity(Gravity.CENTER);
        txtSoLuong.setPadding(dp(12), 0, dp(12), 0);

        Button btnTang = new Button(this);
        btnTang.setText("+");
        btnTang.setMinWidth(dp(40));

        btnTang.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        Color.rgb(237, 37, 43)
                )
        );
        btnTang.setTextColor(Color.WHITE);

        hangSoLuong.addView(btnGiam);
        hangSoLuong.addView(txtSoLuong);
        hangSoLuong.addView(btnTang);

        thongTin.addView(hangSoLuong);
        khung.addView(thongTin, thongTinParams);

        // Nút xóa món
        ImageButton btnXoa = new ImageButton(this);
        btnXoa.setImageResource(
                android.R.drawable.ic_menu_delete
        );
        btnXoa.setBackgroundColor(Color.TRANSPARENT);
        btnXoa.setContentDescription("Xóa món");

        khung.addView(
                btnXoa,
                new LinearLayout.LayoutParams(dp(38), dp(45))
        );

        llDanhSachMon.addView(khung, khungParams);

        // Giảm số lượng
        btnGiam.setOnClickListener(v -> {
            GioHang.giamSoLuong(mon.maMonAn);
            hienThiDanhSachMon();
            capNhatTongTien();
        });

        // Tăng số lượng
        btnTang.setOnClickListener(v -> {
            GioHang.tangSoLuong(mon.maMonAn);
            hienThiDanhSachMon();
            capNhatTongTien();
        });

        // Xóa món
        btnXoa.setOnClickListener(v -> {
            GioHang.xoaMon(mon.maMonAn);
            hienThiDanhSachMon();
            capNhatTongTien();
        });
    }

    // Cập nhật tổng tiền
    private void capNhatTongTien() {

        int tamTinh = GioHang.layTongTien();
        int soSanPham = GioHang.layDanhSach().size();

        // Tính lại giảm giá khi số lượng món thay đổi
        if (!maGiamGiaDangApDung.isEmpty()) {
            giamGia = tamTinh * phanTramGiamDangApDung / 100;
        } else {
            giamGia = 0;
        }

        txtTamTinh.setText(
                "Tạm tính: " + dinhDangTien.format(tamTinh) + " đ"
        );

        txtPhiVanChuyen.setText(
                "Phí vận chuyển: "
                        + dinhDangTien.format(phiVanChuyen) + " đ"
        );

        int tongTien = tamTinh + phiVanChuyen - giamGia;

        if (tongTien < 0) {
            tongTien = 0;
        }

        txtTongTien.setText(
                dinhDangTien.format(tongTien) + " đ"
        );

        txtSoSanPham.setText(
                "Tổng tiền (" + soSanPham + " sản phẩm)"
        );
    }

    // Áp dụng mã giảm giá từ SQLite
    private void apDungMaGiamGia() {

        String ma = edtMaGiamGia.getText()
                .toString()
                .trim()
                .toUpperCase(Locale.ROOT);

        int tamTinh = GioHang.layTongTien();

        if (tamTinh <= 0) {
            Toast.makeText(
                    this,
                    "Giỏ hàng đang trống",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        ArrayList<UuDai> danhSachUuDai =
                databaseHelper.layDanhSachUuDai();

        UuDai uuDaiTimThay = null;

        for (UuDai uuDai : danhSachUuDai) {

            if (uuDai.getMaUuDai() != null &&
                    uuDai.getMaUuDai().trim()
                            .equalsIgnoreCase(ma)) {

                uuDaiTimThay = uuDai;
                break;
            }
        }

        if (uuDaiTimThay == null) {

            maGiamGiaDangApDung = "";
            phanTramGiamDangApDung = 0;
            giamGia = 0;

            Toast.makeText(
                    this,
                    "Mã giảm giá không hợp lệ",
                    Toast.LENGTH_SHORT
            ).show();

            capNhatTongTien();
            return;
        }

        int phanTram = uuDaiTimThay.getPhanTramGiam();

        if (phanTram <= 0 || phanTram > 100) {

            maGiamGiaDangApDung = "";
            phanTramGiamDangApDung = 0;
            giamGia = 0;

            Toast.makeText(
                    this,
                    "Phần trăm giảm giá không hợp lệ",
                    Toast.LENGTH_SHORT
            ).show();

            capNhatTongTien();
            return;
        }

        maGiamGiaDangApDung = uuDaiTimThay.getMaUuDai();
        phanTramGiamDangApDung = phanTram;

        giamGia = tamTinh * phanTramGiamDangApDung / 100;

        Toast.makeText(
                this,
                "Áp dụng mã giảm giá thành công",
                Toast.LENGTH_SHORT
        ).show();

        capNhatTongTien();
    }

    // Xử lý đặt hàng
    private void datHang() {

        ArrayList<GioHang.SanPhamTrongGio> danhSach =
                GioHang.layDanhSach();

        if (danhSach.isEmpty()) {
            Toast.makeText(
                    this,
                    "Giỏ hàng đang trống",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Kiểm tra phương thức thanh toán
        int idDuocChon = rgThanhToan.getCheckedRadioButtonId();

        if (idDuocChon == -1) {
            Toast.makeText(
                    this,
                    "Vui lòng chọn phương thức thanh toán",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        RadioButton radioButton = findViewById(idDuocChon);
        String phuongThuc = radioButton.getText().toString();

        String ghiChu = edtGhiChu.getText().toString().trim();

        String dungCu = swDungCu.isChecked()
                ? "Có lấy dụng cụ ăn uống"
                : "Không lấy dụng cụ ăn uống";

        // Tính tổng tiền
        int tongTien = GioHang.layTongTien()
                + phiVanChuyen
                - giamGia;

        if (tongTien < 0) {
            tongTien = 0;
        }

        // Tạo mã đơn
        String maDon = "#DH" +
                (System.currentTimeMillis() % 100000000);

        String diaChi = txtDiaChi.getText().toString();

        // Chuyển danh sách combo sang JSON
        JSONArray danhSachMonJson = new JSONArray();

        try {
            for (GioHang.SanPhamTrongGio mon : danhSach) {

                JSONObject object = new JSONObject();

                object.put("maMonAn", mon.maMonAn);
                object.put("tenMonAn", mon.tenMonAn);
                object.put("hinhAnh", mon.hinhAnh);
                object.put("giaMotCombo", mon.giaMotCombo);
                object.put("soLuong", mon.soLuong);

                danhSachMonJson.put(object);
            }

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Không thể lưu thông tin sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Chuyển sang màn hình đặt hàng thành công
        Intent intent = new Intent(
                GioHangActivity.this,
                DatHangThanhCongActivity.class
        );

        intent.putExtra("maDon", maDon);
        intent.putExtra("tongTien", tongTien);
        intent.putExtra("phuongThuc", phuongThuc);
        intent.putExtra("diaChi", diaChi);
        intent.putExtra("cuaHang", "TAN MAI");
        intent.putExtra("nguoiNhan", "Khách hàng");
        intent.putExtra("ghiChu", ghiChu);
        intent.putExtra("dungCu", dungCu);

        intent.putExtra(
                "danhSachMon",
                danhSachMonJson.toString()
        );

        // Gửi thông báo đặt hàng
        ThongBaoDonHang.guiThongBao(
                GioHangActivity.this,
                "Đặt hàng thành công",
                "Đơn hàng " + maDon
                        + " đã được ghi nhận. Tổng thanh toán: "
                        + dinhDangTien.format(tongTien) + " đ",
                intent,
                1001
        );

        // Xóa giỏ hàng
        GioHang.xoaTatCa();

        startActivity(intent);
        finish();
    }

    // Chuyển đổi dp
    private int dp(int giaTri) {
        return (int) (
                giaTri * getResources()
                        .getDisplayMetrics().density
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (llDanhSachMon != null) {
            hienThiDanhSachMon();
            capNhatTongTien();
        }
    }
}
