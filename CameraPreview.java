package com.revcord.global;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback, PictureCallback {
	private SurfaceHolder mHolder;
	private Camera mCamera;

	private static Activity mActivity;
	private static String TAG = " : CameraPreview : "; // Not added logs

private int ori=90;
	@SuppressWarnings("deprecation")
	public CameraPreview(Activity activity, Context context, Camera camera,int ori) {
		super(context);
		mActivity = activity;
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.ori=ori;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// create the surface and start camera preview
			if (mCamera == null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(VIEW_LOG_TAG, "Error setting camera preview: ");// +
																	// e.getMessage());
		}
	}

	public void refreshCamera(Camera camera) {
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		// set preview size and make any resize, rotate or
		// reformatting changes here
		// start preview with new settings
		setCamera(camera);
		try {
			mCamera.setPreviewDisplay(mHolder);
			// setCameraDisplayOrientation(mActivity, mCamera);
			mCamera.setDisplayOrientation(ori);
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(VIEW_LOG_TAG, "Error starting camera preview: ");// +
																	// e.getMessage());
		}
	}

	public static void setCameraDisplayOrientation(Activity activity,
			Camera camera) {

		Camera.CameraInfo info = new Camera.CameraInfo();

		// android.hardware.Camera.getCameraInfo(cameraId, info);

		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees =270;
			Log.d(VIEW_LOG_TAG, "Error starting camera preview:000 ");
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			Log.d(VIEW_LOG_TAG, "Error starting camera preview: 900");
			break;
		case Surface.ROTATION_180:
			degrees =90;
			Log.d(VIEW_LOG_TAG, "Error starting camera preview: 180");
			break;
		case Surface.ROTATION_270:
			degrees = 90;
			Log.d(VIEW_LOG_TAG, "Error starting camera preview: 270");
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		try {
			setCameraDisplayOrientation(0, mCamera);
			//setCameraDisplayOrientation(mActivity, mCamera);
			mCamera.stopPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
//            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	//	refreshCamera(mCamera);
	}

	public void setCamera(Camera camera) {
		// method to set a camera instance
		mCamera = camera;
	}


	public static void setCameraDisplayOrientation(
			int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info =
				new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = mActivity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// mCamera.release();

	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
	//	CapturePhotosActivity.mCameraData = data;
	}

}
