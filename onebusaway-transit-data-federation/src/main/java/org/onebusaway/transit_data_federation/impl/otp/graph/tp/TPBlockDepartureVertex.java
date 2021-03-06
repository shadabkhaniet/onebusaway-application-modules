/**
 * Copyright (C) 2011 Brian Ferris <bdferris@onebusaway.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.transit_data_federation.impl.otp.graph.tp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.onebusaway.transit_data_federation.impl.otp.GraphContext;
import org.onebusaway.transit_data_federation.impl.otp.graph.SearchLocal;
import org.onebusaway.transit_data_federation.services.realtime.ArrivalAndDepartureInstance;
import org.onebusaway.transit_data_federation.services.tripplanner.TransferPatternService;
import org.opentripplanner.routing.core.Edge;
import org.opentripplanner.routing.core.HasEdges;

public class TPBlockDepartureVertex extends AbstractTPPathStateVertex implements
    SearchLocal {

  private final ArrivalAndDepartureInstance _departure;

  private final ArrivalAndDepartureInstance _arrival;

  private Object _searchLocalValue = null;

  public TPBlockDepartureVertex(GraphContext context, TPState pathState,
      ArrivalAndDepartureInstance departure, ArrivalAndDepartureInstance arrival) {
    super(context, pathState, true);
    _departure = departure;
    _arrival = arrival;
  }

  public ArrivalAndDepartureInstance getDeparture() {
    return _departure;
  }

  public ArrivalAndDepartureInstance getArrival() {
    return _arrival;
  }

  /****
   * {@link HasEdges} Interface
   ****/

  @Override
  public Collection<Edge> getIncoming() {

    List<Edge> edges = new ArrayList<Edge>();

    if (_pathState.hasTransfers()) {
      TransferPatternService tpService = _context.getTransferPatternService();
      List<TPState> transferStates = _pathState.getTransferStates(tpService);
      for (TPState nextState : transferStates) {
        Edge edge = new TPTransferEdge(_context, _pathState, nextState,
            _departure, _arrival, true);
        edges.add(edge);
      }
    }

    if (_pathState.isExitAllowed()) {
      TPDepartureVertex from = new TPDepartureVertex(_context, _pathState);
      Edge edge = new TPFreeEdge(_context, from, this);
      return Arrays.asList(edge);
    }

    return edges;
  }

  @Override
  public Collection<Edge> getOutgoing() {
    TPBlockHopEdge edge = new TPBlockHopEdge(_context, _pathState, _departure,
        _arrival);
    edge.setFromVertex(this);
    return Arrays.asList((Edge) edge);
  }

  @Override
  public String toString() {
    return "TPBlockDepartureVertex(" + _pathState.toString() + ")";
  }

  /****
   * Search Local
   ****/

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getSearchLocalValue() {
    return (T) _searchLocalValue;
  }

  @Override
  public <T> void setSearchLocalValue(T value) {
    _searchLocalValue = value;
  }
}
