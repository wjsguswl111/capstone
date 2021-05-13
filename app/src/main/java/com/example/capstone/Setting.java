package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity implements View.OnClickListener{
        private final static String TAG = "Setting";
        private final static int REQUESTCODE_RINGTONE_PICKER = 1000;

        private RingtoneManager mRtm;
        private Ringtone mRtCurrent;

        SeekBar seekBar;
        AudioManager maudioManager = null;

        private TextView m_tvRingtoneUri;
        private String m_strRingToneUri;
        private TextView m_tvRingtoneTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mRtm = new RingtoneManager(this);

        this.findViewById(R.id.button).setOnClickListener(this);

        seekBar = findViewById(R.id.seekBar);

        maudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVol = maudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        seekBar.setMax(maxVol);
        int nCurrentVoumn = maudioManager.getStreamVolume(AudioManager.STREAM_RING);
        seekBar.setProgress(nCurrentVoumn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                maudioManager.setStreamVolume(AudioManager.STREAM_RING, i, AudioManager.FLAG_SHOW_UI);
                mRtCurrent.play();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void startRingtone(Uri uriRingtone){
        this.releaseRingtone();

        try{
            mRtCurrent = mRtm.getRingtone(this, uriRingtone);
            if(mRtCurrent == null){
                throw new Exception("Can't play player");
            }

            mRtCurrent.play();
            mRtCurrent.stop();
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        mRtCurrent.stop();
    }

    private void releaseRingtone(){
        if(mRtCurrent != null){
            if(mRtCurrent.isPlaying()){
                mRtCurrent.stop();
                mRtCurrent = null;
            }
        }
    }

    private void showRingtoneChooser(){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Choose Ringtone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);

        if(m_strRingToneUri != null && m_strRingToneUri.isEmpty()){
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(m_strRingToneUri));
        }

        this.startActivityForResult(intent, REQUESTCODE_RINGTONE_PICKER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUESTCODE_RINGTONE_PICKER){
            if(resultCode == RESULT_OK){
                Uri ring = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                if(ring != null){
                    m_strRingToneUri = ring.toString();
                    //m_tvRingtoneUri.setText(ring.toString());
                    this.startRingtone(ring);
                } else{
                    m_strRingToneUri = null;
                    m_tvRingtoneUri.setText("Choose ringtone");
                    m_tvRingtoneTitle.setText("");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                showRingtoneChooser();
                //releaseRingtone();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}