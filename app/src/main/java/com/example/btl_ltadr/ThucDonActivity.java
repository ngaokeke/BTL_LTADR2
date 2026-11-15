
package com.example.btl_ltadr;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

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

    // Danh sách gốc và danh sách đang hiển thị
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
        taoDanhSachMonAn();
        caiDatSpinner();
        hienThiDanhSachMonAn(danhSachMonAn);
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

    private void taoDanhSachMonAn() {
        danhSachMonAn = DuLieuMonAn.taoDanhSachTatCaMonAn();

        // Sao chép danh sách gốc để lọc và sắp xếp
        danhSachHienThi = new ArrayList<>(danhSachMonAn);
    }

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

    private void moChiTietMonAn(MonAn monAn) {
        Intent intent = new Intent(
                ThucDonActivity.this,
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

    private void xuLySuKien() {

        // Tìm kiếm khi người dùng nhập
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count
            ) {
                locDanhSachMonAn(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Nút tìm kiếm
        btnTimKiem.setOnClickListener(v -> {
            locDanhSachMonAn(edtTimKiem.getText().toString());
            anBanPhim();
        });

        // Nhấn Search trên bàn phím
        edtTimKiem.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                locDanhSachMonAn(edtTimKiem.getText().toString());
                anBanPhim();
                return true;
            }
            return false;
        });

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

        // Bấm vào thanh giỏ hàng
        btnGioHang.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ThucDonActivity.this,
                    GioHangActivity.class
            );
            startActivity(intent);
        });

        // Điều hướng
        navTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ThucDonActivity.this,
                    TrangChuActivity.class
            );
            startActivity(intent);
            finish();
        });

        navThucDon.setOnClickListener(v -> {
            // Đang ở trang thực đơn
        });

        navGioHang.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ThucDonActivity.this,
                    GioHangActivity.class
            );
            startActivity(intent);
        });

        navDonHang.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ThucDonActivity.this,
                    DonHangActivity.class
            );
            startActivity(intent);
        });

        navTaiKhoan.setOnClickListener(v ->{
                Intent intent = new Intent(
                ThucDonActivity.this,
                TaiKhoanActivity.class
        );
        startActivity(intent);
    });
    }

    // Lọc món ăn theo tên, không phân biệt chữ hoa và dấu tiếng Việt
    private void locDanhSachMonAn(String tuKhoa) {
        String tuKhoaChuanHoa = chuanHoaChuoi(tuKhoa);

        List<MonAn> ketQua = new ArrayList<>();

        for (MonAn monAn : danhSachMonAn) {
            String tenMon = chuanHoaChuoi(monAn.getTenMonAn());

            if (tenMon.contains(tuKhoaChuanHoa)) {
                ketQua.add(monAn);
            }
        }

        danhSachHienThi = ketQua;
        sapXepDanhSach();

        if (ketQua.isEmpty() && !tuKhoa.trim().isEmpty()) {
            // Không hiển thị Toast ở đây để tránh Toast liên tục khi đang nhập.
        }
    }

    // Chuẩn hóa chuỗi để tìm kiếm không dấu
    private String chuanHoaChuoi(String chuoi) {
        String ketQua = Normalizer.normalize(
                chuoi.toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD
        );

        ketQua = ketQua.replaceAll("\\p{M}", "");
        ketQua = ketQua.replace("đ", "d");

        return ketQua.trim();
    }

    // Sắp xếp danh sách đang hiển thị
    private void sapXepDanhSach() {
        if (danhSachHienThi == null) {
            return;
        }

        List<MonAn> ketQua = new ArrayList<>(danhSachHienThi);

        int viTri = spSapXep.getSelectedItemPosition();

        if (viTri == 1) {
            Collections.sort(
                    ketQua,
                    Comparator.comparingInt(MonAn::getGiaMonAn)
            );
        } else if (viTri == 2) {
            Collections.sort(
                    ketQua,
                    (mon1, mon2) ->
                            Integer.compare(
                                    mon2.getGiaMonAn(),
                                    mon1.getGiaMonAn()
                            )
            );
        }

        hienThiDanhSachMonAn(ketQua);
    }

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

    @Override
    protected void onResume() {
        super.onResume();

        capNhatThongTinGioHang();
    }


    // Ẩn bàn phím sau khi tìm kiếm
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
