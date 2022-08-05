package com.itats.huwenakapp.ui.laporan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LaporanViewModel extends ViewModel {
    MutableLiveData<String> mText;

    public LaporanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is laporan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
