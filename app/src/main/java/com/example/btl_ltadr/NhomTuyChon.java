package com.example.btl_ltadr;

import java.util.List;

public class NhomTuyChon {

    private String tenNhom;
    private int soLuongToiThieu;
    private int soLuongToiDa;
    private List<TuyChonMon> danhSachTuyChon;

    public NhomTuyChon(
            String tenNhom,
            int soLuongToiThieu,
            int soLuongToiDa,
            List<TuyChonMon> danhSachTuyChon
    ) {
        this.tenNhom = tenNhom;
        this.soLuongToiThieu = soLuongToiThieu;
        this.soLuongToiDa = soLuongToiDa;
        this.danhSachTuyChon = danhSachTuyChon;
    }

    public String getTenNhom() {
        return tenNhom;
    }

    public int getSoLuongToiThieu() {
        return soLuongToiThieu;
    }

    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public List<TuyChonMon> getDanhSachTuyChon() {
        return danhSachTuyChon;
    }
}
