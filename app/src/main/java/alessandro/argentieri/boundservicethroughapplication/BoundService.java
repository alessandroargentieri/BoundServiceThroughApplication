package alessandro.argentieri.boundservicethroughapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BoundService extends Service {

    private static String LOG_TAG = "BoundService";
    private IBinder mBinder = new MyBinder();
    public static int count = 0;
    private static CallerInterface callerActivity;
    public boolean activated = false; //NOT THE SERVICE BUT THE THREAD WITHIN THE SERVICE
    MyThread t;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        /*if(mainActivity!=null){
            mainActivity.setServiceText(" Bound");
        }else{
            Toast.makeText(this, "mainActivity is still null!", Toast.LENGTH_LONG).show();
        }*/
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        activated = false; //blocks also the thread
        //let's kill the thread if it's alive
        killerThread();

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //let's kill the thread if it's alive
        killerThread();

        Log.v(LOG_TAG, "in onDestroy");
    }




    //when called from an Activity gives the Service the context to whom communicate the results
    //ARTIFICIO USATO DALL'ACTIVITY CHIAMANTE IL SERVICE PER FAR SAPERE A QUEST'ULTIMO A CHI INVIARE I RISULTATI TRAMITE I METODI DELLA CLASSE ACTIVITY CHIAMANTE IL SERVICE
    public static void setActivity(CallerInterface mainActivity){
        callerActivity = mainActivity;
    }

    public static void azzeraCount(){
        count = 0;
    }

    public void startTask(){
        if(activated==false){ //se il thread non è già partito
            activated = true;
            Handler handler = new MyHandler();
            t = new MyThread(handler);
            t.start();
        }else{
            Toast.makeText(this, "Thread already in action!",Toast.LENGTH_LONG).show();
        }
    }


    /////////////////NESTED CLASSES///////////////////////////////////////
    public class MyBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    //RECEPISCE DAL THREAD INTERNO AL SERVICE TUTTI I MESSAGGI CON CHIAVE "count" INVIATI AL SERVICE STESSO E LI GESTISCE CHIAMANDO L'ACTIVITY PER NOTIFICARE I RISULTATI DEL THREAD
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if(bundle.containsKey("count")) {
                int value = bundle.getInt("count");
                if(callerActivity!=null)
                    try {
                        callerActivity.setCountText(value);
                    }catch(Exception e){
                        Log.e(LOG_TAG, e.toString());
                        Toast.makeText(BoundService.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                else
                    Toast.makeText(BoundService.this, "callerActivity is null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class MyThread extends Thread{
        private Handler handler;

        //COSTRUTTORE DEL THREAD: LEGA L'HANDLER DEL SERVICE AL THREAD STESSO PER FARLI COMUNICARE
        public MyThread(Handler handler){
            this.handler = handler;
        }

        @Override
        public void run(){
            long instant = System.currentTimeMillis();
            while(activated){
                if(System.currentTimeMillis() - instant > 1000){
                    count++;
                    instant = System.currentTimeMillis();
                    //RICHIAMO LA VOID DEL THREAD STESSO CHE NOTIFICA ALL'HANDLER DEL SERVICE I RISULTATI
                    notifyMessage(count);
                }
                if(count == 1000){
                    count = 0;
                }
            }
        }
        //VOID INTERNA AL THREAD PER NOTIFICARE ALL'HANDLER DEL SERVICE COLLEGATO A QUESTO THREAD I RISULTATI
        private void notifyMessage(int index) {
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("count", index);
            msg.setData(b);
            handler.sendMessage(msg);
        }
    } //end of myThread





    ////THREAD KILLER////////////////////////////////////////
    public void killerThread(){
        try {
            if (t.isAlive()) {
                t.interrupt();
                t = null;
            }
        }catch(Exception e){
            Log.e(LOG_TAG,"ThreadKiller error: " + e.toString());
        }
    }








}
