package com.abreuretto.PlacesHere;

import java.util.List;

public interface FetchDataListener {
    void onFetchComplete(List<Application> data);
    void onFetchFailure(String msg);
}

