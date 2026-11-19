
package com.example.btl_ltadr;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodieDB.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_KHACH_HANG = "KhachHang";
    private static final String TABLE_MON_AN = "MonAn";
    private static final String TABLE_NHOM_TUY_CHON = "NhomTuyChon";
    private static final String TABLE_TUY_CHON_MON = "TuyChonMon";
    private static final String TABLE_UU_DAI = "UuDai";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        taoBangKhachHang(db);
        taoBangMonAn(db);
        taoBangNhomTuyChon(db);
        taoBangTuyChonMon(db);
        taoBangUuDai(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            taoBangMonAn(db);
            taoBangNhomTuyChon(db);
            taoBangTuyChonMon(db);
        }

        if (oldVersion < 3) {
            taoBangUuDai(db);
        }
    }

    // ================= TAO BANG =================

    private void taoBangKhachHang(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS KhachHang (" +
                        "maKhachHang TEXT PRIMARY KEY, " +
                        "tenKhachHang TEXT, " +
                        "soDienThoai TEXT, " +
                        "diaChiGiaoHang TEXT)"
        );
    }

    private void taoBangMonAn(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS MonAn (" +
                        "maMonAn INTEGER PRIMARY KEY, " +
                        "tenMonAn TEXT NOT NULL, " +
                        "giaMonAn INTEGER NOT NULL, " +
                        "giaCu INTEGER, " +
                        "moTa TEXT, " +
                        "hinhAnh TEXT)"
        );
    }

    private void taoBangNhomTuyChon(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS NhomTuyChon (" +
                        "idNhom INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "maMonAn INTEGER NOT NULL, " +
                        "tenNhom TEXT NOT NULL, " +
                        "soLuongToiThieu INTEGER, " +
                        "soLuongToiDa INTEGER, " +
                        "FOREIGN KEY(maMonAn) REFERENCES MonAn(maMonAn))"
        );
    }

    private void taoBangTuyChonMon(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS TuyChonMon (" +
                        "idTuyChon INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "idNhom INTEGER NOT NULL, " +
                        "tenTuyChon TEXT NOT NULL, " +
                        "giaThem INTEGER, " +
                        "soLuongMacDinh INTEGER, " +
                        "FOREIGN KEY(idNhom) REFERENCES NhomTuyChon(idNhom))"
        );
    }

    // ================= UU DAI =================

    private void taoBangUuDai(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS UuDai (" +
                        "maUuDai TEXT PRIMARY KEY, " +
                        "tenUuDai TEXT NOT NULL, " +
                        "moTa TEXT, " +
                        "phanTramGiam INTEGER, " +
                        "hinhAnh INTEGER)"
        );
    }

    // ================= KHACH HANG =================

    public boolean kiemTraSoDienThoai(String soDienThoai) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_KHACH_HANG,
                new String[]{"maKhachHang"},
                "soDienThoai = ?",
                new String[]{soDienThoai},
                null, null, null
        );

        boolean tonTai = cursor.moveToFirst();
        cursor.close();

        return tonTai;
    }

    public boolean themKhachHang(KhachHang khachHang) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("maKhachHang", khachHang.getMaKhachHang());
        values.put("tenKhachHang", khachHang.getTenKhachHang());
        values.put("soDienThoai", khachHang.getSoDienThoai());
        values.put("diaChiGiaoHang", khachHang.getDiaChiGiaoHang());

        return db.insert(TABLE_KHACH_HANG, null, values) != -1;
    }

    public KhachHang layKhachHang(String maKhachHang) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_KHACH_HANG,
                null,
                "maKhachHang = ?",
                new String[]{maKhachHang},
                null, null, null
        );

        KhachHang khachHang = null;

        if (cursor.moveToFirst()) {
            khachHang = new KhachHang(
                    cursor.getString(cursor.getColumnIndexOrThrow("maKhachHang")),
                    cursor.getString(cursor.getColumnIndexOrThrow("tenKhachHang")),
                    cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")),
                    cursor.getString(cursor.getColumnIndexOrThrow("diaChiGiaoHang"))
            );
        }

        cursor.close();
        return khachHang;
    }

    public boolean capNhatKhachHang(KhachHang khachHang) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenKhachHang", khachHang.getTenKhachHang());
        values.put("soDienThoai", khachHang.getSoDienThoai());
        values.put("diaChiGiaoHang", khachHang.getDiaChiGiaoHang());

        int ketQua = db.update(
                TABLE_KHACH_HANG,
                values,
                "maKhachHang = ?",
                new String[]{khachHang.getMaKhachHang()}
        );

        return ketQua > 0;
    }

    public boolean xoaKhachHang(String maKhachHang) {
        SQLiteDatabase db = getWritableDatabase();

        int ketQua = db.delete(
                TABLE_KHACH_HANG,
                "maKhachHang = ?",
                new String[]{maKhachHang}
        );

        return ketQua > 0;
    }

    // ================= MON AN =================

    private boolean themMonAn(SQLiteDatabase db, MonAn monAn) {

        ContentValues values = new ContentValues();

        values.put("maMonAn", monAn.getMaMonAn());
        values.put("tenMonAn", monAn.getTenMonAn());
        values.put("giaMonAn", monAn.getGiaMonAn());
        values.put("giaCu", monAn.getGiaCu());
        values.put("moTa", monAn.getMoTa());

        String tenHinhAnh = "";

        if (monAn.getHinhAnh() != 0) {
            try {
                tenHinhAnh = context.getResources()
                        .getResourceEntryName(monAn.getHinhAnh());
            } catch (Resources.NotFoundException e) {
                tenHinhAnh = "";
            }
        }

        values.put("hinhAnh", tenHinhAnh);

        long maMoi = db.insert(TABLE_MON_AN, null, values);

        if (maMoi == -1) {
            return false;
        }

        List<NhomTuyChon> danhSachNhom =
                monAn.getDanhSachNhomTuyChon();

        if (danhSachNhom == null) {
            return true;
        }

        for (NhomTuyChon nhom : danhSachNhom) {

            ContentValues nhomValues = new ContentValues();
            nhomValues.put("maMonAn", monAn.getMaMonAn());
            nhomValues.put("tenNhom", nhom.getTenNhom());
            nhomValues.put("soLuongToiThieu", nhom.getSoLuongToiThieu());
            nhomValues.put("soLuongToiDa", nhom.getSoLuongToiDa());

            long idNhom = db.insert(
                    TABLE_NHOM_TUY_CHON,
                    null,
                    nhomValues
            );

            if (idNhom == -1) {
                return false;
            }

            List<TuyChonMon> danhSachTuyChon =
                    nhom.getDanhSachTuyChon();

            if (danhSachTuyChon == null) {
                continue;
            }

            for (TuyChonMon tuyChon : danhSachTuyChon) {

                ContentValues tuyChonValues = new ContentValues();

                tuyChonValues.put("idNhom", idNhom);
                tuyChonValues.put("tenTuyChon", tuyChon.getTenTuyChon());
                tuyChonValues.put("giaThem", tuyChon.getGiaThem());
                tuyChonValues.put(
                        "soLuongMacDinh",
                        tuyChon.getSoLuongMacDinh()
                );

                long ketQua = db.insert(
                        TABLE_TUY_CHON_MON,
                        null,
                        tuyChonValues
                );

                if (ketQua == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public void khoiTaoDuLieuMonAnMacDinh() {

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM MonAn",
                null
        );

        cursor.moveToFirst();
        int soMonAn = cursor.getInt(0);
        cursor.close();

        if (soMonAn > 0) {
            return;
        }

        ArrayList<MonAn> danhSach =
                DuLieuMonAn.taoDanhSachTatCaMonAn();

        db.beginTransaction();

        try {
            for (MonAn monAn : danhSach) {
                if (!themMonAn(db, monAn)) {
                    throw new Exception("Không thể thêm món ăn");
                }
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<MonAn> layDanhSachMonAn() {

        ArrayList<MonAn> danhSach = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MON_AN,
                null,
                null,
                null,
                null,
                null,
                "maMonAn ASC"
        );

        while (cursor.moveToNext()) {

            int maMonAn = cursor.getInt(
                    cursor.getColumnIndexOrThrow("maMonAn")
            );

            String tenMonAn = cursor.getString(
                    cursor.getColumnIndexOrThrow("tenMonAn")
            );

            int giaMonAn = cursor.getInt(
                    cursor.getColumnIndexOrThrow("giaMonAn")
            );

            int giaCu = cursor.getInt(
                    cursor.getColumnIndexOrThrow("giaCu")
            );

            String moTa = cursor.getString(
                    cursor.getColumnIndexOrThrow("moTa")
            );

            String tenHinhAnh = cursor.getString(
                    cursor.getColumnIndexOrThrow("hinhAnh")
            );

            int hinhAnh = context.getResources().getIdentifier(
                    tenHinhAnh,
                    "drawable",
                    context.getPackageName()
            );

            ArrayList<NhomTuyChon> danhSachNhom =
                    layNhomTuyChon(db, maMonAn);

            MonAn monAn = new MonAn(
                    maMonAn,
                    tenMonAn,
                    giaMonAn,
                    giaCu,
                    moTa,
                    hinhAnh,
                    danhSachNhom
            );

            danhSach.add(monAn);
        }

        cursor.close();

        return danhSach;
    }

    private ArrayList<NhomTuyChon> layNhomTuyChon(
            SQLiteDatabase db,
            int maMonAn
    ) {

        ArrayList<NhomTuyChon> danhSachNhom = new ArrayList<>();

        Cursor cursor = db.query(
                TABLE_NHOM_TUY_CHON,
                null,
                "maMonAn = ?",
                new String[]{String.valueOf(maMonAn)},
                null,
                null,
                "idNhom ASC"
        );

        while (cursor.moveToNext()) {

            int idNhom = cursor.getInt(
                    cursor.getColumnIndexOrThrow("idNhom")
            );

            String tenNhom = cursor.getString(
                    cursor.getColumnIndexOrThrow("tenNhom")
            );

            int soLuongToiThieu = cursor.getInt(
                    cursor.getColumnIndexOrThrow("soLuongToiThieu")
            );

            int soLuongToiDa = cursor.getInt(
                    cursor.getColumnIndexOrThrow("soLuongToiDa")
            );

            ArrayList<TuyChonMon> danhSachTuyChon =
                    layTuyChonMon(db, idNhom);

            danhSachNhom.add(
                    new NhomTuyChon(
                            tenNhom,
                            soLuongToiThieu,
                            soLuongToiDa,
                            danhSachTuyChon
                    )
            );
        }

        cursor.close();

        return danhSachNhom;
    }

    private ArrayList<TuyChonMon> layTuyChonMon(
            SQLiteDatabase db,
            int idNhom
    ) {

        ArrayList<TuyChonMon> danhSach = new ArrayList<>();

        Cursor cursor = db.query(
                TABLE_TUY_CHON_MON,
                null,
                "idNhom = ?",
                new String[]{String.valueOf(idNhom)},
                null,
                null,
                "idTuyChon ASC"
        );

        while (cursor.moveToNext()) {

            String ten = cursor.getString(
                    cursor.getColumnIndexOrThrow("tenTuyChon")
            );

            int giaThem = cursor.getInt(
                    cursor.getColumnIndexOrThrow("giaThem")
            );

            int soLuongMacDinh = cursor.getInt(
                    cursor.getColumnIndexOrThrow("soLuongMacDinh")
            );

            danhSach.add(
                    new TuyChonMon(ten, giaThem, soLuongMacDinh)
            );
        }

        cursor.close();

        return danhSach;
    }

    // ================= QUAN LY UU DAI =================

    // Thêm ưu đãi. Không thêm trùng mã ưu đãi.
    public boolean themUuDai(UuDai uuDai) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("maUuDai", uuDai.getMaUuDai());
        values.put("tenUuDai", uuDai.getTenUuDai());
        values.put("moTa", uuDai.getMoTa());
        values.put("phanTramGiam", uuDai.getPhanTramGiam());
        values.put("hinhAnh", uuDai.getHinhAnh());

        long ketQua = db.insertWithOnConflict(
                TABLE_UU_DAI,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );

        return ketQua != -1;
    }

    // Lấy toàn bộ danh sách ưu đãi.
    public ArrayList<UuDai> layDanhSachUuDai() {

        ArrayList<UuDai> danhSach = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_UU_DAI,
                null,
                null,
                null,
                null,
                null,
                "maUuDai ASC"
        );

        while (cursor.moveToNext()) {

            String maUuDai = cursor.getString(
                    cursor.getColumnIndexOrThrow("maUuDai")
            );

            String tenUuDai = cursor.getString(
                    cursor.getColumnIndexOrThrow("tenUuDai")
            );

            String moTa = cursor.getString(
                    cursor.getColumnIndexOrThrow("moTa")
            );

            int phanTramGiam = cursor.getInt(
                    cursor.getColumnIndexOrThrow("phanTramGiam")
            );

            int hinhAnh = cursor.getInt(
                    cursor.getColumnIndexOrThrow("hinhAnh")
            );

            UuDai uuDai = new UuDai(
                    maUuDai,
                    tenUuDai,
                    moTa,
                    phanTramGiam,
                    hinhAnh
            );

            danhSach.add(uuDai);
        }

        cursor.close();

        return danhSach;
    }

    // Cập nhật thông tin ưu đãi.
    public boolean capNhatUuDai(UuDai uuDai) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenUuDai", uuDai.getTenUuDai());
        values.put("moTa", uuDai.getMoTa());
        values.put("phanTramGiam", uuDai.getPhanTramGiam());
        values.put("hinhAnh", uuDai.getHinhAnh());

        int ketQua = db.update(
                TABLE_UU_DAI,
                values,
                "maUuDai = ?",
                new String[]{uuDai.getMaUuDai()}
        );

        return ketQua > 0;
    }

    // Xóa ưu đãi theo mã.
    public boolean xoaUuDai(String maUuDai) {

        SQLiteDatabase db = getWritableDatabase();

        int ketQua = db.delete(
                TABLE_UU_DAI,
                "maUuDai = ?",
                new String[]{maUuDai}
        );

        return ketQua > 0;
    }
}
