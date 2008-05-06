/*
 * All content copyright (c) 2003-2008 Terracotta, Inc., except as may otherwise be noted in a separate copyright notice.  All rights reserved.
 */
package com.tc.management.lock.stats;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.tc.net.groups.NodeID;
import com.tc.object.lockmanager.api.LockID;
import com.tc.object.lockmanager.api.ThreadID;

public class LockKey {
  private LockID   lockID;
  private NodeID   nodeID;
  private ThreadID threadID;
  private int      hashCode;

  private LockKey  subKey;

  public LockKey(LockID lockID, NodeID nodeID) {
    this.lockID = lockID;
    this.nodeID = nodeID;
    this.threadID = null;
    this.subKey = null;
    this.hashCode = new HashCodeBuilder(5503, 6737).append(lockID).append(nodeID).toHashCode();
  }
  
  public LockKey(NodeID nodeID, ThreadID threadID) {
    this.lockID = null;
    this.nodeID = nodeID;
    this.threadID = threadID;
    this.hashCode = new HashCodeBuilder(5503, 6737).append(nodeID).append(threadID).toHashCode();
  }

  public LockKey(LockID lockID, NodeID nodeID, ThreadID threadID) {
    this.lockID = lockID;
    this.nodeID = nodeID;
    this.threadID = threadID;
    this.hashCode = new HashCodeBuilder(5503, 6737).append(lockID).append(nodeID).append(threadID).toHashCode();
    this.subKey = new LockKey(lockID, nodeID);
  }

  public String toString() {
    return "LockKey [ " + lockID + ", " + nodeID + ", " + threadID + ", " + hashCode + "] ";
  }

  public NodeID getNodeID() {
    return nodeID;
  }

  public LockID getLockID() {
    return lockID;
  }

  public ThreadID getThreadID() {
    return threadID;
  }

  public LockKey subKey() {
    return subKey;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof LockKey)) return false;
    LockKey cmp = (LockKey) o;
    if (lockID == null) {
      return nodeID.equals(cmp.nodeID) && threadID.equals(cmp.threadID);
    } else if (threadID != null) {
      return lockID.equals(cmp.lockID) && nodeID.equals(cmp.nodeID) && threadID.equals(cmp.threadID);
    } else {
      return lockID.equals(cmp.lockID) && nodeID.equals(cmp.nodeID);
    }
  }

  public int hashCode() {
    return hashCode;
  }
}

