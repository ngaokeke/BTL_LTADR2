
package com.example.btl_ltadr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachMonAn;
    private RecyclerView rvDanhSachUuDai;

    private MonAnAdapter monAnAdapter;
    private UuDaiAdapter uuDaiAdapter;

    private DatabaseHelper databaseHelper;

    private List<MonAn> danhSachMonAn;
    private List<UuDai> danhSachUuDai;

    private TextView btnDatHangNgay;
    private TextView btnXemThem;
    private TextView btnMenu;

    private LinearLayout navTrangChu;
    private LinearLayout navThucDon;
    private LinearLayout navGioHang;
    private LinearLayout navDonHang;
    private LinearLayout navTaiKhoan;

    private View btnGioHangNoi;

    private LinearLayout layoutHeaderChuaDangNhap;
    private LinearLayout layoutHeaderDaDangNhap;

    private TextView btnDangNhap;
    private TextView txtDiaChiChuaDangNhap;
    private ImageButton btnQuetMaChuaDangNhap;
    private ImageButton btnThongBaoChuaDangNhap;

    private TextView txtLoiChao;
    private TextView txtDiaChiGiaoHang;
    private TextView txtDiemRIA;
    private TextView txtHangThanhVien;

    private ImageView imgGaRanHeader;

    private ImageButton btnQuetMa;
    private ImageButton btnThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trangchu_activity);

        anhXa();

        databaseHelper = new DatabaseHelper(this);

        hienThiThongTinDangNhap();

        taoDanhSachMonAn();
        taoDanhSachUuDai();

        hienThiDanhSachMonAn();
        hienThiDanhSachUuDai();

        xuLySuKien();
        xuLySuKienHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            hienThiThongTinDangNhap();
            capNhatDanhSachUuDai();
        }
    }

    // ================= THONG TIN DANG NHAP =================

    private void hienThiThongTinDangNhap() {

        SharedPreferences session = getSharedPreferences(
                "FOODIE_SESSION",
                MODE_PRIVATE
        );

        boolean daDangNhap = session.getBoolean(
                "DANG_DANG_NHAP",
                false
        );

        if (daDangNhap) {

            String ten = session.getString(
                    "TEN_KHACH_HANG",
                    ""
            );

            String sdt = session.getString(
                    "SO_DIEN_THOAI",
                    ""
            );

            KhachHang khachHang = databaseHelper.layKhachHang(
                    "KH" + sdt
            );

            if (khachHang == null) {
                khachHang = new KhachHang(
                        "KH" + sdt,
                        ten,
                        sdt,
                        ""
                );
            }

            session.edit()
                    .putString(
                            "TEN_KHACH_HANG",
                            khachHang.getTenKhachHang()
                    )
                    .apply();

            hienThiHeaderDaDangNhap(khachHang);

        } else {
            hienThiHeaderChuaDangNhap();
        }
    }

    // ================= ANH XA =================

    private void anhXa() {

        rvDanhSachMonAn = findViewById(R.id.rvDanhSachMonAn);
        rvDanhSachUuDai = findViewById(R.id.rvDanhSachUuDai);

        btnDatHangNgay = findViewById(R.id.btnDatHangNgay);
        btnXemThem = findViewById(R.id.btnXemThem);
        btnMenu = findViewById(R.id.btnMenu);

        navTrangChu = findViewById(R.id.navTrangChu);
        navThucDon = findViewById(R.id.navThucDon);
        navGioHang = findViewById(R.id.navGioHang);
        navDonHang = findViewById(R.id.navDonHang);
        navTaiKhoan = findViewById(R.id.navTaiKhoan);

        btnGioHangNoi = findViewById(R.id.btnGioHangNoi);

        layoutHeaderChuaDangNhap =
                findViewById(R.id.layoutHeaderChuaDangNhap);

        layoutHeaderDaDangNhap =
                findViewById(R.id.layoutHeaderDaDangNhap);

        btnDangNhap = findViewById(R.id.btnDangNhap);

        txtDiaChiChuaDangNhap =
                findViewById(R.id.txtDiaChiChuaDangNhap);

        btnQuetMaChuaDangNhap =
                findViewById(R.id.btnQuetMaChuaDangNhap);

        btnThongBaoChuaDangNhap =
                findViewById(R.id.btnThongBaoChuaDangNhap);

        txtLoiChao = findViewById(R.id.txtLoiChao);
        txtDiaChiGiaoHang = findViewById(R.id.txtDiaChiGiaoHang);
        txtDiemRIA = findViewById(R.id.txtDiemRIA);
        txtHangThanhVien = findViewById(R.id.txtHangThanhVien);

        imgGaRanHeader = findViewById(R.id.imgGaRanHeader);

        btnQuetMa = findViewById(R.id.btnQuetMa);
        btnThongBao = findViewById(R.id.btnThongBao);
    }

    // ================= HEADER =================

    private void hienThiHeaderChuaDangNhap() {

        layoutHeaderChuaDangNhap.setVisibility(View.VISIBLE);
        layoutHeaderDaDangNhap.setVisibility(View.GONE);
    }

    private void hienThiHeaderDaDangNhap(KhachHang khachHang) {

        if (khachHang == null) {
            hienThiHeaderChuaDangNhap();
            return;
        }

        layoutHeaderChuaDangNhap.setVisibility(View.GONE);
        layoutHeaderDaDangNhap.setVisibility(View.VISIBLE);

        String ten = khachHang.getTenKhachHang();

        if (ten == null || ten.trim().isEmpty()) {
            txtLoiChao.setText("Kính chào!");
        } else {
            txtLoiChao.setText("Kính chào, " + ten.trim() + "!");
        }

        imgGaRanHeader.setImageResource(R.drawable.duiga);
        imgGaRanHeader.setVisibility(View.VISIBLE);
    }

    public void capNhatHeader(KhachHang khachHang) {
        hienThiHeaderDaDangNhap(khachHang);
    }

    // ================= KIEM TRA DIA CHI =================

    private boolean kiemTraDiaChiGiaoHang() {

        SharedPreferences preferences = getSharedPreferences(
                "FOODIE_SESSION",
                MODE_PRIVATE
        );

        boolean daDangNhap = preferences.getBoolean(
                "DANG_DANG_NHAP",
                false
        );

        String soDienThoai = preferences.getString(
                "SO_DIEN_THOAI",
                ""
        );

        if (!daDangNhap || soDienThoai.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Vui lòng đăng nhập trước!",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(new Intent(this, DangNhapActivity.class));
            return false;
        }

        KhachHang khachHang = databaseHelper.layKhachHang(
                "KH" + soDienThoai
        );

        if (khachHang == null
                || khachHang.getDiaChiGiaoHang() == null
                || khachHang.getDiaChiGiaoHang().trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Vui lòng cập nhật địa chỉ giao hàng!",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(new Intent(this, DiaChiGiaoHangActivity.class));
            return false;
        }

        return true;
    }

    // ================= CHI TIET MON AN =================

    private void moChiTietMonAn(MonAn monAn) {

        if (!kiemTraDiaChiGiaoHang()) {
            return;
        }

        Intent intent = new Intent(
                this,
                ChiTietMonAnActivity.class
        );

        intent.putExtra("maMonAn", monAn.getMaMonAn());
        intent.putExtra("tenMonAn", monAn.getTenMonAn());
        intent.putExtra("giaMonAn", monAn.getGiaMonAn());
        intent.putExtra("giaCu", monAn.getGiaCu());
        intent.putExtra("moTa", monAn.getMoTa());
        intent.putExtra("hinhAnh", monAn.getHinhAnh());

        startActivity(intent);
    }

    // ================= SU KIEN HEADER =================

    private void xuLySuKienHeader() {

        btnDangNhap.setOnClickListener(v -> {

            startActivity(new Intent(
                    TrangChuActivity.this,
                    DangNhapActivity.class
            ));
        });

        btnQuetMaChuaDangNhap.setOnClickListener(v ->
                Toast.makeText(this, "Quét mã QR", Toast.LENGTH_SHORT).show()
        );

        btnThongBaoChuaDangNhap.setOnClickListener(v ->
                Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show()
        );

        btnQuetMa.setOnClickListener(v ->
                Toast.makeText(this, "Quét mã QR", Toast.LENGTH_SHORT).show()
        );

        btnThongBao.setOnClickListener(v ->
                Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show()
        );

        txtDiaChiChuaDangNhap.setOnClickListener(v -> {

            SharedPreferences session = getSharedPreferences(
                    "FOODIE_SESSION",
                    MODE_PRIVATE
            );

            if (!session.getBoolean("DANG_DANG_NHAP", false)) {
                startActivity(new Intent(this, DangNhapActivity.class));
            } else {
                startActivity(new Intent(this, DiaChiGiaoHangActivity.class));
            }
        });

        txtDiaChiGiaoHang.setOnClickListener(v -> {

            if (!kiemTraDangNhap()) {
                return;
            }

            startActivity(new Intent(
                    this,
                    DiaChiGiaoHangActivity.class
            ));
        });
    }

    private boolean kiemTraDangNhap() {

        SharedPreferences session = getSharedPreferences(
                "FOODIE_SESSION",
                MODE_PRIVATE
        );

        if (!session.getBoolean("DANG_DANG_NHAP", false)) {

            Toast.makeText(
                    this,
                    "Vui lòng đăng nhập trước!",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(new Intent(this, DangNhapActivity.class));
            return false;
        }

        return true;
    }

    // ================= MON AN SQLITE =================

    private void taoDanhSachMonAn() {

        databaseHelper.khoiTaoDuLieuMonAnMacDinh();

        ArrayList<MonAn> tatCaMon =
                databaseHelper.layDanhSachMonAn();

        danhSachMonAn = new ArrayList<>();

        for (MonAn monAn : tatCaMon) {

            if (monAn.getMaMonAn() >= 1
                    && monAn.getMaMonAn() <= 7) {

                danhSachMonAn.add(monAn);
            }
        }
    }

    // ================= UU DAI SQLITE =================

    private void taoDanhSachUuDai() {

        // Thêm dữ liệu mẫu nếu mã ưu đãi chưa tồn tại.
        databaseHelper.themUuDai(new UuDai(
                "FOODIE20",
                "Giảm giá đặc biệt",
                "Áp dụng cho đơn hàng đủ điều kiện",
                20,
                R.drawable.uudai
        ));

        databaseHelper.themUuDai(new UuDai(
                "FOODIE30",
                "Ưu đãi thành viên",
                "Áp dụng cho đơn hàng từ 200.000đ",
                30,
                R.drawable.uudai
        ));

        databaseHelper.themUuDai(new UuDai(
                "COMBO15",
                "Combo tiết kiệm",
                "Tiết kiệm khi mua combo",
                15,
                R.drawable.uudai
        ));

        databaseHelper.themUuDai(new UuDai(
                "WEEKEND10",
                "Ưu đãi cuối tuần",
                "Áp dụng cho đơn hàng cuối tuần",
                10,
                R.drawable.uudai
        ));

        // Đọc dữ liệu từ SQLite.
        danhSachUuDai = databaseHelper.layDanhSachUuDai();
    }

    // Đọc lại danh sách ưu đãi khi quay về trang chủ.
    private void capNhatDanhSachUuDai() {

        danhSachUuDai = databaseHelper.layDanhSachUuDai();

        if (uuDaiAdapter != null) {
            uuDaiAdapter.capNhatDanhSach(danhSachUuDai);
        }
    }

    // ================= HIEN THI MON AN =================

    private void hienThiDanhSachMonAn() {

        LinearLayoutManager boCucMonAn =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                );

        rvDanhSachMonAn.setLayoutManager(boCucMonAn);
        rvDanhSachMonAn.setNestedScrollingEnabled(false);

        monAnAdapter = new MonAnAdapter(
                danhSachMonAn,
                monAn -> moChiTietMonAn(monAn)
        );

        rvDanhSachMonAn.setAdapter(monAnAdapter);
    }

    // ================= HIEN THI UU DAI =================

    private void hienThiDanhSachUuDai() {

        LinearLayoutManager boCucUuDai =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        rvDanhSachUuDai.setLayoutManager(boCucUuDai);
        rvDanhSachUuDai.setNestedScrollingEnabled(false);

        uuDaiAdapter = new UuDaiAdapter(danhSachUuDai);

        rvDanhSachUuDai.setAdapter(uuDaiAdapter);
    }

    // ================= CHUYEN MAN HINH =================

    private void moTrangGioHang() {

        startActivity(new Intent(
                this,
                GioHangActivity.class
        ));
    }

    private void moTrangUuDai() {

        startActivity(new Intent(
                this,
                UuDaiActivity.class
        ));
    }

    private void moTrangThucDon() {

        startActivity(new Intent(
                this,
                ThucDonActivity.class
        ));
    }

    // ================= SU KIEN =================

    private void xuLySuKien() {

        btnDatHangNgay.setOnClickListener(v -> {

            if (!kiemTraDiaChiGiaoHang()) {
                return;
            }

            moTrangThucDon();
        });

        btnXemThem.setOnClickListener(v -> moTrangUuDai());

        btnMenu.setOnClickListener(v -> moTrangThucDon());

        navTrangChu.setOnClickListener(v ->
                Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show()
        );

        navThucDon.setOnClickListener(v -> moTrangThucDon());

        navGioHang.setOnClickListener(v -> moTrangGioHang());

        btnGioHangNoi.setOnClickListener(v -> moTrangGioHang());

        navDonHang.setOnClickListener(v -> {

            startActivity(new Intent(
                    this,
                    DonHangActivity.class
            ));
        });

        navTaiKhoan.setOnClickListener(v -> {

            startActivity(new Intent(
                    this,
                    TaiKhoanActivity.class
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
