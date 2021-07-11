package com.haryanvifolks.bajando.MediaControls;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;
import com.haryanvifolks.bajando.UI_Controls.UIControls;

import java.net.URL;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import static com.haryanvifolks.bajando.MainActivity.mediaPlayer;
import static com.haryanvifolks.bajando.MainActivity.mediaPlayerController;


public class MediaPLayerService extends Service implements AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.haryanvifolks.bajando.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.haryanvifolks.bajando.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.haryanvifolks.bajando.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.haryanvifolks.bajando.ACTION_NEXT";
    public static final String ACTION_STOP = "com.haryanvifolks.bajando.ACTION_STOP";

    public static final String NOTIFICATION_CHANNEL = "com.haryanvifolks.bajando.NOTIFICATION_CHANNEL";

    public static NotificationCompat.Builder builder;

    private static MediaSessionManager mediaSessionManager;
    public static MediaSessionCompat mediaSession;
    public static MediaControllerCompat.TransportControls transportControls;

    private static final int NOTIFICATION_ID = 101;

    private boolean onGoingCalls = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;


    static int notificationAction = android.R.drawable.ic_media_pause;
    static PendingIntent play_pauseAcion = null;

    private final IBinder iBinder = new MediaPLayerService.LocalBinder();

    private AudioManager audioManager;

    public static void onPrepared() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    mediaPlayer.start();
                    UIControls.pause(false);
                    UIControls.setDuration(mp.getDuration());
                    MainActivity.mediaPlayerPreparedState = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ErrorFound", "onPrepared");
                }
                UIControls.stopProgressBar();
                if (!MainActivity.playActivityRunning){
                    MediaPlayerController.startPlayMenu();

                }
                buildNotification(PlaybackStatus.PLAYING);
            }
        });

    }

    public static void onBuffer(){
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        UIControls.startProgressBar();
                        Log.e("Buffer","start");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        UIControls.stopProgressBar();
                        Log.e("Buffer","stop");
                        break;
                }
                return false;
            }
        });
    }

    public static void onCompletion() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                transportControls.skipToNext();
            }
        });
    }

    public static void onError() {
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaPlayer.reset();
                MainActivity.mediaPlayerPreparedState = false;
                Log.e("ERROR_CAUGHT_HERE", what + " " + extra);
                Toast.makeText(MainActivity.getMainContext(), "Connection Error: Try offline mode",
                        Toast.LENGTH_SHORT).show();
                UIControls.stopProgressBar();
                mediaPlayerController.play();
                return true;
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callStateListener();
        registerBecomingNoisyReceiver();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mediaPlayer == null)
                    mediaPlayerController.play();
                else if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if (mediaPlayer != null)
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayerController.stop();
                    }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.setVolume(0.5f, 0.5f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;
            default:
                break;

        }

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!requestAudioFocus()) {
            stopSelf();
        }
        if (mediaSessionManager == null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    initMediaSession();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackStatus.PLAYING);
        }

        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayerController.stop();
        removeAudioFocus();
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        removeNotification();
        unregisterReceiver(becomingNoisyReceiver);
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }


    public class LocalBinder extends Binder {
        public MediaPLayerService getService() {
            return MediaPLayerService.this;
        }
    }

    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mediaPlayerController.pause();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private void registerBecomingNoisyReceiver() {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }

    private void callStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayerController.pause();
                                onGoingCalls = true;
                            }
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (mediaPlayer != null) {
                            if (onGoingCalls) {
                                onGoingCalls = false;
                                mediaPlayerController.pause();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return;

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        updateMetaData();

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                mediaPlayerController.pause();
                       buildNotification(PlaybackStatus.PLAYING);
                       builder.setOngoing(true);
            }

            @Override
            public void onPause() {
                super.onPause();
                mediaPlayerController.pause();
                buildNotification(PlaybackStatus.PAUSED);
                builder.setOngoing(false);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                mediaPlayerController.next();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                mediaPlayerController.previous();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                stopSelf();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
            }
        });
    }

    public static void playSong(){
        mediaPlayerController.play();
    }

    public static void updateMetaData() {
        Song song = CurrentPlaylist.currentPlayList.get(CurrentPlaylist.index);
        Bitmap albumArt;
        try {
            URL url = new URL(song.getSongPicURL());
            albumArt = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            albumArt = BitmapFactory.decodeResource(MainActivity.getMainContext().getResources()
                    , R.drawable.ic_musical_note);
        }

        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getSongName())
                .build());
    }

    public static void buildNotification(PlaybackStatus playbackStatus) {

        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            play_pauseAcion = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            play_pauseAcion = playbackAction(0);
        }

        new getImage().execute();

    }

    public static void removeNotification() {
        NotificationManager notificationManager =
                (NotificationManager) MainActivity.getMainContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private static PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(MainActivity.getMainContext(),
                MediaPLayerService.class);
        switch (actionNumber) {
            case 0:
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(MainActivity.getMainContext(),
                        actionNumber, playbackAction, 0);
            case 1:
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(MainActivity.getMainContext(),
                        actionNumber, playbackAction, 0);
            case 2:
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(MainActivity.getMainContext(),
                        actionNumber, playbackAction, 0);
            case 3:
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(MainActivity.getMainContext(),
                        actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    public void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    public enum PlaybackStatus {
        PLAYING,
        PAUSED
    }

    private static class getImage extends AsyncTask<Void, Void, Void> {
        Bitmap albumArt;
        Song song = CurrentPlaylist.currentPlayList
                .get(CurrentPlaylist.index);


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(song.getSongPicURL());
                albumArt = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("IMGERROR", e.toString());
                albumArt = BitmapFactory.decodeResource(MainActivity.getMainContext().
                        getResources(), R.drawable.ic_musical_note);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotification();
                 builder = new NotificationCompat.Builder(MainActivity.getMainContext(),NOTIFICATION_CHANNEL)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                                setMediaSession(mediaSession.getSessionToken())
                                .setShowActionsInCompactView(0,1,2))
                        .setColor(ContextCompat.getColor(MainActivity.getMainContext(),R.color.colorPrimaryDark))
                        .setLargeIcon(albumArt)
                        .setSmallIcon(android.R.drawable.stat_sys_headset)
                        .setContentText(CurrentPlaylist.currentPlayList.get(
                                CurrentPlaylist.index).getArtistName())
                        .setContentTitle(CurrentPlaylist.currentPlayList.get(
                                CurrentPlaylist.index).getSongName())
                        .addAction(R.drawable.ic_previous, "previous", playbackAction(3))
                        .addAction(notificationAction, "pause", play_pauseAcion)
                        .addAction(R.drawable.ic_next, "next", playbackAction(2));

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.getMainContext());
                notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
            } else {

                 builder = new NotificationCompat.Builder(
                        MainActivity.getMainContext()).setShowWhen(false)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setMediaSession(mediaSession.getSessionToken())
                                .setShowActionsInCompactView(0, 1, 2))
                        .setColor(MainActivity.getMainContext().getResources().getColor(R.color.colorPrimaryDark))
                        .setLargeIcon(albumArt)
                        .setSmallIcon(android.R.drawable.stat_sys_headset)
                        .setContentText(CurrentPlaylist.currentPlayList.get(
                                CurrentPlaylist.index).getArtistName())
                        .setContentTitle(CurrentPlaylist.currentPlayList.get(
                                CurrentPlaylist.index).getSongName())
                        .addAction(R.drawable.ic_previous, "previous", playbackAction(3))
                        .addAction(notificationAction, "pause", play_pauseAcion)
                        .addAction(R.drawable.ic_next, "next", playbackAction(2));

                ((NotificationManager) MainActivity.getMainContext().getSystemService(Context.NOTIFICATION_SERVICE))
                        .notify(NOTIFICATION_ID, builder.build());
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void createNotification(){
            CharSequence name = "Bajan De";
            String description = "Audio Player";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL,name,importance);
            notificationChannel.setShowBadge(false);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setSound(null,null);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) MainActivity.getMainContext()
                            .getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
