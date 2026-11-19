
package com.example.btl_ltadr;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONObject;

public class TheoDoiDonHangWidget extends AppWidgetProvider {

    private static final String TEN_PREF = "FOODIE_DON_HANG";
    private static final String KEY_DON_HANG = "DANH_SACH";

    private static final String PREF_SESSION = "FOODIE_SESSION";
    private static final String KEY_DANG_NHAP = "DANG_DANG_NHAP";
    private static final String KEY_SO_DIEN_THOAI = "SO_DIEN_THOAI";

    @Override
    public void onUpdate(
            Context context,
            AppWidgetManager appWidgetManager,
            int[] appWidgetIds
    ) {
        for (int appWidgetId : appWidgetIds) {
            capNhatWidget(context, appWidgetManager, appWidgetId);
        }
    }

    // CẬP NHẬT TẤT CẢ WIDGET FOODIE
    public static void capNhatTatCaWidgets(Context context) {

        AppWidgetManager manager =
                AppWidgetManager.getInstance(context);

        ComponentName componentName =
                new ComponentName(
                        context,
                        TheoDoiDonHangWidget.class
                );

        int[] widgetIds =
                manager.getAppWidgetIds(componentName);

        for (int widgetId : widgetIds) {
            capNhatWidget(context, manager, widgetId);
        }
    }
    public static void capNhatWidget(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        ComponentName componentName =
                new ComponentName(context, TheoDoiDonHangWidget.class);

        int[] widgetIds = manager.getAppWidgetIds(componentName);

        for (int widgetId : widgetIds) {
            capNhatWidget(context, manager, widgetId);
        }
    }

    // CẬP NHẬT NỘI DUNG WIDGET
    public  static void capNhatWidget(
            Context context,
            AppWidgetManager manager,
            int widgetId
    ) {
        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_theo_doi_don_hang
        );

        String maDon = "";
        String trangThai = "";
        int tienTrinh = 0;
        String moTa = "";

        // KIỂM TRA TÀI KHOẢN
        SharedPreferences session =
                context.getSharedPreferences(
                        PREF_SESSION,
                        Context.MODE_PRIVATE
                );

        boolean daDangNhap =
                session.getBoolean(KEY_DANG_NHAP, false);

        String soDienThoai =
                session.getString(KEY_SO_DIEN_THOAI, "");

        // LẤY ĐƠN HÀNG MỚI NHẤT CỦA TÀI KHOẢN
        if (daDangNhap && soDienThoai != null) {

            SharedPreferences preferences =
                    context.getSharedPreferences(
                            TEN_PREF,
                            Context.MODE_PRIVATE
                    );

            try {
                JSONArray danhSach = new JSONArray(
                        preferences.getString(
                                KEY_DON_HANG,
                                "[]"
                        )
                );

                for (int i = danhSach.length() - 1; i >= 0; i--) {

                    JSONObject don =
                            danhSach.optJSONObject(i);

                    if (don == null) {
                        continue;
                    }

                    String chuDon = don.optString(
                            "soDienThoaiChuDon",
                            ""
                    );

                    if (soDienThoai.trim().equals(chuDon.trim())) {

                        maDon = don.optString(
                                "maDon",
                                ""
                        );

                        trangThai = don.optString(
                                "trangThai",
                                ""
                        );

                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // XÁC ĐỊNH TIẾN TRÌNH MÔ PHỎNG
        if (trangThai.equals("Đặt hàng thành công")) {

            tienTrinh = 25;
            moTa = "Nhà hàng đang chuẩn bị món ăn";

        } else if (trangThai.equals("Đang giao hàng")) {

            tienTrinh = 70;
            moTa = "Shipper đang trên đường giao món";

        } else if (trangThai.equals("Hoàn tất đơn hàng")) {

            tienTrinh = 100;
            moTa = "Đơn hàng đã được giao thành công";

        } else if (trangThai.equals("Đơn hàng đã hủy")) {

            tienTrinh = 0;
            moTa = "Đơn hàng đã bị hủy";

        } else {

            trangThai = "Chưa có đơn hàng";
            moTa = daDangNhap
                    ? "Hãy đặt món để theo dõi đơn hàng"
                    : "Đăng nhập để theo dõi đơn hàng";
        }

        // HIỂN THỊ THÔNG TIN
        views.setTextViewText(
                R.id.txtWidgetMaDon,
                maDon.isEmpty()
                        ? "Chưa có mã đơn"
                        : "Mã đơn: " + maDon
        );

        views.setTextViewText(
                R.id.txtWidgetTrangThai,
                trangThai
        );

        views.setTextViewText(
                R.id.txtWidgetMoTa,
                moTa
        );

        views.setTextViewText(
                R.id.txtWidgetPhanTram,
                tienTrinh + "%"
        );

        views.setProgressBar(
                R.id.progressWidget,
                100,
                tienTrinh,
                false
        );

        // NHẤN WIDGET ĐỂ MỞ TRANG ĐƠN HÀNG
        Intent intent = new Intent(
                context,
                DonHangActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        widgetId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        views.setOnClickPendingIntent(
                R.id.widgetRoot,
                pendingIntent
        );

        manager.updateAppWidget(widgetId, views);
    }
}
