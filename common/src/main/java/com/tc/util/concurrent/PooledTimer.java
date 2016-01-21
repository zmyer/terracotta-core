/*
 *
 *  The contents of this file are subject to the Terracotta Public License Version
 *  2.0 (the "License"); You may not use this file except in compliance with the
 *  License. You may obtain a copy of the License at
 *
 *  http://terracotta.org/legal/terracotta-public-license.
 *
 *  Software distributed under the License is distributed on an "AS IS" basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 *  the specific language governing rights and limitations under the License.
 *
 *  The Covered Software is Terracotta Core.
 *
 *  The Initial Developer of the Covered Software is
 *  Terracotta, Inc., a Software AG company
 *
 */

package com.tc.util.concurrent;

import com.tc.text.StringUtils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Timer} implementation based on a shared thread pool.
 *
 * @author Eugene Shelestovich
 * @see Timer
 * @see ScheduledNamedTaskRunner
 */
class PooledTimer implements Timer {

  private static final String ALREADY_CANCELLED_MSG = "Cannot schedule a task - the timer has been already cancelled";

  private final ScheduledNamedTaskRunner executor;
  private final String name;
  private volatile boolean cancelled;

  public PooledTimer(ScheduledNamedTaskRunner executor) {
    this(null, executor);
  }

  public PooledTimer(String name, ScheduledNamedTaskRunner executor) {
    if(executor == null) {
      throw new IllegalArgumentException("executor should not be null");
    }

    this.name = name;
    this.executor = executor;
  }

  @Override
  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    if(cancelled) {
      throw new IllegalStateException(ALREADY_CANCELLED_MSG);
    }
    return executor.schedule(wrap(command), delay, unit);
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay,
                                                long period, TimeUnit unit) {
    if(cancelled) {
      throw new IllegalStateException(ALREADY_CANCELLED_MSG);
    }
    return executor.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
  }

  @Override
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,
                                                   long delay, TimeUnit unit) {
    if(cancelled) {
      throw new IllegalStateException(ALREADY_CANCELLED_MSG);
    }
    return executor.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
  }

  @Override
  public void execute(Runnable command) {
    if(cancelled) {
      throw new IllegalStateException(ALREADY_CANCELLED_MSG);
    }
    executor.execute(wrap(command));
  }

  @Override
  public void cancel() {
    executor.cancelTimer(this);
    cancelled = true;
  }

  private Runnable wrap(final Runnable r) {
    return (StringUtils.isNotBlank(name)) ? new TimerNamedRunnable() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public Timer getTimer() {
        return PooledTimer.this;
      }

      @Override
      public void run() {
        r.run();
      }
    } : r;
  }
}
