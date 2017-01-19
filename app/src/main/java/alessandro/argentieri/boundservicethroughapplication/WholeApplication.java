package alessandro.argentieri.boundservicethroughapplication;


import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class WholeApplication extends Application {

    static CallerInterface caller;
    BoundService mBoundService;
    boolean isServiceBound = false;

    @Override
    public void onCreate(){
        super.onCreate();
        //create and start the service
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);

        //the application itself bind to it!
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    //in realtà verrà passata una vera activity
    public static void setCaller(CallerInterface mCaller){
        caller = mCaller;
        BoundService.setActivity(caller);
    }

    public static synchronized void azzeraCount(){
        BoundService.azzeraCount();
    }




    //non è la dichiarazione di una classe, ma la creazione di un'istanza che estendo ServiceConnection.
    //come se fosse dichiarata prima di tutti i metodi sotto la definizione della classe
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.MyBinder myBinder = (BoundService.MyBinder) service;
            mBoundService = myBinder.getService();
            isServiceBound = true;
            Toast.makeText(WholeApplication.this, "bound Service in app", Toast.LENGTH_LONG).show();
            //let the thread starts
            mBoundService.startTask();
        }
    };
    ///////////////////////////////////////////////////////////////////////////////////////////////////







}
