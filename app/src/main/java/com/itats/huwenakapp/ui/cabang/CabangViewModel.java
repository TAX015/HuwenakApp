package com.itats.huwenakapp.ui.cabang;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CabangViewModel extends ViewModel {
    MutableLiveData<String> mText;

    public CabangViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ganti cabang fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
