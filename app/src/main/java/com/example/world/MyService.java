package com.example.world;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.newclient.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyService extends Service {
    private int num;
    private Callback clientCallback;
    private List<Callback> callbackList = new ArrayList<>();
    private List<Person> list;
    private static final String TAG = "MyServiceclass";
    Binder binder = new IMyAidlInterface.Stub() {
        @Override
        public List<Person> addPerson(Person person) throws RemoteException {
            if (list != null) {
                list.add(person);
                Log.d(TAG, "添加的person" + person + "当前线程" + Thread.currentThread().getName());
            }
            return list;
        }

        @Override
        public void registerCallback(Callback callback) throws RemoteException {
            if (!callbackList.contains(callback)) {
                callbackList.add(callback);
//                io.reactivex.rxjava3.core.Observable
//                        .create(emitter -> {
//                            Log.d(TAG, "创建一个observal");
//                        })
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(o -> {
//                            num = (int) (Math.random()*100);
//                            Log.d(TAG, "num为" + num);
//                        });
                Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            num = (int)(Math.random()*100);
                            Log.d(TAG, "num为" + num);
                        });
                clientCallback = callback;
                clientCallback.callbackInt(num);
            } else
                Log.d(TAG, "registerCallback打印没有打印或已存在" + callback.hashCode());
        }

        @Override
        public void unregisterCallback(Callback callback) throws RemoteException {
            if (callbackList.contains(callback)) {
                callbackList.remove(callback);
            } else
                Log.d(TAG, "registerCallback未打印或移除失败" + callback);
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: " + intent);
        list = new ArrayList<>();
        return binder;
    }
}