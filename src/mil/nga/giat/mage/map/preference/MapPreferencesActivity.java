package mil.nga.giat.mage.map.preference;

import java.util.HashSet;
import java.util.Set;

import mil.nga.giat.mage.R;
import mil.nga.giat.mage.preferences.PreferenceFragmentSummary;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Provides map configuration driven settings that are available to the user.
 * Check mappreferences.xml for the configuration.
 * 
 * @author newmanw
 * 
 */
public class MapPreferencesActivity extends PreferenceActivity {

    public static final int TILE_OVERLAY_ACTIVITY = 0;
    public static final int FEATURE_OVERLAY_ACTIVITY = 1;
    public static final String OVERLAY_EXTENDED_DATA_KEY = "overlay";
    
    private MapPreferenceFragment preference = new MapPreferenceFragment();
    
    public static class MapPreferenceFragment extends PreferenceFragmentSummary {
        
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.mappreferences);
            
            findPreference("tileOverlays").setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), TileOverlayPreferenceActivity.class);
                    getActivity().startActivityForResult(intent, TILE_OVERLAY_ACTIVITY);
                    return true;
                }
            });

            findPreference("featureOverlays").setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), FeatureOverlayPreferenceActivity.class);
                    getActivity().startActivityForResult(intent, FEATURE_OVERLAY_ACTIVITY);
                    return true;
                }
            });
            
            PreferenceManager.setDefaultValues(getActivity(), R.xml.mappreferences, true);
        }

        @Override
        public void onResume() {
            super.onResume();
            
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                Preference preference = getPreferenceScreen().getPreference(i);
                setSummary(preference);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, preference).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case TILE_OVERLAY_ACTIVITY: {
            if (resultCode == Activity.RESULT_OK) {
                Set<String> overlays = new HashSet<String>(data.getStringArrayListExtra(OVERLAY_EXTENDED_DATA_KEY));
                OverlayPreference p = (OverlayPreference) preference.findPreference("tileOverlays");
                p.setValues(overlays);
            }
            break;
        }
        case FEATURE_OVERLAY_ACTIVITY: {
            if (resultCode == Activity.RESULT_OK) {
                Set<String> overlays = new HashSet<String>(data.getStringArrayListExtra(OVERLAY_EXTENDED_DATA_KEY));
                OverlayPreference p = (OverlayPreference) preference.findPreference("featureOverlays");
                p.setValues(overlays);
            }
            break;
        }
        default:
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}