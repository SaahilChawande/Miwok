package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class NumbersFragment extends Fragment {
    //Handles playback of all the sound files
    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()   {
        @Override
        public void onCompletion(MediaPlayer mp)    {
            releaseMediaPLayer();
        }
    };
    //Handles audio focus when playing a sound file
    //(i.e.) e gain or lose audio focus because of another app or device
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


    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);



        final ArrayList<Word> numbersArrayList = new ArrayList<Word>();
        numbersArrayList.add(new Word("One","Lutti",R.drawable.number_one, R.raw.number_one));
        numbersArrayList.add(new Word("Two","Otiiko",R.drawable.number_two, R.raw.number_two));
        numbersArrayList.add(new Word("Three","Tolookosu",R.drawable.number_three, R.raw.number_three));
        numbersArrayList.add(new Word("Four","Oyyisa",R.drawable.number_four, R.raw.number_four));
        numbersArrayList.add(new Word("Five","Massokka",R.drawable.number_five, R.raw.number_five));
        numbersArrayList.add(new Word("Six","Temmokka",R.drawable.number_six, R.raw.number_six));
        numbersArrayList.add(new Word("Seven","Kenekaku",R.drawable.number_seven, R.raw.number_seven));
        numbersArrayList.add(new Word("Eight","Kawinta",R.drawable.number_eight, R.raw.number_eight));
        numbersArrayList.add(new Word("Nine","Wo'e",R.drawable.number_nine, R.raw.number_nine));
        numbersArrayList.add(new Word("Ten","Na'aacha",R.drawable.number_ten, R.raw.number_ten));

        WordAdapter adapter = new WordAdapter(getActivity(), numbersArrayList, R.color.category_numbers);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = numbersArrayList.get(i);
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

    @Override
    public void onStop() {
        super.onStop();

        //When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPLayer();
    }

    //Clean up the media player by releasing its resources
    private void releaseMediaPLayer()  {
        // If the media player is not null, then it may be currently playing a sound
        if(mediaPlayer!=null)   {
            //Regardless of the current state of the media player, release its resources
            // because we no longer need it
            mediaPlayer.release();

            //Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            //Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
