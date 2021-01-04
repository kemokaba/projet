package ziz.org.ecommerce;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class EService extends Service {
    public EService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"veillez cliquer le map pour avoir la localistaion du magasin",
                Toast.LENGTH_LONG).show();

        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}