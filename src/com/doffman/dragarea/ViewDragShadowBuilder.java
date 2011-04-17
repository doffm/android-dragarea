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

// FIXME May need to take padding in to account across the implementation
// of this whole class. Never mind padding, don't I have to take lots of
// other view altering properties in to account? Is there a simpler way?
public class ViewDragShadowBuilder implements DragShadowBuilder
{
  private View  mView;
  private Point mTouchPoint;

  public ViewDragShadowBuilder(View view)
  {
    mView = view;
    //By default center the view under the touch point.
    mTouchPoint = new Point(mView.getWidth()/2, mView.getHeight()/2);
  }

  public ViewDragShadowBuilder(View view, Point touchPoint)
  {
    mView = view;
    mTouchPoint = touchPoint;
  }

  @Override
  public View getView()
  {
    return mView;
  }

  @Override
  public void onProvideShadowMetrics(Point shadowSize, Point touchPoint)
  {
    shadowSize.x = mView.getWidth();
    shadowSize.y = mView.getHeight();

    touchPoint.x = mTouchPoint.x;
    touchPoint.y = mTouchPoint.y;
  }

  @Override
  public void onDraw(Canvas canvas)
  {
    mView.draw(canvas);
  }
}
