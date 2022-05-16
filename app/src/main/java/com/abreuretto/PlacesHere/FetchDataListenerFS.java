package com.abreuretto.PlacesHere;

import java.util.List;

public interface FetchDataListenerFS {
    void onFetchCompleteFS(List<ApplicationFS> data);
    void onFetchFailureFS(String msg);
}
