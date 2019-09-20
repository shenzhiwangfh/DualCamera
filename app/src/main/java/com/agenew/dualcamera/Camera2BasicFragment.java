/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agenew.dualcamera;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agenew.dualcamera.camera.AgenewCamera;
import com.agenew.dualcamera.camera.AutoFitTextureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Camera2BasicFragment extends Fragment implements View.OnClickListener {

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "Camera2BasicFragment";

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private AutoFitTextureView mTextureView0;
    private AutoFitTextureView mTextureView1;
    private AutoFitTextureView mTextureView2;
    private AgenewCamera mAgenewCamera0;
    private AgenewCamera mAgenewCamera1;
    private AgenewCamera mAgenewCamera2;

    public static Camera2BasicFragment newInstance()     {
        return new Camera2BasicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.picture0).setOnClickListener(this);
        view.findViewById(R.id.picture1).setOnClickListener(this);
        view.findViewById(R.id.picture2).setOnClickListener(this);
        mTextureView0 = (AutoFitTextureView) view.findViewById(R.id.texture0);
        mTextureView1 = (AutoFitTextureView) view.findViewById(R.id.texture1);
        mTextureView2 = (AutoFitTextureView) view.findViewById(R.id.texture2);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mAgenewCamera0 = new AgenewCamera(getActivity());
        mAgenewCamera1 = new AgenewCamera(getActivity());
        mAgenewCamera2 = new AgenewCamera(getActivity());
    }

    @Override
    public void onPause() {
        if(mAgenewCamera0.isStart()) mAgenewCamera0.stop();
        if(mAgenewCamera1.isStart()) mAgenewCamera1.stop();
        if(mAgenewCamera2.isStart()) mAgenewCamera2.stop();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.picture0:
                if(mAgenewCamera0.isStart()) {
                    mAgenewCamera0.stop();
                } else {
                    mAgenewCamera0.start(mTextureView0, "0");
                }
                break;
            case R.id.picture1:
                if(mAgenewCamera1.isStart()) {
                    mAgenewCamera1.stop();
                } else {
                    mAgenewCamera1.start(mTextureView1, "1");
                }
                break;
            case R.id.picture2:
                if(mAgenewCamera2.isStart()) {
                    mAgenewCamera2.stop();
                } else {
                    mAgenewCamera2.start(mTextureView2, "2");
                }
                break;
        }
    }
}
