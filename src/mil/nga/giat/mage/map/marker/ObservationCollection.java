package mil.nga.giat.mage.map.marker;

import java.util.Collection;
import java.util.Date;

import mil.nga.giat.mage.filter.Filter;
import mil.nga.giat.mage.sdk.datastore.observation.Observation;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

public interface ObservationCollection extends OnCameraChangeListener, OnMarkerClickListener {
    public void add(Observation observation);
    public void addAll(Collection<Observation> observations);
    public void remove(Observation observation);
    
    public Collection<Observation> getObservations();
    
    public void setVisibility(boolean visible);
    
    public Date getLatestObservationDate();
    
    public void addFilter(Filter<Observation> filter);
    public void removeFilters();
    
    public void clear();
}