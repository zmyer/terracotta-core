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

/**
 * The {@code Cluster} interface provides access to Terracotta cluster events and meta data.
 * <p>
 * Note that only terracotta client nodes are taken into account for the cluster events and meta data, information about
 * TSA server nodes is not available.
 * <p>
 * See {@link ClusterListener} for more information about the events themselves.
 * 
 * @since 3.0.0
 */
public interface Cluster {
  /**
   * Adds a cluster events listener.
   * <p>
   * If the cluster events listener instance has already been registered before, this method will not register it again.
   * <p>
   * When the cluster is already joined or the operations have already been enabled, those events will be immediately
   * triggered on the listener when it's registered.
   *
   * @param listener the cluster listener instance that will be registered
   */
  public void addClusterListener(ClusterListener listener);

  /**
   * Removes a cluster events listener.
   * <p>
   * If the cluster events listener instance was not registered before, this method will have no effect.
   *
   * @param listener the cluster listener instance that will be unregistered
   */
  public void removeClusterListener(ClusterListener listener);

  /**
   * Retrieves a view of the topology of the cluster, as seen from the current node.
   * <p>
   * Note that the returned topology instance will be updated internally as nodes joined and leave the cluster. If you
   * want a snapshot of the current nodes in the cluster, you should use the {@link DsoClusterTopology#getNodes()}
   * method.
   *
   * @return an instance of the cluster topology as seen from the current node
   */
  public ClusterTopology getClusterTopology();

  /**
   * Retrieves the {@code DsoNode} instance that corresponds to the current node. May return null if this node is not
   * connected to the cluster yet.
   * 
   * @return the {@code DsoNode} instance that corresponds to the current node. May return null if this node is not
   *         connected to the cluster yet.
   */
  public Node getCurrentNode();

  /**
   * Waits until this node joins the cluster. This operation can be interrupted.
   * 
   * @return the {@code DsoNode} instance that corresponds to the current node. May return null if the thread is
   *         interrupted before the current node joins the cluster.
   */
  public Node waitUntilNodeJoinsCluster();

  /**
   * Indicates whether the current node has joined the cluster.
   *
   * @return {@code true} if the current node has joined the cluster; {@code false} otherwise
   */
  public boolean isNodeJoined();

  /**
   * Indicates whether operations are enabled on the current node.
   *
   * @return {@code true} if operations are enabled on the current node; {@code false} otherwise
   */
  public boolean areOperationsEnabled();

}