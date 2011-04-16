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
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.graphics.drawable.Drawable;
import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.Rect;

import android.content.Context;
import android.util.AttributeSet;

import java.util.HashMap;

public class DragArea extends FrameLayout
{
  private HashMap<OnDragListener, Droppable> mDroppables;
  private boolean  mTouching;
  private boolean  mDrag;

  private float    mX;
  private float    mY;
  private Point    mTouchPoint;
  private Drawable mShadow;

  private void initDragArea()
  {
    mDroppables = new HashMap<OnDragListener, Droppable>();
    mTouching = false;
    mDrag = false;
    mX = 0;
    mY = 0;
    mTouchPoint = new Point(0,0);

    setWillNotDraw(false);
  }

  public DragArea(Context context)
  {
    super(context);
    initDragArea();
  }

  public DragArea(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    initDragArea();
  }

  public DragArea(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initDragArea();
  }

  /**
   * Called to start dragging an object.
   * 
   * @param shadow Shadow image that should be used for visualising the drag operation.
   * @param touch Position within the shadow image that is underneath the touch point.
   */
  public void startDrag(Drawable shadow, Point touchPoint)
  {
    dragStarted();
    // A drag operation will be aborted in the case
    // that the user is no longer touching the view.
    if (mTouching) {
      mShadow = shadow;
      mTouchPoint = touchPoint;

      dragMoved();
    } else {
      dragAborted();
    }
  }

  /**
   * Adds a drag listener to the drag area.
   * 
   * The drag listener object will recieve drag events once any drag
   * operation has been started.
   *
   * @param listener A drag listener to be added to this drag area.
   * @param view The view associated with this drag listener.
   */
  public void addDragListener(View view, OnDragListener listener)
  {
    mDroppables.put(listener, new Droppable(listener, view));
  }

  /**
   * Removes a drag listener.
   *
   * @param listener The drag listener object to be removed.
   */
  public void removeDragListener(OnDragListener listener)
  {
    mDroppables.remove(listener);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event)
  {
    switch (event.getAction())
    {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_MOVE:
        // Make a note of where the last touch event was
        // recieved so that the drag shadow can be drawn
        // in the correct place when the drag operation
        // begins.
        mX = event.getX();
        mY = event.getY();
        mTouching = true;
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        mTouching = false;
        // In the case that mDrag is true here this counts as
        // an aborted drag operation. Need to send out DRAG_ENDED
        // events to all listeners in this case.
        if (mDrag) {
          dragAborted();
        }
        break;
      default:
        break;
    }

    // Returning true from this method signals an intent to 'intercept'
    // touch events. From that point forward all events are delivered
    // to the 'onTouchEvent' handler for this view and will not be
    // passed to child views.
    return mDrag;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
    boolean handled = false;

    if (mDrag) {
      handled = true;
      switch (event.getAction())
      {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
          // Update the touch point
          mX = event.getX();
          mY = event.getY();

          dragMoved();
          invalidate();
          break;
        case MotionEvent.ACTION_UP:
          // Update the touch point
          mX = event.getX();
          mY = event.getY();

          dragDropped();
          invalidate();
        case MotionEvent.ACTION_CANCEL:
          dragAborted();
          invalidate();
        default:
          // Only handle touch events.
          handled = false;
          break;
      }
    }
    return handled;
  }

  @Override
  public void onDraw(Canvas canvas)
  {
    // Draw our child views
    super.onDraw(canvas);

    // Draw the drag shadow
    if (mDrag && (mShadow != null)) {
      canvas.save();
      // Position the drag shadow underneath the touch point
      canvas.translate(mX - mTouchPoint.x, mY - mTouchPoint.y);
      mShadow.draw(canvas);
      canvas.restore();
    }
  }

  private void dragStarted()
  {
    DragEvent dragStarted = new DragEvent(DragEvent.ACTION_DRAG_STARTED, 0, 0);
    for (Droppable d: mDroppables.values())
    {
      d.listener.onDrag(d.view, dragStarted);
    }
    mDrag = true;
  }

  private void dragAborted()
  {
    DragEvent dragEnded = new DragEvent(DragEvent.ACTION_DRAG_ENDED, 0, 0);
    for (Droppable d: mDroppables.values())
    {
      d.listener.onDrag(d.view, dragEnded);
    }
    mDrag = false;
  }

  private void dragMoved()
  {
    for (Droppable d: mDroppables.values())
    {
      boolean hit = isHit(d, (int) mX, (int) mY);
      int event = d.onMoveEvent(hit);

      if (event != 0) {
        DragEvent dragEvent = new DragEvent(event, (int) mX, (int) mY);
        d.listener.onDrag(d.view, dragEvent);
      }
    }
  }

  private void dragDropped()
  {
    for (Droppable d: mDroppables.values())
    {
      boolean hit = isHit(d, (int) mX, (int) mY);
      int event = d.onUpEvent(hit);

      DragEvent dragEvent = new DragEvent(event, (int) mX, (int) mY);
      d.listener.onDrag(d.view, dragEvent);
    }
  }

  /*
   * Calculate if an event at coordinates x, y intersects the droppables
   * view.
   *
   * x, y are coordinates respective to the DragArea.
   */
  private boolean isHit(Droppable droppable, int x, int y)
  {
    Rect hitRect = new Rect(0, 0, droppable.view.getWidth(), droppable.view.getHeight());
    // Translate to DragArea coordinates.
    offsetDescendantRectToMyCoords(droppable.view, hitRect);

    /*
    DragArea.this.debug ("x  = " + x + " y = " + y + "\n" +
                         "left = " + hitRect.left + " top = " + hitRect.top + "\n" +
                         "right = " + hitRect.right + " bottom = " + hitRect.bottom);
     */

    return hitRect.contains(x, y);
  }

  private static class Droppable
  {
    public OnDragListener listener;
    public View           view;

    private boolean mLastEventHit;

    public Droppable(OnDragListener listener, View view)
    {
      this.listener = listener;
      this.view = view;

      mLastEventHit = false;
    }

    /*
     * Update the last event hit and decide which drag event is applicable
     * for this droppable on a move touch event.
     */
    public int onMoveEvent(boolean eventHit)
    {
      int result;

      // If the last event was outside and this one is inside we have ENTERED
      if (!mLastEventHit && eventHit)
        result = DragEvent.ACTION_DRAG_ENTERED;
      // If the last event was inside and this one is inside we have moved LOCATION
      else if (mLastEventHit && eventHit)
        result = DragEvent.ACTION_DRAG_LOCATION;
      // If the last event was inside and this one is outside we have moved EXITED
      else if (mLastEventHit && !eventHit)
        result = DragEvent.ACTION_DRAG_EXITED;
      else
        // There is no 0 for drag events, this indicates that
        // no event should be sent.
        result = 0;

      mLastEventHit = eventHit;
      return result;
    }

    /*
     * Update the last event hit and decide which drag event is applicable
     * for this droppable on an up touch event.
     */
    public int onUpEvent(boolean eventHit)
    {
      int result;

      mLastEventHit = false;

      if (eventHit)
        return DragEvent.ACTION_DROP;
      else
        return DragEvent.ACTION_DRAG_ENDED;
    }
  }
}
