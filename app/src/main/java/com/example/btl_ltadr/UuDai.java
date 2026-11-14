package com.example.btl_ltadr;

public class UuDai {

    private int maUuDai;
    private String tenUuDai;
    private String moTa;
    private int hinhAnh;

    public UuDai(int maUuDai, String tenUuDai,
                 String moTa, int hinhAnh) {

        this.maUuDai = maUuDai;
        this.tenUuDai = tenUuDai;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    public int getMaUuDai() {
        return maUuDai;
    }

    public String getTenUuDai() {
        return tenUuDai;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }
}