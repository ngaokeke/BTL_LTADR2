
package com.example.btl_ltadr;

public class UuDai {

    private String maUuDai;
    private String tenUuDai;
    private String moTa;
    private int phanTramGiam;
    private int hinhAnh;

    public UuDai(String maUuDai, String tenUuDai,
                 String moTa, int phanTramGiam, int hinhAnh) {

        this.maUuDai = maUuDai;
        this.tenUuDai = tenUuDai;
        this.moTa = moTa;
        this.phanTramGiam = phanTramGiam;
        this.hinhAnh = hinhAnh;
    }

    public String getMaUuDai() {
        return maUuDai;
    }

    public String getTenUuDai() {
        return tenUuDai;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getPhanTramGiam() {
        return phanTramGiam;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public void setMaUuDai(String maUuDai) {
        this.maUuDai = maUuDai;
    }

    public void setTenUuDai(String tenUuDai) {
        this.tenUuDai = tenUuDai;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setPhanTramGiam(int phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public void setHinhAnh(int hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
