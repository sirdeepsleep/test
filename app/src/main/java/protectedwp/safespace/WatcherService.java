package protectedwp.safespace;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class WatcherService extends Service {
    private boolean isBeeping = false;
    private boolean isSelfBound = false; // Предохранитель от рекурсии

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
        showToast("onBind -> Само-бинд + Звук");
        
        // Запускаем механизм удержания самого себя
        triggerSelfBind();
        
        startLifeMarker();
        return new Binder();
    }

    private void triggerSelfBind() {
        if (isSelfBound) return; // Если уже привязаны, выходим
        
        try {
            Intent selfIntent = new Intent(this, WatcherService.class);
            // Используем ApplicationContext, чтобы привязка жила дольше процесса
            getApplicationContext().bindService(selfIntent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    isSelfBound = true;
                    showToast("УЗЕЛ ЗАВЯЗАН (Self-bound)");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isSelfBound = false;
                    showToast("УЗЕЛ РАЗОРВАН");
                }
            }, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT | Context.BIND_ABOVE_CLIENT);
        } catch (Exception e) {
            showToast("Ошибка само-бинда: " + e.getMessage());
        }
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
        showToast("СЕРВИС УНИЧТОЖЕН");
        super.onDestroy();
    }
}
