package com.example.btl_ltadr;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ThucDonActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachMonAn;
    private MonAnAdapter monAnAdapter;

    private DatabaseHelper databaseHelper;

    private List<MonAn> danhSachMonAn;
    private List<MonAn> danhSachHienThi;

    private EditText edtTimKiem;
    private TextView btnTimKiem;
    private TextView btnSuaDiaChi;
    private ImageView btnQuayLai;

    private LinearLayout btnGioHang;
    private TextView txtSoMon;
    private TextView txtTongTien;

    private Spinner spSapXep;

    private LinearLayout navTrangChu;
    private LinearLayout navThucDon;
    private LinearLayout navGioHang;
    private LinearLayout navDonHang;
    private LinearLayout navTaiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuc_don);

        anhXa();

        databaseHelper = new DatabaseHelper(this);

        taoDanhSachMonAn();
        caiDatSpinner();
        hienThiDanhSachMonAn(danhSachHienThi);

        xuLySuKien();
    }

    private void anhXa() {

        rvDanhSachMonAn = findViewById(R.id.rvDanhSachMonAn);

        edtTimKiem = findViewById(R.id.edtTimKiem);
        btnTimKiem = findViewById(R.id.btnTimKiem);

        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnSuaDiaChi = findViewById(R.id.btnSuaDiaChi);

        btnGioHang = findViewById(R.id.btnGioHang);
        txtSoMon = findViewById(R.id.txtSoMon);
        txtTongTien = findViewById(R.id.txtTongTien);

        spSapXep = findViewById(R.id.spSapXep);

        navTrangChu = findViewById(R.id.navTrangChu);
        navThucDon = findViewById(R.id.navThucDon);
        navGioHang = findViewById(R.id.navGioHang);
        navDonHang = findViewById(R.id.navDonHang);
        navTaiKhoan = findViewById(R.id.navTaiKhoan);
    }

    // ĐỌC DANH SÁCH MÓN ĂN TỪ SQLITE
    private void taoDanhSachMonAn() {

        // Khởi tạo dữ liệu mẫu nếu SQLite chưa có món ăn
        databaseHelper.khoiTaoDuLieuMonAnMacDinh();

        // Lấy danh sách món ăn từ SQLite
        danhSachMonAn = databaseHelper.layDanhSachMonAn();

        // Danh sách ban đầu hiển thị tất cả món
        danhSachHienThi = new ArrayList<>(danhSachMonAn);
    }

    // CÀI ĐẶT SPINNER SẮP XẾP
    private void caiDatSpinner() {

        String[] danhSachSapXep = {
                "Mặc định",
                "Giá thấp đến cao",
                "Giá cao đến thấp"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                danhSachSapXep
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spSapXep.setAdapter(adapter);
    }

    // HIỂN THỊ DANH SÁCH MÓN ĂN
    private void hienThiDanhSachMonAn(List<MonAn> danhSach) {

        rvDanhSachMonAn.setLayoutManager(
                new LinearLayoutManager(this)
        );

        monAnAdapter = new MonAnAdapter(
                danhSach,
                monAn -> moChiTietMonAn(monAn)
        );

        rvDanhSachMonAn.setAdapter(monAnAdapter);
    }

    // MỞ CHI TIẾT MÓN ĂN
    private void moChiTietMonAn(MonAn monAn) {
        if (!kiemTraDiaChiGiaoHang()) {
            return;
        }

        Intent intent = new Intent(this, ChiTietMonAnActivity.class);
        intent.putExtra("maMonAn", monAn.getMaMonAn());
        intent.putExtra("tenMonAn", monAn.getTenMonAn());
        intent.putExtra("giaMonAn", monAn.getGiaMonAn());
        intent.putExtra("giaCu", monAn.getGiaCu());
        intent.putExtra("moTa", monAn.getMoTa());
        intent.putExtra("hinhAnh", monAn.getHinhAnh());

        startActivity(intent);
    }

    // XỬ LÝ SỰ KIỆN
    private void xuLySuKien() {

        // Tìm kiếm khi nhập nội dung
        edtTimKiem.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                locDanhSachMonAn(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Nút tìm kiếm
        btnTimKiem.setOnClickListener(v -> {
            locDanhSachMonAn(
                    edtTimKiem.getText().toString()
            );
            anBanPhim();
        });

        // Nút Search trên bàn phím
        edtTimKiem.setOnEditorActionListener(
                (v, actionId, event) -> {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        locDanhSachMonAn(
                                edtTimKiem.getText().toString()
                        );

                        anBanPhim();
                        return true;
                    }

                    return false;
                }
        );

        // Sắp xếp giá
        spSapXep.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {
                        sapXepDanhSach();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent
                    ) {
                    }
                }
        );

        // Quay lại
        btnQuayLai.setOnClickListener(v -> finish());

        // Sửa địa chỉ giao hàng
        btnSuaDiaChi.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ThucDonActivity.this,
                    DiaChiGiaoHangActivity.class
            );

            startActivity(intent);
        });

        // Mở giỏ hàng
        btnGioHang.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ThucDonActivity.this,
                    GioHangActivity.class
            );

            startActivity(intent);
        });

        // Trang chủ
        navTrangChu.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ThucDonActivity.this,
                    TrangChuActivity.class
            );

            startActivity(intent);
            finish();
        });

        // Thực đơn
        navThucDon.setOnClickListener(v -> {
            // Đang ở trang thực đơn
        });

        // Giỏ hàng
        navGioHang.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ThucDonActivity.this,
                    GioHangActivity.class
            );

            startActivity(intent);
        });

        // Đơn hàng
        navDonHang.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ThucDonActivity.this,
                    DonHangActivity.class
            );

            startActivity(intent);
        });

        // Tài khoản
        navTaiKhoan.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ThucDonActivity.this,
                    TaiKhoanActivity.class
            );

            startActivity(intent);
        });
    }

    // CHUẨN HÓA CHUỖI TIẾNG VIỆT
    private String chuanHoaChuoi(String chuoi) {

        String ketQua = Normalizer.normalize(
                chuoi.toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD
        );

        ketQua = ketQua.replaceAll("\\p{M}", "");
        ketQua = ketQua.replace("đ", "d");

        return ketQua.trim();
    }

    // LỌC MÓN ĂN
    private void locDanhSachMonAn(String tuKhoa) {

        String tuKhoaChuanHoa = chuanHoaChuoi(tuKhoa);

        List<MonAn> ketQua = new ArrayList<>();

        for (MonAn monAn : danhSachMonAn) {

            String tenMon = chuanHoaChuoi(
                    monAn.getTenMonAn()
            );

            if (tenMon.contains(tuKhoaChuanHoa)) {
                ketQua.add(monAn);
            }
        }

        danhSachHienThi = ketQua;

        sapXepDanhSach();
    }

    // SẮP XẾP DANH SÁCH
    private void sapXepDanhSach() {

        if (danhSachHienThi == null) {
            return;
        }

        List<MonAn> ketQua = new ArrayList<>(
                danhSachHienThi
        );

        int viTri = spSapXep.getSelectedItemPosition();

        if (viTri == 1) {

            Collections.sort(
                    ketQua,
                    Comparator.comparingInt(MonAn::getGiaMonAn)
            );

        } else if (viTri == 2) {

            Collections.sort(
                    ketQua,
                    (mon1, mon2) -> Integer.compare(
                            mon2.getGiaMonAn(),
                            mon1.getGiaMonAn()
                    )
            );
        } else {

            Collections.sort(
                    ketQua,
                    Comparator.comparingInt(MonAn::getMaMonAn)
            );
        }

        hienThiDanhSachMonAn(ketQua);
    }

    // CẬP NHẬT THÔNG TIN GIỎ HÀNG
    private void capNhatThongTinGioHang() {

        int tongSoCombo = GioHang.layTongSoCombo();
        int tongTien = GioHang.layTongTien();

        txtSoMon.setText(tongSoCombo + " combo");

        txtTongTien.setText(
                new java.text.DecimalFormat("#,###")
                        .format(tongTien) + " đ"
        );

        if (monAnAdapter != null) {
            monAnAdapter.notifyDataSetChanged();
        }
    }

    private boolean kiemTraDiaChiGiaoHang() {

        android.content.SharedPreferences preferences =
                getSharedPreferences("FOODIE_SESSION", MODE_PRIVATE);

        boolean daDangNhap = preferences.getBoolean(
                "DANG_DANG_NHAP", false
        );

        String soDienThoai = preferences.getString(
                "SO_DIEN_THOAI", ""
        );

        if (!daDangNhap || soDienThoai.trim().isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng đăng nhập trước!",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, DangNhapActivity.class));
            return false;
        }

        // Truy vấn địa chỉ từ SQLite
        KhachHang khachHang = databaseHelper.layKhachHang(
                "KH" + soDienThoai
        );

        if (khachHang == null
                || khachHang.getDiaChiGiaoHang() == null
                || khachHang.getDiaChiGiaoHang().trim().isEmpty()) {

            Toast.makeText(this,
                    "Vui lòng cập nhật địa chỉ giao hàng!",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, DiaChiGiaoHangActivity.class));
            return false;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        capNhatThongTinGioHang();
    }

    // ẨN BÀN PHÍM
    private void anBanPhim() {

        InputMethodManager imm =
                (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );

        if (imm != null) {
            imm.hideSoftInputFromWindow(
                    edtTimKiem.getWindowToken(),
                    0
            );
        }
    }
}
