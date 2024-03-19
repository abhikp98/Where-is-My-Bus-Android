package com.app.timewheels.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Everything is in the left Navigation Drawer. Chill :)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}