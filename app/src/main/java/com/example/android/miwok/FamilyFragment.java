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
public class FamilyFragment extends Fragment {

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

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> familyArrayList = new ArrayList<Word>();
        familyArrayList.add(new Word("Father","Epe",R.drawable.family_father, R.raw.family_father));
        familyArrayList.add(new Word("Mother","Eta",R.drawable.family_mother, R.raw.family_mother));
        familyArrayList.add(new Word("Son","Angsi",R.drawable.family_son, R.raw.family_son));
        familyArrayList.add(new Word("Daughter","Tune",R.drawable.family_daughter, R.raw.family_daughter));
        familyArrayList.add(new Word("Older Brother","Taachi",R.drawable.family_older_brother, R.raw.family_older_brother));
        familyArrayList.add(new Word("Younger Brother","Chalitti",R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyArrayList.add(new Word("Older Sister","Tete",R.drawable.family_older_sister, R.raw.family_older_sister));
        familyArrayList.add(new Word("Younger Sister","Kolliti",R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyArrayList.add(new Word("Grandmother","Ama",R.drawable.family_grandmother, R.raw.family_grandmother));
        familyArrayList.add(new Word("Grandfather","Paapa",R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter adapter = new WordAdapter(getActivity(), familyArrayList, R.color.category_family);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = familyArrayList.get(i);
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

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPLayer();
    }
}
