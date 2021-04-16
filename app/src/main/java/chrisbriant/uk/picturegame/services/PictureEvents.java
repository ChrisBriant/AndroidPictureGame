package chrisbriant.uk.picturegame.services;

import android.util.Log;

public class PictureEvents {

        public interface PictureEventListener {
            public void onMessage(String data);
        }

        // Member variable was defined earlier
        private PictureEventListener listener;

        // Constructor where listener events are ignored
        public PictureEvents() {
            // set null or default listener or accept as argument to constructor
            this.listener = null;
            //loadDataAsync();
        }

        // ... setter defined here as shown earlier
        // Assign the listener implementing events interface that will receive the events
        public void setCustomObjectListener(PictureEventListener listener) {
            this.listener = listener;
        }

        public void triggerEvent(String data) {
            Log.d("TRIGGER", "Picture event triggered");
            listener.onMessage(data);
        }

}
