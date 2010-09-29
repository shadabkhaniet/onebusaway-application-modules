package org.onebusaway.transit_data_federation.impl.tripplanner.offline;

import java.io.Serializable;
import java.util.List;

import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.transit_data_federation.services.tripplanner.BlockEntry;
import org.onebusaway.transit_data_federation.services.tripplanner.StopTimeEntry;
import org.onebusaway.transit_data_federation.services.tripplanner.TripEntry;

public class BlockEntryImpl implements BlockEntry, Serializable {

  private static final long serialVersionUID = 2L;

  private AgencyAndId _id;

  private List<TripEntry> _trips;

  private List<StopTimeEntry> _stopTimes;

  private double _totalBlockDistance = 0;

  public void setId(AgencyAndId id) {
    _id = id;
  }

  public void setTrips(List<TripEntry> trips) {
    _trips = trips;
  }

  public void setStopTimes(List<StopTimeEntry> stopTimes) {
    _stopTimes = stopTimes;
  }

  public void setTotalBlockDistance(double totalBlockDistance) {
    _totalBlockDistance = totalBlockDistance;
  }

  /****
   * {@link BlockEntry} Interface
   ****/

  @Override
  public AgencyAndId getId() {
    return _id;
  }

  @Override
  public List<TripEntry> getTrips() {
    return _trips;
  }

  @Override
  public List<StopTimeEntry> getStopTimes() {
    return _stopTimes;
  }

  @Override
  public double getTotalBlockDistance() {
    return _totalBlockDistance;
  }

  @Override
  public String toString() {
    return _id.toString();
  }
}