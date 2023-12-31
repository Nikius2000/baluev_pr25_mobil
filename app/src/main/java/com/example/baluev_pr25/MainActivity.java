package com.example.baluev_pr25;
import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private int mCatSound, mChickenSound, mCowSound, mDogSound, mDuckSound, mSheepSound;
    private int mStreamID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cowImageButton = findViewById(R.id.btn_korova);
        cowImageButton.setOnClickListener(onClickListener);

        Button chickenImageButton = findViewById(R.id.btn_kyrica);
        chickenImageButton.setOnClickListener(onClickListener);

        Button catImageButton = findViewById(R.id.btn_koshka);
        catImageButton.setOnClickListener(onClickListener);

        Button duckImageButton = findViewById(R.id.btn_ytka);
        duckImageButton.setOnClickListener(onClickListener);

        Button sheepImageButton = findViewById(R.id.btn_ovca);
        sheepImageButton.setOnClickListener(onClickListener);

        Button dogImageButton = findViewById(R.id.btn_sobaka);
        dogImageButton.setOnClickListener(onClickListener);


        cowImageButton.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                if (eventAction == MotionEvent.ACTION_UP) {
                    // Отпускаем палец
                    if (mStreamID > 0)
                        mSoundPool.stop(mStreamID);
                }
                if (eventAction == MotionEvent.ACTION_DOWN) {
                    // Нажимаем на кнопку
                    mStreamID = playSound(mCowSound);
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mSoundPool.stop(mStreamID);
                }
                return true;
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_korova){
                playSound(mCowSound);
            } else if(v.getId() == R.id.btn_kyrica){
                playSound(mChickenSound);
            }else if(v.getId() == R.id.btn_koshka){
                playSound(mCatSound);
            }else if(v.getId() == R.id.btn_ytka){
                playSound(mDuckSound);
            }else if(v.getId() == R.id.btn_ovca){
                playSound(mSheepSound);
            }else if(v.getId() == R.id.btn_sobaka){
                playSound(mDogSound);
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundPool() {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    }

    private int playSound(int sound) {
        if (sound > 0) {
            mStreamID = mSoundPool.play(sound, 1, 1, 1, 0, 1);
        }
        return mStreamID;
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Не могу загрузить файл " + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Для устройств до Android 5
            createOldSoundPool();
        } else {
            // Для новых устройств
            createNewSoundPool();
        }

        mAssetManager = getAssets();

        // получим идентификаторы
        mCowSound = loadSound("1.ogg");
        mChickenSound = loadSound("2.ogg");
        mCatSound = loadSound("3.ogg");
        mDuckSound = loadSound("4.ogg");
        mSheepSound = loadSound("5.ogg");
        mDogSound = loadSound("6.ogg");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPool.release();
        mSoundPool = null;
    }
}