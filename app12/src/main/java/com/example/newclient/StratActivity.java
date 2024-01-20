package com.example.newclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.world.IMyAidlInterface;
import com.example.world.Person;

public class StratActivity extends AppCompatActivity {
    private static final String TAG = "StratActivityclass";
    private static final String PACKAGE_NAME = "com.example.world";
    private static final String CLASS_NAME = "com.example.world.MyService";
    private IMyAidlInterface iMyAidlInterface;
    private Person person = new Person();
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "当前线程为" + Thread.currentThread() + "当前binder" + service);
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                iMyAidlInterface.addPerson(person);
                Log.d(TAG, "成功添加一个人" + person);
                iMyAidlInterface.registerCallback(myCallback);
                Log.d(TAG, "成功注册监听" + myCallback + "person" + person);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                iMyAidlInterface.unregisterCallback(myCallback);
                Log.d(TAG, "注销了" + myCallback);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    };
    Button strButton;
    Button stoButton;
    private Callback myCallback = new Callback.Stub() {
        @Override
        public void callbackInt(int data) throws RemoteException {
            Log.d(TAG, "传递的数据为" + data);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strat);
        strButton = findViewById(R.id.send_me);
        stoButton = findViewById(R.id.stop_me);
    }

    @Override
    protected void onResume() {
        super.onResume();
        strButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkservice();
            }
        });
        stoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
            }
        });
    }
    private void linkservice(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PACKAGE_NAME,CLASS_NAME));
        bindService(intent,conn,BIND_AUTO_CREATE);
    }
}