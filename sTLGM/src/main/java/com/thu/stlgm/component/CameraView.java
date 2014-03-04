package com.thu.stlgm.component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback,
        Camera.AutoFocusCallback, Camera.PictureCallback {

    private TakePictureListener mListener;

    /**
     * This is a holder that holding a display surface.
     */
    private SurfaceHolder mHolder = null;

    private int sdk = 3;

    /**
     * This is a camera object using for connect/disconnect with the camera
     * service,and so on.
     */
    private Camera mCamera;

    /**
     * Perform inflation from XML and apply a class-specific base style. This
     * constructor of View allows subclasses to use their own base style when
     * they are inflating.
     *
     * @param context  The Context the view is running in, through which it can
     *                 access the current theme, resources, etc.
     * @param attrs    The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view. If 0, no style will
     *                 be applied (beyond what is included in the theme). This may
     *                 either be an attribute resource, whose value will be retrieved
     *                 from the current theme, or an explicit style resource.
     */
    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public CameraView(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (mHolder == null) {
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            sdk = getSDKInt();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        try {

            // rotate camera 90 degree on portrait mode
            if (getContext().getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

                if (sdk <= 4) {

                    // 1.5 & 1.6
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.set("orientation", "portrait");
                    mCamera.setParameters(parameters);
                } else {
                    setDisplayOrientation(mCamera, 90);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        startPreview();
    }

    /**
     * rotate camera with any degree, only available for SDK 5 and later
     *
     * @param camera
     * @param angle
     */
    private void setDisplayOrientation(Camera camera, int angle) {
        Method downPolymorphic;

        if (sdk <= 4)
            return;

        try {
            if (sdk > 4 && sdk < 8) {

                // parameters for pictures created by a Camera service.
                Camera.Parameters parameters = mCamera.getParameters();

                // 2.0, 2.1
                downPolymorphic = parameters.getClass().getMethod(
                        "setRotation", new Class[]{int.class});
                if (downPolymorphic != null)
                    downPolymorphic.invoke(parameters, new Object[]{angle});

                // Sets the Parameters for pictures from this Camera
                // service.
                mCamera.setParameters(parameters);

            } else {

                downPolymorphic = camera.getClass().getMethod(
                        "setDisplayOrientation", new Class[]{int.class});
                if (downPolymorphic != null)
                    downPolymorphic.invoke(camera, new Object[]{angle});
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // get Camera object.
        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);

        } catch (RuntimeException e) {
            e.printStackTrace();
            releaseCamera();
        } catch (IOException e) {
            e.printStackTrace();
            releaseCamera();
        }
    }

    public void stopPreview() {
        releaseCamera();
    }

    public void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
            mCamera.autoFocus(this);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        mCamera = null;
        System.gc();
    }

    private int getSDKInt() {
        // this is safe so that we don't need to use SDKInt which is only
        // available after 1.6
        try {
            return Integer.parseInt(Build.VERSION.SDK);
        } catch (Exception e) {
            return 3; // default to target 1.5 cupcake
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success)
            mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        if (mListener != null) {
            try {
                Bitmap cameraBitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
                mListener.onTakePicture(cameraBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        startPreview();
    }

    public void setTakePictureListener(TakePictureListener listener) {
        mListener = listener;
    }

    public interface TakePictureListener {

        public void onTakePicture(Bitmap bitmap);
    }
}