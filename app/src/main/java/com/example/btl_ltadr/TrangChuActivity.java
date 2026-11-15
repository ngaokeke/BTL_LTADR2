package com.example.btl_ltadr;

import android.content.Intent;
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

    RecyclerView rvDanhSachMonAn;
    RecyclerView rvDanhSachUuDai;

    MonAnAdapter monAnAdapter;
    UuDaiAdapter uuDaiAdapter;

    List<MonAn> danhSachMonAn;
    List<UuDai> danhSachUuDai;

    TextView btnDatHangNgay;

    LinearLayout navTrangChu;
    LinearLayout navThucDon;
    LinearLayout navGioHang;
    LinearLayout navDonHang;
    LinearLayout navTaiKhoan;

    View btnGioHangNoi;

    LinearLayout layoutHeaderChuaDangNhap;
    LinearLayout layoutHeaderDaDangNhap;

    TextView btnDangNhap;
    TextView txtDiaChiChuaDangNhap;
    ImageButton btnQuetMaChuaDangNhap;
    ImageButton btnThongBaoChuaDangNhap;

    TextView txtLoiChao;
    TextView txtDiaChiGiaoHang;
    TextView txtDiemRIA;
    TextView txtHangThanhVien;
    ImageView imgGaRanHeader;

    ImageButton btnQuetMa;
    ImageButton btnThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trangchu_activity);

        anhXa();

        String ten = getIntent().getStringExtra("TEN_KHACH_HANG");
        String sdt = getIntent().getStringExtra("SO_DIEN_THOAI");

        if (ten != null && sdt != null) {
            KhachHang khachHang = new KhachHang(
                    "",
                    ten,
                    sdt,
                    ""
            );

            hienThiHeaderDaDangNhap(khachHang);
        } else {
            hienThiHeaderChuaDangNhap();
        }

        taoDanhSachMonAn();
        taoDanhSachUuDai();

        hienThiDanhSachMonAn();
        hienThiDanhSachUuDai();

        xuLySuKien();
        xuLySuKienHeader();
    }

    private void anhXa() {
        rvDanhSachMonAn = findViewById(R.id.rvDanhSachMonAn);
        rvDanhSachUuDai = findViewById(R.id.rvDanhSachUuDai);

        btnDatHangNgay = findViewById(R.id.btnDatHangNgay);

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

    private void xuLySuKienHeader() {
        btnDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(
                    TrangChuActivity.this,
                    DangNhapActivity.class
            );
            startActivity(intent);
        });

        btnQuetMaChuaDangNhap.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Quét mã QR",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnThongBaoChuaDangNhap.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Thông báo",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnQuetMa.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Quét mã QR",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnThongBao.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Thông báo",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void taoDanhSachMonAn() {
        danhSachMonAn = DuLieuMonAn.taoDanhSachBanChay();
    }

    private void taoDanhSachUuDai() {
        danhSachUuDai = new ArrayList<>();

        danhSachUuDai.add(new UuDai(
                1,
                "Ưu đãi thành viên",
                "Ưu đãi dành cho thành viên.",
                R.drawable.uudai
        ));

        danhSachUuDai.add(new UuDai(
                2,
                "Giảm giá đặc biệt",
                "Nhận ưu đãi khi đặt hàng.",
                R.drawable.uudai
        ));

        danhSachUuDai.add(new UuDai(
                3,
                "Combo tiết kiệm",
                "Tiết kiệm khi mua combo.",
                R.drawable.uudai
        ));

        danhSachUuDai.add(new UuDai(
                4,
                "Ưu đãi cuối tuần",
                "Ưu đãi dành cho cuối tuần.",
                R.drawable.uudai
        ));
    }

    private void hienThiDanhSachMonAn() {
        LinearLayoutManager boCucMonAn =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                );

        rvDanhSachMonAn.setLayoutManager(boCucMonAn);

        monAnAdapter = new MonAnAdapter(
                danhSachMonAn,
                monAn -> {
                    Intent intent = new Intent(
                            TrangChuActivity.this,
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
        );

        rvDanhSachMonAn.setAdapter(monAnAdapter);
    }

    private void hienThiDanhSachUuDai() {
        LinearLayoutManager boCucUuDai =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        rvDanhSachUuDai.setLayoutManager(boCucUuDai);

        uuDaiAdapter = new UuDaiAdapter(danhSachUuDai);

        rvDanhSachUuDai.setAdapter(uuDaiAdapter);
    }

    private void moTrangGioHang() {
        Intent intent = new Intent(
                TrangChuActivity.this,
                GioHangActivity.class
        );

        startActivity(intent);
    }

    private void xuLySuKien() {
        btnDatHangNgay.setOnClickListener(v -> {
            Intent intent = new Intent(
                    TrangChuActivity.this,
                    ThucDonActivity.class
            );

            startActivity(intent);
        });

        navTrangChu.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Trang chủ",
                    Toast.LENGTH_SHORT
            ).show();
        });

        navThucDon.setOnClickListener(v -> {
            Intent intent = new Intent(
                    TrangChuActivity.this,
                    ThucDonActivity.class
            );

            startActivity(intent);
        });

        navGioHang.setOnClickListener(v -> moTrangGioHang());

        btnGioHangNoi.setOnClickListener(v -> moTrangGioHang());

        navDonHang.setOnClickListener(v -> {
            Intent intent = new Intent(
                    TrangChuActivity.this,
                    DonHangActivity.class
            );

            startActivity(intent);
        });

        navTaiKhoan.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Tài khoản",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}