package com.example.btl_ltadr;

public class TuyChonMon {

    private String tenTuyChon;
    private int giaThem;
    private int soLuongMacDinh;

    public TuyChonMon(String tenTuyChon, int giaThem, int soLuongMacDinh) {
        this.tenTuyChon = tenTuyChon;
        this.giaThem = giaThem;
        this.soLuongMacDinh = soLuongMacDinh;
    }

    public String getTenTuyChon() {
        return tenTuyChon;
    }

    public int getGiaThem() {
        return giaThem;
    }

    public int getSoLuongMacDinh() {
        return soLuongMacDinh;
    }

    public void setSoLuongMacDinh(int soLuongMacDinh) {
        this.soLuongMacDinh = soLuongMacDinh;
    }
}
