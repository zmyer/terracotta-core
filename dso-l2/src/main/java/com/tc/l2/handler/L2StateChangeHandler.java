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
package com.tc.l2.handler;

import com.tc.async.api.AbstractEventHandler;
import com.tc.async.api.ConfigurationContext;
import com.tc.async.impl.StageController;
import com.tc.l2.context.StateChangedEvent;
import com.tc.l2.state.StateManager;
import com.tc.objectserver.core.api.ITopologyEventCollector;
import com.tc.objectserver.core.api.ServerConfigurationContext;
import com.tc.server.TCServer;
import com.tc.server.TCServerMain;
import com.tc.services.LocalMonitoringProducer;
import com.tc.util.State;


public class L2StateChangeHandler extends AbstractEventHandler<StateChangedEvent> {

  private StateManager stateManager;
  private final StageController stageManager;
  private ConfigurationContext context;
  private final ITopologyEventCollector eventCollector;

  public L2StateChangeHandler(StageController stageManager, ITopologyEventCollector eventCollector) {
    this.stageManager = stageManager;
    this.eventCollector = eventCollector;
  }

  @Override
  public void handleEvent(StateChangedEvent sce) {
// execute state transition before notifying any listeners.  Listener notification 
// can happen in any order.  State transition happens in specific order as dictated 
// by the stage controller.
    State newState = sce.getCurrentState();
    // notify the collector that the server's state first to mark the start of transition
    if (sce.movedToActive()) {
      TCServerMain.getServer().updateActivateTime(); // make sure activateTime is updated if needed
//  if this server just became active
      eventCollector.serverDidEnterState(newState, TCServerMain.getServer().getActivateTime());      
    } else {
      eventCollector.serverDidEnterState(newState, System.currentTimeMillis());      
    }
    stageManager.transition(context, sce.getOldState(), newState);
    stateManager.fireStateChangedEvent(sce);
  }

  @Override
  public void initialize(ConfigurationContext context) {
    super.initialize(context);
    ServerConfigurationContext oscc = (ServerConfigurationContext) context;
    this.stateManager = oscc.getL2Coordinator().getStateManager();
    this.context = context;
  }

}
