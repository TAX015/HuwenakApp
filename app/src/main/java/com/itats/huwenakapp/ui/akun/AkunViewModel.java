package com.itats.huwenakapp.ui.akun;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AkunViewModel extends ViewModel {
    MutableLiveData<String> mText;

    public AkunViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is akun fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
