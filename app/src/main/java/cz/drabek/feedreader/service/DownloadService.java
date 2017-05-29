package cz.drabek.feedreader.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.data.source.ArticlesDataSource;
import cz.drabek.feedreader.data.source.ArticlesRepository;
import cz.drabek.feedreader.util.ClientToServiceBinder;
import cz.drabek.feedreader.util.Injection;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    /** Command to the service to display a message */
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SET_VALUE = 3;
    public static final int MSG_START_SERVICE = 4;

    List<Messenger> mClients = new ArrayList<Messenger>();
    /** Holds last value set by a client. */
    int mValue = 0;

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_START_SERVICE:
                    Log.d("HONZA", "handleMessage: starting service");
                    startDownload();
                    break;
                case MSG_SET_VALUE:
                    mValue = msg.arg1;
                    Log.d("HONZA", "handleMessage: Service received from client: " + mValue++);
                    sendMsg(Message.obtain(null, MSG_SET_VALUE, mValue, 0));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    }

    private void sendMsg(Message msg) {
        for (int i=mClients.size()-1; i>=0; i--) {
            try {
                mClients.get(i).send(msg);
            } catch (RemoteException e) {
                // The client is dead.  Remove it from the list;
                // we are going through the list from back to front
                // so this is safe to do inside the loop.
                mClients.remove(i);
            } catch (IllegalStateException e) {
                Log.e(TAG, "sendMsg: This message is already in use.", e);
            }
        }
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        startDownload();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startDownload();
        return START_STICKY;
    }

    private void startDownload() {
        Runnable r = new DownloadingRunnable();
        Thread thread = new Thread(r);
        thread.start();
    }


    class DownloadingRunnable implements Runnable, ArticlesDataSource.LoadArticlesCallback {
        @Override
        public void run() {
            sendMsg(Message.obtain(null, ClientToServiceBinder.MSG_LOAD_STARTED, 1, 0));

            ArticlesRepository repository = Injection.provideTasksRepository(getApplicationContext());
            repository.getArticles(this);

            sendMsg(Message.obtain(null, ClientToServiceBinder.MSG_LOAD_FINISHED, 1, 0));
        }

        // implemented in ArticlesRepository - getArticles()
        @Override
        public void onArticlesLoaded(List<Article> articles) { }
        @Override
        public void onDataNotAvailable() { }
    }

    private class DownloadArticlesTask extends AsyncTask<Void, Boolean, Void>
            implements ArticlesDataSource.LoadArticlesCallback {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sendMsg(Message.obtain(null, ClientToServiceBinder.MSG_LOAD_STARTED, 1, 0));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArticlesRepository repository = Injection.provideTasksRepository(getApplicationContext());
            repository.getArticles(this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sendMsg(Message.obtain(null, ClientToServiceBinder.MSG_LOAD_FINISHED, 1, 0));
        }

        // implemented in ArticlesRepository - getArticles()
        @Override
        public void onArticlesLoaded(List<Article> articles) { }
        @Override
        public void onDataNotAvailable() { }
    }
}
