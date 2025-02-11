package dam.pmdm.spyrothedragon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private final List<Collectible> list;

    private OnCollectibleClickListener listener;

    public void setOnCollectibleClickListener(OnCollectibleClickListener listener) {
        this.listener = listener;
    }

    public CollectiblesAdapter(List<Collectible> collectibleList) {
        this.list = collectibleList;
    }

    @NonNull
    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen
        int imageResId = holder.itemView.getContext().getResources()
                .getIdentifier(collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        // Agregar click listener para notificar el clic en el collectible
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCollectibleClick(collectible);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }

    public interface OnCollectibleClickListener {
        void onCollectibleClick(Collectible collectible);
    }
}
