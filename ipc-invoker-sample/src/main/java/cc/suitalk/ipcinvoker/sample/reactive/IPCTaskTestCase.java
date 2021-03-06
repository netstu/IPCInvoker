/*
 *  Copyright (C) 2017-present Albie Liang. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package cc.suitalk.ipcinvoker.sample.reactive;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import cc.suitalk.ipcinvoker.IPCAsyncInvokeTask;
import cc.suitalk.ipcinvoker.IPCInvokeCallback;
import cc.suitalk.ipcinvoker.IPCSyncInvokeTask;
import cc.suitalk.ipcinvoker.IPCTask;
import cc.suitalk.ipcinvoker.exception.OnExceptionObserver;
import cc.suitalk.ipcinvoker.tools.Log;
import cc.suitalk.ipcinvoker.type.IPCString;

/**
 * Created by albieliang on 2018/4/2.
 */

public class IPCTaskTestCase {

    private static final String TAG = "IPCInvokerSample.IPCTaskTestCase";

    public static void invokeAsync() {
        IPCTask.create("cc.suitalk.ipcinvoker.sample:push")
                .timeout(10)
                .async(AsyncInvokeTask.class)
                .data(new IPCString("test invokeAsync"))
                .defaultResult(false)
                .callback(new IPCInvokeCallback<Boolean>() {
                    @Override
                    public void onCallback(Boolean data) {
                        Log.i(TAG, "invokeAsync result : %s", data);
                    }
                }).invoke();
    }

    public static void invokeSync() {
        Boolean result = IPCTask.create("cc.suitalk.ipcinvoker.sample:push")
                .timeout(20)
                .onExceptionObserver(new OnExceptionObserver() {
                    @Override
                    public void onExceptionOccur(Exception e) {
                        Log.e(TAG, "onExceptionOccur : %s", android.util.Log.getStackTraceString(e));
                    }
                })
                .serviceConnection(new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Log.i(TAG, "onServiceConnected(%s)", name);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Log.i(TAG, "onServiceDisconnected(%s)", name);
                    }
                })
                .sync(SyncInvokeTask.class)
                .data("test invokeSync")
                .defaultResult(false)
                .invoke();
        Log.i(TAG, "invokeSync result : %s", result);
    }

    private static class SyncInvokeTask implements IPCSyncInvokeTask<String, Boolean> {

        @Override
        public Boolean invoke(String data) {
            return true;
        }
    }

    private static class AsyncInvokeTask implements IPCAsyncInvokeTask<IPCString, Boolean> {

        @Override
        public void invoke(IPCString data, IPCInvokeCallback<Boolean> callback) {
            callback.onCallback(true);
        }
    }
}
