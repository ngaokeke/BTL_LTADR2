package com.example.btl_ltadr;

import java.util.ArrayList;

public class DuLieuMonAn {

    // DANH SACH TAT CA MON AN
    public static ArrayList<MonAn> taoDanhSachTatCaMonAn() {

        ArrayList<MonAn> danhSach = new ArrayList<>();

        // 1. YEU THUONG 1
        danhSach.add(new MonAn(
                1,
                "Yêu thương 1",
                145000,
                180000,
                "02 Gà Rán, 02 Mỳ Ý (M), 01 Khoai Tây Chiên (S), " +
                        "02 Pepsi (S), Đã bao gồm: 4 Tương Ớt Ngọt, 1 Tương Cà.",
                R.drawable.yeu_thuong1,
                taoNhomTuyChon(2, 2, "Pepsi (S)")
        ));

        // 2. YEU THUONG 2
        danhSach.add(new MonAn(
                2,
                "Yêu thương 2",
                145000,
                177000,
                "02 Gà Rán Sốt Phô Mai, 01 Mỳ Ý (M), " +
                        "01 Khoai Tây Lắc, 02 Pepsi (S), " +
                        "Đã bao gồm: 3 Tương Ớt Ngọt, 1 Tương Cà.",
                R.drawable.yeu_thuong2,
                taoNhomTuyChon(2, 2, "Pepsi (S)")
        ));

        // 3. YEU THUONG 3
        danhSach.add(new MonAn(
                3,
                "Yêu thương 3",
                145000,
                180000,
                "02 Gà Rán, 01 Burger Bulgogi, 01 Mỳ Ý (M), " +
                        "02 Pepsi (S), Đã bao gồm: 4 Tương Ớt Ngọt.",
                R.drawable.yeu_thuong3,
                taoNhomTuyChon(2, 2, "Pepsi (S)")
        ));

        // 4. HANH PHUC 1
        danhSach.add(new MonAn(
                4,
                "Hạnh phúc 1",
                185000,
                251000,
                "03 Gà Rán, 02 Mỳ Ý (M), 01 Khoai Tây Chiên (M), " +
                        "03 Pepsi (M), Đã bao gồm: 5 Tương Ớt Ngọt, 1 Tương Cà.",
                R.drawable.hanh_phuc1,
                taoNhomTuyChon(3, 3, "Pepsi (M)")
        ));

        // 5. HANH PHUC 2
        danhSach.add(new MonAn(
                5,
                "Hạnh phúc 2",
                280000,
                338000,
                "04 Gà Rán, 01 Burger Bulgogi, 01 Burger Tôm, " +
                        "01 Khoai Tây Lắc, 04 Pepsi (M), " +
                        "Đã bao gồm: 6 Tương Ớt Ngọt, 1 Tương Cà.",
                R.drawable.hanh_phuc2,
                taoNhomTuyChon(4, 4, "Pepsi (M)")
        ));

        // 6. TIEN LOI 1
        danhSach.add(new MonAn(
                6,
                "Tiện lợi 1",
                55000,
                67000,
                "01 Gà Rán, 01 Khoai Tây Chiên (S), " +
                        "01 Pepsi (S), Đã bao gồm: 1 Tương Ớt Ngọt, 1 Tương Cà.",
                R.drawable.tien_loi1,
                taoNhomTuyChon(1, 1, "Pepsi (S)")
        ));

        // 7. TIEN LOI 2
        danhSach.add(new MonAn(
                7,
                "Tiện lợi 2",
                59000,
                73000,
                "01 Mỳ Ý (M), 01 Khoai Tây Chiên (S), " +
                        "01 Pepsi (S), Đã bao gồm: 1 Tương Ớt Ngọt, 1 Tương Cà.",
                R.drawable.tien_loi2,
                taoNhomTuyChon(0, 1, "Pepsi (S)")
        ));

        // THEM CAC MON AN MOI VAO DAY

        return danhSach;
    }

    // TAO CAC NHOM TUY CHON CHO COMBO
    private static ArrayList<NhomTuyChon> taoNhomTuyChon(
            int soLuongGa,
            int soLuongNuoc,
            String tenNuoc
    ) {

        ArrayList<NhomTuyChon> danhSachNhom = new ArrayList<>();

        // NHOM CHON GA
        if (soLuongGa > 0) {

            ArrayList<TuyChonMon> danhSachGa = new ArrayList<>();

            danhSachGa.add(new TuyChonMon(
                    "Chọn Gà không cay",
                    0,
                    soLuongGa
            ));

            danhSachGa.add(new TuyChonMon(
                    "Chọn Gà cay",
                    0,
                    0
            ));

            danhSachNhom.add(new NhomTuyChon(
                    "Chọn gà",
                    soLuongGa,
                    soLuongGa,
                    danhSachGa
            ));
        }

        // NHOM CHON NUOC
        ArrayList<TuyChonMon> danhSachNuoc = new ArrayList<>();

        danhSachNuoc.add(new TuyChonMon(
                tenNuoc,
                0,
                soLuongNuoc
        ));

        danhSachNhom.add(new NhomTuyChon(
                "Chọn nước",
                soLuongNuoc,
                soLuongNuoc,
                danhSachNuoc
        ));

        // NHOM THEM SOT GA
        ArrayList<TuyChonMon> danhSachSot = new ArrayList<>();

        danhSachSot.add(new TuyChonMon(
                "Sốt HS",
                5000,
                0
        ));

        danhSachSot.add(new TuyChonMon(
                "Sốt phô mai",
                5000,
                0
        ));

        danhSachSot.add(new TuyChonMon(
                "Sốt đậu",
                5000,
                0
        ));

        danhSachNhom.add(new NhomTuyChon(
                "Thêm sốt gà",
                0,
                99,
                danhSachSot
        ));

        // NHOM MON AN KEM
        ArrayList<TuyChonMon> danhSachAnKem = new ArrayList<>();

        danhSachAnKem.add(new TuyChonMon(
                "Phô mai 1 miếng",
                7000,
                0
        ));

        danhSachAnKem.add(new TuyChonMon(
                "Trứng",
                7000,
                0
        ));

        danhSachNhom.add(new NhomTuyChon(
                "Ngon hơn khi ăn kèm",
                0,
                99,
                danhSachAnKem
        ));

        return danhSachNhom;
    }

    // DANH SACH MON BAN CHAY
    public static ArrayList<MonAn> taoDanhSachBanChay() {

        ArrayList<MonAn> tatCaMon =
                taoDanhSachTatCaMonAn();

        ArrayList<MonAn> banChay = new ArrayList<>();

        for (MonAn monAn : tatCaMon) {

            if (monAn.getMaMonAn() >= 1 &&
                    monAn.getMaMonAn() <= 7) {

                banChay.add(monAn);
            }
        }

        return banChay;
    }
}
