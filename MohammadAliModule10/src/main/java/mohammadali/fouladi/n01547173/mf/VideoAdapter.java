package mohammadali.fouladi.n01547173.mf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private String[] videoDescriptions;
    private String[] videoUrls;
    private WebView videoWebView;

    public VideoAdapter(String[] videoDescriptions, String[] videoUrls, WebView videoWebView) {
        this.videoDescriptions = videoDescriptions;
        this.videoUrls = videoUrls;
        this.videoWebView = videoWebView;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(videoDescriptions[position], videoUrls[position]);
    }

    @Override
    public int getItemCount() {
        return videoDescriptions.length;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
            imageView= itemView.findViewById(android.R.id.icon);
        }

        public void bind(String description, String videoUrl) {
            textView.setText(description);
            imageView.setImageResource(R.drawable.demon);
            itemView.setOnClickListener(v -> videoWebView.loadUrl(videoUrl));
        }
    }
}
