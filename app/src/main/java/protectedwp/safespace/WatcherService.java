package protectedwp.safespace;

import android.app.admin.DeviceAdminService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class WatcherService extends DeviceAdminService {
    private boolean isBeeping = false;

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(() -> 
            Toast.makeText(getApplicationContext(), "WatcherService: " + text, Toast.LENGTH_SHORT).show());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showToast("onStartCommand (Sticky)");
        return START_STICKY; 
    }

    @Override
    public IBinder onBind(Intent intent) {
        showToast("onBind -> Запуск писка");
        startLifeMarker();
        return new Binder();
    }

    private void startLifeMarker() {
        if (isBeeping) return;
        isBeeping = true;
        
        new Thread(() -> {
            ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            while (isBeeping) {
                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 150);
                try {
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        isBeeping = false;
        showToast("Сервис УНИЧТОЖЕН");
        super.onDestroy();
    }
}
