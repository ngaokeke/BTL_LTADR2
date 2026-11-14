
package com.example.btl_ltadr;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class ThongBaoDonHang {

    private static final String CHANNEL_ID = "thong_bao_don_hang";

    public static void taoKenhThongBao(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Thông báo đơn hàng",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Thông báo trạng thái đơn hàng");

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void guiThongBao(
            Context context,
            String tieuDe,
            String noiDung,
            Intent intentMoActivity,
            int notificationId
    ) {

        taoKenhThongBao(context);

        // Kiểm tra quyền thông báo Android 13 trở lên
        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intentMoActivity,
                PendingIntent.FLAG_UPDATE_CURRENT |
                        PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(tieuDe)
                        .setContentText(noiDung)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(noiDung)
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context)
                .notify(notificationId, builder.build());
    }
}
