
package com.example.btl_ltadr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GioHang {

    public static class SanPhamTrongGio {
        public int maMonAn;
        public String tenMonAn;
        public int soLuong;
        public int giaMotCombo;
        public int hinhAnh;

        // Constructor cũ: giữ nguyên để code cũ tiếp tục hoạt động
        public SanPhamTrongGio(
                int maMonAn,
                String tenMonAn,
                int soLuong,
                int giaMotCombo
        ) {
            this.maMonAn = maMonAn;
            this.tenMonAn = tenMonAn;
            this.soLuong = soLuong;
            this.giaMotCombo = giaMotCombo;
        }

        // Constructor mới: dùng khi cần lưu hình ảnh
        public SanPhamTrongGio(
                int maMonAn,
                String tenMonAn,
                int soLuong,
                int giaMotCombo,
                int hinhAnh
        ) {
            this(maMonAn, tenMonAn, soLuong, giaMotCombo);
            this.hinhAnh = hinhAnh;
        }

        public int getThanhTien() {
            return soLuong * giaMotCombo;
        }
    }

    private static final Map<Integer, SanPhamTrongGio> danhSach =
            new HashMap<>();

    // Thêm món vào giỏ hàng
    // Hàm themMon cũ: giữ nguyên để code cũ tiếp tục hoạt động
    public static void themMon(
            int maMonAn,
            String tenMonAn,
            int soLuong,
            int giaMotCombo
    ) {
        themMon(
                maMonAn,
                tenMonAn,
                soLuong,
                giaMotCombo,
                0
        );
    }

    // Hàm themMon mới: có thêm hình ảnh
    public static void themMon(
            int maMonAn,
            String tenMonAn,
            int soLuong,
            int giaMotCombo,
            int hinhAnh
    ) {
        if (soLuong <= 0) return;

        SanPhamTrongGio sanPham = danhSach.get(maMonAn);

        if (sanPham == null) {
            danhSach.put(
                    maMonAn,
                    new SanPhamTrongGio(
                            maMonAn,
                            tenMonAn,
                            soLuong,
                            giaMotCombo,
                            hinhAnh
                    )
            );
        } else {
            int tongSoLuong = sanPham.soLuong + soLuong;

            int tongTien = sanPham.getThanhTien()
                    + soLuong * giaMotCombo;

            sanPham.soLuong = tongSoLuong;
            sanPham.giaMotCombo = tongTien / tongSoLuong;

            if (hinhAnh != 0) {
                sanPham.hinhAnh = hinhAnh;
            }
        }
    }

    // Lấy số lượng của một món
    public static int laySoLuong(int maMonAn) {
        SanPhamTrongGio sp = danhSach.get(maMonAn);
        return sp == null ? 0 : sp.soLuong;
    }

    // Lấy tổng số combo trong giỏ
    public static int layTongSoCombo() {
        int tong = 0;

        for (SanPhamTrongGio sp : danhSach.values()) {
            tong += sp.soLuong;
        }

        return tong;
    }

    // Lấy tổng tiền giỏ hàng
    public static int layTongTien() {
        int tong = 0;

        for (SanPhamTrongGio sp : danhSach.values()) {
            tong += sp.getThanhTien();
        }

        return tong;
    }

    // Lấy danh sách sản phẩm trong giỏ hàng
    public static ArrayList<SanPhamTrongGio> layDanhSach() {
        return new ArrayList<>(danhSach.values());
    }

    // Tăng số lượng sản phẩm
    public static void tangSoLuong(int maMonAn) {
        SanPhamTrongGio sp = danhSach.get(maMonAn);

        if (sp != null) {
            sp.soLuong++;
        }
    }

    // Giảm số lượng sản phẩm
    public static void giamSoLuong(int maMonAn) {
        SanPhamTrongGio sp = danhSach.get(maMonAn);

        if (sp != null) {
            if (sp.soLuong > 1) {
                sp.soLuong--;
            } else {
                danhSach.remove(maMonAn);
            }
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public static void xoaMon(int maMonAn) {
        danhSach.remove(maMonAn);
    }

    // Xóa toàn bộ giỏ hàng
    public static void xoaTatCa() {
        danhSach.clear();
    }
}
