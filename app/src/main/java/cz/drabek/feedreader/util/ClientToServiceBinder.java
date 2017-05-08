package cz.drabek.feedreader.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import cz.drabek.feedreader.articles.ArticlesPresenter;
import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.service.DownloadService;

/**
 * Example of binding and unbinding to the remote service.
 * This demonstrates the implementation of a service which the client will
 * bind to, interacting with it through an aidl interface.</p>
 *
 * <p>Note that this is implemented as an inner class only keep the sample
 * all together; typically this code would appear in some separate class.
 */
public class ClientToServiceBinder {

    private static final String TAG = "HONZA";
    private static ClientToServiceBinder INSTANCE = null;
    public static final int MSG_LOAD_FINISHED = 1;

    private ArticlesPresenter mPresenter;
    private Messenger mService = null;
    private boolean mIsBound = false;

    private ClientToServiceBinder() { }

    public static ClientToServiceBinder getInstance(ArticlesPresenter presenter) {
        if (INSTANCE == null)
            INSTANCE = new ClientToServiceBinder();

        INSTANCE.setPresenter(presenter);
        return INSTANCE;
    }

    private void setPresenter (ArticlesPresenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadService.MSG_SET_VALUE:
                    Log.d(TAG, "handleMessage: Client received from service: " + msg.arg1);
                    break;
                case MSG_LOAD_FINISHED:
                    Log.d(TAG, "handleMessage: Client received from service: MSG_LOAD_FINISHED=" + msg.arg1);
                    mPresenter.onServiceActive(false);
                    break;
                case DownloadService.MSG_IS_WORKING:
                    boolean isWorking = (msg.arg1 == 1);
                    mPresenter.onServiceActive(isWorking);
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);

            try {
                Message msg = Message.obtain(null,
                        DownloadService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }

            // At this point is remote service connected
        }

        // This is called when the connection with the service has been
        // unexpectedly disconnected -- that is, its process crashed.
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mIsBound = false;
        }
    };

    public void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because there is no reason to be able to let other
        // applications replace our component.
        mPresenter.getContext().bindService(
                new Intent(mPresenter.getContext(), DownloadService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    public void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            DownloadService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }

            // Detach our existing connection.
            mPresenter.getContext().unbindService(mConnection);
            mIsBound = false;
        }
    }

    public boolean isBound() {
        return mIsBound;
    }

    public void sendMessage(Message msg) {
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches articles to DB
     * @return  success
     */
    public boolean startService() {
        if (!isBound())
            return false;

        mPresenter.onServiceActive(true);
        Message msg = Message.obtain(null, DownloadService.MSG_START_SERVICE, 0, 0);
        sendMessage(msg);
        return true;
    }

    public void getServiceStatus() {
        Message msg = Message.obtain(null, DownloadService.MSG_IS_WORKING, 0, 0);
        sendMessage(msg);
    }

}
