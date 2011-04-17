/*
 * Copyright (C) 2011 by Mark Doffman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE
 */
package com.doffman.dragarea;

import android.view.View;
import android.graphics.Point;
import android.graphics.Canvas;

/**
 * Interface for drawing the drag shadow during
 * a drag operation.
 */
public interface DragShadowBuilder
{
  /**
   * Returns the View object that started the drag operation.
   *
   * @return The View object associated with this ShadowBuilder.
   */
  public View getView();

  /**
   * Provides the size and touch point of the shadow image.
   *
   * The size and touch point of the image is where in the image should
   * be placed beneath the touch point in the drag operation.
   */
  public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint);

  /**
   * Called to draw the shadow image.
   *
   * @param canvas A Canvas object to draw the drag shadow to.
   */
  public void onDraw(Canvas c);
}
