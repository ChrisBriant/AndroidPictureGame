package chrisbriant.uk.picturegame.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        Log.d("ROOM STATUS IN HOLDER", String.valueOf(room.isRoomStatus()));
        if(room.isRoomStatus()) {
            holder.rmStatus.setText("Locked");
        } else {
            holder.rmStatus.setText("Open");
        }

        Log.d("BINDING",room.getRoomName());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void setRoomList(List<RoomItem> roomList) {
        this.roomList = roomList;
        Log.d("ROOM LIST SET", "I am setting the room list");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView rmItmName;
        private TextView rmOwner;
        private TextView rmNoPlayers;
        private TextView rmStatus;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            rmItmName = itemView.findViewById(R.id.rmItmName2);
            rmOwner = itemView.findViewById(R.id.rmOwner2);
            rmNoPlayers = itemView.findViewById(R.id.rmNoPlayers2);
            rmStatus = itemView.findViewById(R.id.rmStatus2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RoomItem r = roomList.get(getAdapterPosition());
            if(r.isRoomStatus()) {
                Toast.makeText(context,"Sorry room is not available.",Toast.LENGTH_SHORT).show();
            } else {
                //Enter room here
                
            }
        }
    }
}
