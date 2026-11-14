
package com.example.btl_ltadr;

import java.util.ArrayList;
import java.util.List;

public class MonAn {

    private int maMonAn;
    private String tenMonAn;
    private int giaMonAn;
    private int giaCu;
    private String moTa;
    private int hinhAnh;

    private List<NhomTuyChon> danhSachNhomTuyChon;

    // Constructor cũ để tương thích với code hiện tại
    public MonAn(int maMonAn, String tenMonAn,
            int giaMonAn, int giaCu, String moTa,
            int hinhAnh) {
        this(maMonAn, tenMonAn, giaMonAn, giaCu,
                moTa, hinhAnh, new ArrayList<>());
    }

    // Constructor mới có danh sách tùy chọn
    public MonAn(int maMonAn, String tenMonAn,
            int giaMonAn, int giaCu, String moTa,
            int hinhAnh,
            List<NhomTuyChon> danhSachNhomTuyChon
    ) {
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.giaMonAn = giaMonAn;
        this.giaCu = giaCu;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.danhSachNhomTuyChon = danhSachNhomTuyChon;
    }

    public int getMaMonAn() {
        return maMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public int getGiaMonAn() {
        return giaMonAn;
    }

    public int getGiaCu() {
        return giaCu;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public List<NhomTuyChon> getDanhSachNhomTuyChon() {
        return danhSachNhomTuyChon;
    }
}
