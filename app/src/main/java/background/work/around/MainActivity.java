package background.work.around;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        TextView tv = new TextView(this);
        tv.setTextSize(16);

        String lang = Locale.getDefault().getLanguage();
        if (lang.equals("ru")) {
            tv.setText("Привет, это приложение которое тестирует насколько долго сможет продержаться фоновый сервис не являющися foreground в системе. Индикатором этого будет звук. Приложение использует взаимную цепочку из bind для удержания фоновых сервисов вместо привычного запуска. Это даёт очень высокую живучесть. Вначале к первому из сервисов подключается ресивер, а потом засыпает. В это время первый сервис взаимно подключается ко второму сервису, а второй к первому образуя прочную связь, после чего ресивер уже перестает быть нужен. Также приложение имеет action на перезагрузку для ресивера, чтобы тестировать получится ли запустить звук после перезагрузки. Если у вас нет ограничений на ресиверы перезагрузки, как на некоторых телефонах Xiaomi, то шанс что получится довольно высокий. \n\n" +
                    "Если вы находитесь на обычном Android, то запустив сейчас это приложение вы разрешили ему запуск после переазгрузки. Имейте ввиду, что оно может стать источником звука неожиданно. Вы можете удалить его в любое время.\n\n" +
                    "Нажмите старт, чтобы запустить звук.");
        } else {
            tv.setText("Hi, this is an app that tests how long a non-foreground background service can last in the system. The indicator for this will be sound. The app uses a mutual bind chain to hold background services instead of a standard launch. This gives very high survival rate. First, a receiver connects to the first service and then falls asleep. During this time, the first service mutually connects to the second service, and the second to the first, forming a strong bond, after which the receiver is no longer needed. The app also has a reboot action for the receiver to test if it's possible to start the sound after a reboot. If you don't have restrictions on reboot receivers, like on some Xiaomi phones, the chance of success is quite high.\n\n" +
                    "If you are on a stock Android, by launching this app now you have allowed it to run after a reboot. Keep in mind that it may become a sound source unexpectedly. You can uninstall it at any time.\n\n" +
                    "Press start to trigger the sound.");
        }

        Button runBtn = new Button(this);
        runBtn.setText("START");

        runBtn.setOnClickListener(v -> {
            Intent intent = new Intent("background.work.around.START_NUCLEUS");
            intent.setPackage("background.work.around");            
            sendBroadcast(intent);
        });

        layout.addView(tv);
        layout.addView(runBtn);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        
        setContentView(scroll);
    }
}
