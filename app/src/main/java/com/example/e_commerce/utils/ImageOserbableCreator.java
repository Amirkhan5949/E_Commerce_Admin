package com.example.e_commerce.utils;

import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class ImageOserbableCreator {
    public static Observable<Map<String,String>> get(final UploadRequest request) {
       return Observable.create(new ObservableOnSubscribe<Map<String, String>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Map<String, String>> emitter) {

                request.callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Map map = new HashMap();
                        map.put(FirebaseConstants.Product.img, resultData.get("secure_url"));
                        map.put(FirebaseConstants.Product.image_format, resultData.get("format"));
                        emitter.onNext(map);
                        emitter.onComplete();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        emitter.onError(new Throwable(error.getDescription()));
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();;
            }
        });
    }
}
