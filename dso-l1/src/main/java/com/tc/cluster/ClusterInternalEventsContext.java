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
package com.tc.cluster;

import com.tc.cluster.ClusterEvent;
import com.tc.cluster.ClusterListener;
import com.tc.cluster.ClusterInternal.ClusterEventType;

/**
 * Cluster Events Contexts to be put in ClusterInternalEventsHandler.
 */
public class ClusterInternalEventsContext {

  private final ClusterEventType eventType;
  private final ClusterEvent     event;
  private final ClusterListener clusterListener;

  public ClusterInternalEventsContext(ClusterEventType eventType, ClusterEvent event, ClusterListener listener) {
    this.eventType = eventType;
    this.event = event;
    this.clusterListener = listener;
  }

  public ClusterEventType getEventType() {
    return eventType;
  }

  public ClusterEvent getEvent() {
    return event;
  }

  public ClusterListener getClusterListener() {
    return clusterListener;
  }

  @Override
  public String toString() {
    return "ClusterInternalEventsContext [eventType=" + eventType + ", event=" + event + ", clusterListener=" + clusterListener + "]";
  }

}
