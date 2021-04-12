package chrisbriant.uk.picturegame.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.objects.RoomItem;

public class RoomRecycler extends RecyclerView.Adapter<RoomRecycler.ViewHolder> {
    private Context context;
    private List<RoomItem> roomList;

    public RoomRecycler(Context context, List<RoomItem> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item,parent,false);
        return new RoomRecycler.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomItem room = roomList.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView rmItmName;
        private TextView rmOwner;
        private TextView rmNoPlayers;
        private TextView rmPlayers;
        private TextView rmStatus;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            rmItmName = itemView.findViewById(R.id.rmItmName);
            rmOwner = itemView.findViewById(R.id.rmOwner);
            rmNoPlayers = itemView.findViewById(R.id.rmNoPlayers);
            rmPlayers = itemView.findViewById(R.id.rmPlayers);
            rmStatus = itemView.findViewById(R.id.rmStatus);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
