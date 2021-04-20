package chrisbriant.uk.picturegame.adapters;

import android.content.Context;
import android.util.Log;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item2,parent,false);
        return new RoomRecycler.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomItem room = roomList.get(position);

        holder.rmItmName.setText(room.getRoomName());
        holder.rmOwner.setText(room.getOwner());
        holder.rmNoPlayers.setText(String.valueOf(room.getPlayerCount()));
        if(room.isRoomStatus()) {
            holder.rmStatus.setText("Open");
        } else {
            holder.rmStatus.setText("Locked");
        }

        Log.d("BINDING",room.getRoomName());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void setRoomList(List<RoomItem> roomList) {
        this.roomList = roomList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView rmItmName;
        private TextView rmOwner;
        private TextView rmNoPlayers;
        private TextView rmPlayers;
        private TextView rmStatus;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            rmItmName = itemView.findViewById(R.id.rmItmName2);
            rmOwner = itemView.findViewById(R.id.rmOwner2);
            rmNoPlayers = itemView.findViewById(R.id.rmNoPlayers2);
            rmPlayers = itemView.findViewById(R.id.rmPlayers2);
            rmStatus = itemView.findViewById(R.id.rmStatus2);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
