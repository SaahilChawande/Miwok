package com.example.android.miwok;

import android.media.MediaPlayer;
import android.provider.MediaStore;

/**
 * Created by HP on 18-03-2017.
 */

public class Word {
    private String mDefaultTranslation;
    private String mMivokTranslation;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioResourceId;

    public Word(String mDefaultTranslation, String mMivokTranslation, int mAudioResourceId)  {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mMivokTranslation = mMivokTranslation;
        this.mAudioResourceId = mAudioResourceId;
    }
    public Word(String mDefaultTranslation, String mMivokTranslation, int mImageResourceId, int mAudioResourceId)  {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mMivokTranslation = mMivokTranslation;
        this.mImageResourceId = mImageResourceId;
        this.mAudioResourceId = mAudioResourceId;
    }

    public String getDefaultTranslation()  {
        return mDefaultTranslation;
    }

    public String getMivokTranslation() {
        return mMivokTranslation;
    }

    public int getImageResourceId()   { return mImageResourceId; }

    public int getAudioResourceId() {   return mAudioResourceId; }

    public boolean hasImage()   {
        if(mImageResourceId != NO_IMAGE_PROVIDED)
            return true;
        else
            return false;
    }
}
