package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()   {
        @Override
        public void onCompletion(MediaPlayer mp)    {
            releaseMediaPLayer();
        }
    };
    private AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener()   {
                public void onAudioFocusChange(int focusChange) {
                    if(focusChange == AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)    {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }
                    else if(focusChange == AudioManager.AUDIOFOCUS_GAIN)    {
                        mediaPlayer.start();
                    }
                    else if(focusChange == AudioManager.AUDIOFOCUS_LOSS)    {
                        releaseMediaPLayer();
                    }
                }
            };

    public ColorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPLayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> colorsArrayList = new ArrayList<Word>();
        colorsArrayList.add(new Word("Red","Wetetti",R.drawable.color_red, R.raw.color_red));
        colorsArrayList.add(new Word("Green","Chokokki",R.drawable.color_green, R.raw.color_green));
        colorsArrayList.add(new Word("Brown","Takaakki",R.drawable.color_brown, R.raw.color_brown));
        colorsArrayList.add(new Word("Gray","Topoppi",R.drawable.color_gray, R.raw.color_gray));
        colorsArrayList.add(new Word("Black","Kululli",R.drawable.color_black, R.raw.color_black));
        colorsArrayList.add(new Word("White","Kelelli",R.drawable.color_white, R.raw.color_white));
        colorsArrayList.add(new Word("Dust Yellow","Topiise",R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorsArrayList.add(new Word("Mustard Yellow","Chiwiite",R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter adapter = new WordAdapter(getActivity(), colorsArrayList, R.color.category_colors);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = colorsArrayList.get(i);
                releaseMediaPLayer();
                mediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //             mAudioManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    private void releaseMediaPLayer()  {
        if(mediaPlayer!=null)   {
            mediaPlayer.release();
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
