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

import android.os.Bundle;

/**
 * Encapsulates all information regarding a Drag and Drop operation.
 *
 * The {@link DragEvent#getAction getAction} method returns the type of drag
 * event. Drag events are sent to all listeners when a drag event
 * is started or ended.
 *
 * Otherwise events are only sent to relevant listeners. 
 * Events are sent selectively when:
 *  A drag operation moves inside a view.
 *  A drag operation leaves the bounds of a view.
 *  A drop operaton occurs inside the bounds of a view.
 *  Movement occurs inside the bounds of a view.
 */
public class DragEvent
{
  /** Drag operation has started */
  public static final int ACTION_DRAG_STARTED  = 1;
  /** Drag location has moved */
  public static final int ACTION_DRAG_LOCATION = 2;
  /** The object has been dropped on this listener */
  public static final int ACTION_DROP          = 3;
  /** The drag operation has ended and not been dropped on the associated view */
  public static final int ACTION_DRAG_ENDED    = 4;
  /** The the Drag operation has entered the bounds of the associated view */
  public static final int ACTION_DRAG_ENTERED  = 5;
  /** The the Drag operation has exited the bounds of the associated view */
  public static final int ACTION_DRAG_EXITED   = 6;

  private int mX;
  private int mY;
  private int mAction;

  private Bundle mData;

  /**
   * Create a DragEvent.
   *
   * @param data The information to pass in the drag event.
   * @param action The type of drag event.
   * @param x The horizontal location of the current touch point. (Relative to associated view)
   * @param y The vertical location of the current touch point. (Relative to associated view)
   */
  public DragEvent(Bundle data, int action, int x, int y)
  {
    mData = data;
    mAction = action;
    mX = x; mY = y;
  }

  /**
   * Get the information associated with this drag and drop operation.
   */
  public Bundle getBundle()
  {
    return mData;
  }

  /**
   * Get the type of drag event.
   *
   * One of: 
   *  {@link DragEvent#ACTION_DRAG_STARTED}
   *  {@link DragEvent#ACTION_DRAG_LOCATION}
   *  {@link DragEvent#ACTION_DROP}
   *  {@link DragEvent#ACTION_DRAG_ENDED}
   *  {@link DragEvent#ACTION_DRAG_ENTERED}
   *  {@link DragEvent#ACTION_DRAG_EXITED}
   *
   */
  public int getAction()
  {
    return mAction;
  }

  /**
   * Get the horizontal location of the current touch point (Relative to associated view).
   */
  public int getX()
  {
    return mX;
  }

  /**
   * Get the vertical location of the current touch point (Relative to associated view).
   */
  public int getY()
  {
    return mY;
  }
}
