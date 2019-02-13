/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

  private static final long ANIMATION_DELAY = 20L;
  private static final int CURRENT_POINT_OPACITY = 0xA0;
  private static final int POINT_SIZE = 6;

  private CameraManager cameraManager;
  private final Paint paint;
  private Bitmap resultBitmap;
  private final int maskColor;
  private final int resultColor;
  public int laserLinePosition=0;
  public LinearGradient linearGradient;
  public float[]position=new float[]{0f,0.5f,1f};
  //public int[]colors=new int[]{0x0047ac31,0xffff0fff,0x00ffff0f};
  public int[]colors=new int[]{0xFF7FFF00,0xFF00FF00,0xFF93DB70};
  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Resources resources = getResources();
    maskColor = resources.getColor(R.color.viewfinder_mask);
    resultColor = resources.getColor(R.color.result_view);
  }

  public void setCameraManager(CameraManager cameraManager) {
    this.cameraManager = cameraManager;
  }

  @SuppressLint("DrawAllocation")
  @Override
  public void onDraw(Canvas canvas) {
    if (cameraManager == null) {
      return; // not ready yet, early draw before done configuring
    }
    Rect frame = cameraManager.getFramingRect();
    Rect previewFrame = cameraManager.getFramingRectInPreview();    
    if (frame == null || previewFrame == null) {
      return;
    }
    int width = canvas.getWidth();
    int height = canvas.getHeight();
      //在扫描框上画四个角
      paint.setColor(0xFF47ac31);
      canvas.drawRect(frame.left, frame.top, frame.left+70, frame.top+10,paint);
      canvas.drawRect(frame.left, frame.top, frame.left+10, frame.top+70,paint);
      canvas.drawRect(frame.right-70, frame.top, frame.right, frame.top+10,paint);
      canvas.drawRect(frame.right-10, frame.top, frame.right, frame.top+70,paint);
      canvas.drawRect(frame.left, frame.bottom-10, frame.left+70, frame.bottom,paint);
      canvas.drawRect(frame.left, frame.bottom-70, frame.left+10, frame.bottom,paint);
      canvas.drawRect(frame.right-70, frame.bottom-10, frame.right, frame.bottom,paint);
      canvas.drawRect(frame.right-10, frame.bottom-70, frame.right, frame.bottom,paint);


    // Draw the exterior (i.e. outside the framing rect) darkened
    paint.setColor(resultBitmap != null ? resultColor : maskColor);
    canvas.drawRect(0, 0, width, frame.top, paint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
    canvas.drawRect(0, frame.bottom + 1, width, height, paint);
    if (resultBitmap != null) {
      // Draw the opaque result bitmap over the scanning rectangle
      paint.setAlpha(CURRENT_POINT_OPACITY);
      canvas.drawBitmap(resultBitmap, null, frame, paint);
    } else {

      // Draw a red "laser scanner" line through the middle to show decoding is active
      int middle = frame.height() / 2 + frame.top;
      laserLinePosition=laserLinePosition+5;
      if(laserLinePosition> frame.height()) {

        laserLinePosition=0;

      }
      linearGradient=new LinearGradient(frame.left+1, frame.top+laserLinePosition, frame.right-1,
              frame.top+10+laserLinePosition,colors,position, Shader.TileMode.CLAMP);
      paint.setShader(linearGradient);

      canvas.drawRect(frame.left+1, frame.top+laserLinePosition, frame.right-1, frame.top+10+laserLinePosition,paint);
      paint.setShader(null);

      float scaleX = frame.width() / (float) previewFrame.width();
      float scaleY = frame.height() / (float) previewFrame.height();

      // Request another update at the animation interval, but only repaint the laser line,
      // not the entire viewfinder mask.
      postInvalidateDelayed(ANIMATION_DELAY,
                            frame.left - POINT_SIZE,
                            frame.top - POINT_SIZE,
                            frame.right + POINT_SIZE,
                            frame.bottom + POINT_SIZE);
    }
  }

  public void drawViewfinder() {
    Bitmap resultBitmap = this.resultBitmap;
    this.resultBitmap = null;
    if (resultBitmap != null) {
      resultBitmap.recycle();
    }
    invalidate();
  }
}
