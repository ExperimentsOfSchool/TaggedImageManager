package homework.taggedimagemanager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import homework.taggedimagemanager.model.AbstractTag;

/**
 * Created by rtnelo on 16-12-10.
 */

public class TagHorizontalListView extends RecyclerView {
    private class TagListAdapter extends RecyclerView.Adapter {
        private class TagItemViewHolder extends ViewHolder {
            private Button tagView;

            public TagItemViewHolder(Button itemView) {
                super(itemView);
                tagView = itemView;
            }

            public Button getTagView() {
                return tagView;
            }

            public void setTagView(Button tagView) {
                this.tagView = tagView;
            }
        }

        private List<AbstractTag> tags;

        public TagListAdapter(List<AbstractTag> tags) {
            super();
            this.tags = tags;
        }

        public List<AbstractTag> getTags() {
            return tags;
        }

        public void setTags(List<AbstractTag> tags) {
            this.tags = tags;
        }

        public AbstractTag getItem(int position) {
            return tags.get(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.tag_item, null);
            return new TagItemViewHolder((Button)view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TagItemViewHolder tagItemViewHolder = (TagItemViewHolder)holder;
            tagItemViewHolder.getTagView().setText(getItem(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return tags.size();
        }
    }

    public TagHorizontalListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LinearLayoutManager layoutManager= new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(layoutManager);
    }

    public void setTags(List<AbstractTag> tags) {
        TagListAdapter tagListAdapter = (TagListAdapter)this.getAdapter();
        tagListAdapter.setTags(tags);
        tagListAdapter.notifyDataSetChanged();
    }

    public List<AbstractTag> getTags() {
        TagListAdapter tagListAdapter = (TagListAdapter)this.getAdapter();
        return tagListAdapter.getTags();
    }

    public void addTag(AbstractTag tag) {
        TagListAdapter tagListAdapter = (TagListAdapter)this.getAdapter();
        tagListAdapter.getTags().add(tag);
        tagListAdapter.notifyDataSetChanged();
    }
}
