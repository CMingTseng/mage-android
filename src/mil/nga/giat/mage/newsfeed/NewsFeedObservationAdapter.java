package mil.nga.giat.mage.newsfeed;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.bumptech.glide.resize.load.Transformation;

import mil.nga.giat.mage.R;
import mil.nga.giat.mage.form.MageTextView;
import mil.nga.giat.mage.sdk.datastore.common.Geometry;
import mil.nga.giat.mage.sdk.datastore.common.PointGeometry;
import mil.nga.giat.mage.sdk.datastore.observation.Attachment;
import mil.nga.giat.mage.sdk.datastore.observation.Observation;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NewsFeedObservationAdapter extends BaseAdapter {
	
	private static LayoutInflater inflater = null;
	private List<Observation> data;
	private FragmentActivity activity;
	
	public NewsFeedObservationAdapter(FragmentActivity activity, List<Observation> data) {
		this.data = data;
		this.activity = activity;
		inflater = (LayoutInflater)activity.getLayoutInflater();//getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size()-1;
	}

	@Override
	public Observation getItem(int index) {
		return data.get(index);
	}

	@Override
	public long getItemId(int index) {
		return data.get(index).getPk_id();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = inflater.inflate(R.layout.observation_list_item, null);
		}
		LinearLayout ll = (LinearLayout)v.findViewById(R.id.observation_list_container);
		Observation o = getItem(position);
		populatePropertyFields(ll, o.getPropertiesMap());
		
		
		Geometry geo = o.getObservationGeometry().getGeometry();
		if(geo instanceof PointGeometry) {
			PointGeometry pointGeo = (PointGeometry)geo;
			((TextView)v.findViewById(R.id.location)).setText(pointGeo.getLatitude() + ", " + pointGeo.getLongitude());
		}
		
		ImageView iv = ((ImageView)v.findViewById(R.id.observation_thumb));
		Collection<Attachment> attachments = o.getAttachments();
		Log.i("test", "there are " + attachments.size() + " attachments");
		Glide.load("http://www.rosco.com/spectrum/wp-content/uploads/2011/06/Purple-loneliness-purple-18741803-1000-600.jpg").centerCrop().into(iv);
		if (!attachments.isEmpty()) {
			Attachment a = attachments.iterator().next();
//			Glide.load(a.getRemote_path()).centerCrop().into(iv);
			Glide.load("http://www.rosco.com/spectrum/wp-content/uploads/2011/06/Purple-loneliness-purple-18741803-1000-600.jpg").into(iv);
//			http://www.rosco.com/spectrum/wp-content/uploads/2011/06/Purple-loneliness-purple-18741803-1000-600.jpg
		}
		
		return v;
	}
	
	private void populatePropertyFields(LinearLayout ll, Map<String, String> propertiesMap) {
		for (int i = 0; i < ll.getChildCount(); i++) {
			View v = ll.getChildAt(i);
			if (v instanceof MageTextView) {
				MageTextView m = (MageTextView)v;
				String propertyKey = m.getPropertyKey();
				String propertyValue = propertiesMap.get(propertyKey);
				if (propertyValue == null) continue;
				switch(m.getPropertyType()) {
				case STRING:
				case MULTILINE:
					m.setText(propertyValue);
					break;
				case USER:
					
					break;
				case DATE:
					m.setText(new Date(Long.parseLong(propertyValue)).toString());
					break;
				case LOCATION:
					
					break;
				case MULTICHOICE:
					
					break;
				}
			} else if (v instanceof LinearLayout) {
				populatePropertyFields((LinearLayout)v, propertiesMap);
			}
		}
	}

}
