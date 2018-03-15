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
public class PhrasesFragment extends Fragment {

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

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> phrasesArrayList = new ArrayList<Word>();
        phrasesArrayList.add(new Word("Where are you going?","Minto wukusus", R.raw.phrase_where_are_you_going));
        phrasesArrayList.add(new Word("What is your name?","Tinne Oyaasene", R.raw.phrase_what_is_your_name));
        phrasesArrayList.add(new Word("My name is...","Oyaaset...", R.raw.phrase_my_name_is));
        phrasesArrayList.add(new Word("How are you feeling?","Michekses?", R.raw.phrase_how_are_you_feeling));
        phrasesArrayList.add(new Word("I'm feeling good","Kuchi achit", R.raw.phrase_im_feeling_good));
        phrasesArrayList.add(new Word("Are you coming?","Eenesaa?", R.raw.phrase_are_you_coming));
        phrasesArrayList.add(new Word("Yes, I'm coming","Heeeenem", R.raw.phrase_im_coming));
        phrasesArrayList.add(new Word("Let's go","Yoowutis", R.raw.phrase_lets_go));
        phrasesArrayList.add(new Word("Come here","Enninem", R.raw.phrase_come_here));

        WordAdapter adapter = new WordAdapter(getActivity(), phrasesArrayList, R.color.category_phrases);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = phrasesArrayList.get(i);
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
        releaseMediaPLayer();
    }

    private void releaseMediaPLayer()  {
        if(mediaPlayer!=null)   {
            mediaPlayer.release();
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
