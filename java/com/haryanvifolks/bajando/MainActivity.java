package com.haryanvifolks.bajando;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akaita.android.circularseekbar.CircularSeekBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.haryanvifolks.bajando.Adapters.ArtistAdapter;
import com.haryanvifolks.bajando.Adapters.CategoryAdapter;
import com.haryanvifolks.bajando.Adapters.CategoryAdapterRecycler;
import com.haryanvifolks.bajando.Adapters.GenreAdapter;
import com.haryanvifolks.bajando.Adapters.SongAdapter;
import com.haryanvifolks.bajando.Amination.FloatingButtonAnim;
import com.haryanvifolks.bajando.DatabaseClasses.Artist;
import com.haryanvifolks.bajando.DatabaseClasses.PlayListDatabase;
import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.DatabaseClasses.SongDatabase;
import com.haryanvifolks.bajando.DatabaseClasses.SongViewModel;
import com.haryanvifolks.bajando.MainFragments.currentPlaylist;
import com.haryanvifolks.bajando.MainFragments.home;
import com.haryanvifolks.bajando.MainFragments.library;
import com.haryanvifolks.bajando.MainFragments.profile;
import com.haryanvifolks.bajando.MainFragments.search;
import com.haryanvifolks.bajando.MediaControls.CurrentPlaylist;
import com.haryanvifolks.bajando.MediaControls.MediaPLayerService;
import com.haryanvifolks.bajando.MediaControls.MediaPlayerController;
import com.haryanvifolks.bajando.MessageBox.PresetSelect;
import com.haryanvifolks.bajando.UI_Controls.UIControls;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean playActivityRunning = false;
    public static boolean serviceBound = false;
    public static MediaPLayerService pLayerService;
    public static boolean mediaPlayerPreparedState  = false;
    public static boolean repeatState = false;
    public static boolean shuffleState = false;
    private static Context context;
    private DrawerLayout drawer;

    //Firebase variables
    public static FirebaseAuth mAuth;


    public static DatabaseReference artistDatabaseReference;
    public static DatabaseReference songDatabaseReference;
    public static DatabaseReference genreDatabaseReference;

    public static float x, y;
    public static List<String> musicStyles;

    public static Equalizer equalizer;

    private Toolbar mToolbar;

    public static boolean togglePlayButton = false;

    public static SongViewModel songViewModel;
    public static SongAdapter personalTopAdapter;

    public static SongAdapter allSongsAdapter;
    public static SongAdapter trendingAdapter;
    public static CategoryAdapter categoryAdapter;
    public static ArtistAdapter allArtists;
    public static GenreAdapter popularArtist;

    public static int addToPlaylist;

    public static CurrentPlaylist current_Playlist;

    public static final String PLAYLIST_ID = "com.haryavifolks.bajande.PLAYLIST_ID";
    public static List<Song> songsForArtist;


    public static List<PlayListDatabase> playLists;
    public static List<Artist> artists;
    public static Artist artistGenre;

    public Button Cast_buttton;

    public static FloatingActionButton play_button;
    public static FloatingActionButton next_button;
    public static FloatingActionButton previous_button;
    public static FloatingActionButton play_menu_button;
    public static FloatingActionButton shuffle_button;
    public static FloatingActionButton repeat_button;


    public static CoordinatorLayout rootView;
    public static RelativeLayout slideView;
    public static Button slideButton;

    public static Button closeslide;


    public static CategoryAdapterRecycler categoryAdapterRecycler;

    public static ProgressBar mainProgressbar;

    public static BottomNavigationView bottomNavigationView;

    public static MediaPlayer mediaPlayer;
    public static MediaPlayerController mediaPlayerController;
    public static UIControls uiControls;

    public static String[] categories = {"This Month's Record Breaking Songs",
            "Browse Artists", "Trending Songs", "Top Artists", "All Songs", "Top Genres"};

    public static FloatingButtonAnim floatingButtonAnim;
    public static CircularSeekBar circularSeekBar;


    public static ServiceConnection serviceConnection;

    @Override
    protected void onDestroy() {

        if (serviceBound) {
            unbindService(serviceConnection);
            pLayerService.stopSelf();
        }
        try {
            MediaPLayerService.removeNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @SuppressLint({"ClickableViewAccessibility", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);



        slideButton = findViewById(R.id.play_small_button);
        rootView = findViewById(R.id.coordinatorLayout);
        slideView = findViewById(R.id.slideView);
        closeslide = findViewById(R.id.closeslide);


        final SlideUp slideUp = new SlideUpBuilder(slideView)
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView)
                .build();

        slideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.show();
            }
        });

        closeslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.hide();
            }
        });

        artistDatabaseReference = FirebaseDatabase.getInstance().getReference("Artist");
        songDatabaseReference = FirebaseDatabase.getInstance().getReference("Songs");
        genreDatabaseReference = FirebaseDatabase.getInstance().getReference("Genres");


        songDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot song:dataSnapshot.getChildren()){
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if(Integer.parseInt(Objects.requireNonNull(song.getKey()))>songViewModel.maxID())
                                SongDatabase.insertSong(song.getValue(Song.class));
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        artistDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot artist:dataSnapshot.getChildren()){
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (Integer.parseInt(artist.getKey())>songViewModel.maxIDArtist())
                                SongDatabase.insertArtist(artist.getValue(Artist.class));
                        }
                    };
                    Thread t1 = new Thread(runnable);
                    t1.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        mToolbar = findViewById(R.id.toolbar);

//        setSupportActionBar(mToolbar);


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MediaPLayerService.LocalBinder binder = (MediaPLayerService.LocalBinder) service;
                pLayerService = binder.getService();
                serviceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceBound = false;
            }
        };

        songViewModel = ViewModelProviders.of(this).get(SongViewModel.class);

        artists = new ArrayList<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                artists = songViewModel.getArtists();
            }
        };
        Thread t = new Thread(runnable);
        t.start();

        personalTopAdapter = new SongAdapter();
        allSongsAdapter = new SongAdapter();
        trendingAdapter = new SongAdapter();
        allArtists = new ArtistAdapter();
        popularArtist = new GenreAdapter(artists);
        categoryAdapter = new CategoryAdapter(this, categories);

        categoryAdapterRecycler = new CategoryAdapterRecycler();

        context = MainActivity.this;

        Runnable rSongs = new Runnable() {
            @Override
            public void run() {
                songsForArtist = songViewModel.getSongs();
                CurrentPlaylist.currentPlayList = songsForArtist;
            }
        };
        Thread getSongs = new Thread(rSongs);
        getSongs.start();


        /////////////Bottom Navigantion////////////////////////////
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListerner);
        bottomNavigationView.setSelectedItemId(R.id.play_frag);


        /////////////Drawer Menu//////////////////////////////////
/*        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Cast_buttton = findViewById(R.id.cast_button);
        Cast_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName cn = new ComponentName("com.android.settings",
                            "com.android.settings.bluetooth.BluetoothSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        ////////////////Floating Button Actions//////////////////////
        play_button = findViewById(R.id.play_button);
        previous_button = findViewById(R.id.previous_button);
        next_button = findViewById(R.id.next_button);
        shuffle_button = findViewById(R.id.shuffle_button);
        repeat_button = findViewById(R.id.repeat_button);
        play_menu_button = findViewById(R.id.play_activity_button);
        mainProgressbar = findViewById(R.id.main_progress);

        floatingButtonAnim = new FloatingButtonAnim();

 /*       play_button.setOnClickListener(onClickListener);
        previous_button.setOnClickListener(onClickListener);
        next_button.setOnClickListener(onClickListener);
        shuffle_button.setOnClickListener(onClickListener);
        repeat_button.setOnClickListener(onClickListener);
        play_menu_button.setOnClickListener(onClickListener);

        play_button.setOnLongClickListener(onLongClickListener);

        play_button.setOnTouchListener(onTouchListener);

        /////////////seekbar/////////////////////////

        circularSeekBar = findViewById(R.id.circle_seek_bar);

*/
        ///////////DATABASE/////////////////////////////////


        songViewModel.getTopsongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                personalTopAdapter.setSongs(songs);
            }
        });
        songViewModel.getAllPlaylist().observe(this, new Observer<List<PlayListDatabase>>()
        {
            @Override
            public void onChanged(List<PlayListDatabase> playListDatabases) {
                playLists = playListDatabases;
            }
        });
        songViewModel.getAllsongs().observe(this, new Observer<List<Song>>() {

            @Override
            public void onChanged(List<Song> songs) {
                allSongsAdapter.setSongs(songs);
                if (current_Playlist == null) {
                    current_Playlist = new CurrentPlaylist(songs, 0);
                }
                //to be removed from main thread

                List<Song> trending = new ArrayList<>();
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).getAlbumName().contains("Trending Musics")) {
                        trending.add(songs.get(i));
                    }
                }
                trendingAdapter.setSongs(trending);
            }
        });
        songViewModel.getAllArtist().observe(this, new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artists) {
                allArtists.setArtists(artists);
            }
        });

        songViewModel.getTopArtist().observe(this, new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artists) {
                popularArtist.setArtists(artists);
            }
        });

        ////////////////////////////////AdView///////////////////////////////////////////
/*
        MobileAds.initialize(this, getString(R.string.admob_id));
        AdView adview = findViewById(R.id.admob_banner);
//        adview.setAdSize(AdSize.BANNER);
        //      adview.setAdUnitId(getString(R.string.admob_baner));
        //964EC61074DE5C6607F636EC2B4D4E5D

        AdRequest adRequest = new AdRequest.Builder().build();

        adview.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        adview.loadAd(adRequest);
*/
        ////////////////////////MediaPlayer//////////////////////////////////////////////
        mediaPlayer = new MediaPlayer();
        mediaPlayerController = new MediaPlayerController();


        equalizer = new Equalizer(0,mediaPlayer.getAudioSessionId());
        equalizer.setEnabled(true);
        short m = equalizer.getNumberOfBands();
        musicStyles = new ArrayList<>();

        for (short i =0 ; i<m;i++){
            musicStyles.add(equalizer.getPresetName(i));
        }


        uiControls = new UIControls();




    }


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() - x < -100) {
                            MediaPlayerController.startPlayMenu();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;

            }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (next_button.getVisibility() == View.VISIBLE)
            floatingButtonAnim.closeMenu();
        else
            moveTaskToBack(true);
    }



    private FloatingActionButton.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play_button:

                    mediaPlayerController.pause();
                    if (togglePlayButton)
                        floatingButtonAnim.closeMenu();
                    break;
                case R.id.next_button:
                    mediaPlayerController.next();
                    floatingButtonAnim.closeMenu();
                    break;
                case R.id.previous_button:
                    mediaPlayerController.previous();
                    floatingButtonAnim.closeMenu();
                    break;
                case R.id.shuffle_button:
                    mediaPlayerController.shuffle();
                    floatingButtonAnim.closeMenu();
                    break;
                case R.id.repeat_button:
                    mediaPlayerController.repeat();
                    floatingButtonAnim.closeMenu();
                    break;
                case R.id.play_activity_button:
                    MediaPlayerController.startPlayMenu();
                    floatingButtonAnim.closeMenu();
                    break;
                default:
                    break;
            }
        }
    };

    private FloatingActionButton.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            floatingButtonAnim.togglePlayButton();
            return true;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navListerner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.currenList:
                            selectedFragment = new currentPlaylist();
                            break;
                        case R.id.search_frag:
                            selectedFragment = new search();
                            break;
                        case R.id.play_frag:
                            selectedFragment = new home();
                            break;
                        case R.id.library_frag:
                            selectedFragment = new library();
                            break;
                        case R.id.profile_frag:
                            selectedFragment = new profile();
                            break;
                    }

                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_downloads:
                bottomNavigationView.setSelectedItemId(R.id.library_frag);
                break;
            case R.id.nav_equaliser:
                PresetSelect presetSelect = new PresetSelect(this);
                presetSelect.show();
                break;
            case R.id.nav_subscriptions:
                bottomNavigationView.setSelectedItemId(R.id.profile_frag);
                break;


            case R.id.nav_share:Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "haryanvifolks.com");
                startActivity(Intent.createChooser(intent, "Share"));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Context getMainContext() {
        return context;
    }

    public static void setArtistGenre(Artist artistGenre) {
        MainActivity.artistGenre = artistGenre;
    }

    public static Artist getArtistGenre() {
        return artistGenre;
    }

}
