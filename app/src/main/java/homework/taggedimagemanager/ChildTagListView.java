package homework.taggedimagemanager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import homework.taggedimagemanager.model.AbstractTag;

/**
 * Created by rtnelo on 16-12-10.
 */

public class ChildTagListView extends ListView {
    class ChildTagListAdapter extends BaseAdapter {
        private List<AbstractTag> tags;

        public List<AbstractTag> getTags() {
            return tags;
        }

        public void setTags(List<AbstractTag> tags) {
            this.tags = tags;
            this.notifyDataSetChanged();
        }

        public ChildTagListAdapter(List<AbstractTag> tags) {
            this.tags = tags;
        }

        @Override
        public int getCount() {
            return getTags().size();
        }

        @Override
        public AbstractTag getItem(int i) {
            return getTags().get(i);
        }

        @Override
        public long getItemId(int i) {
            return getTags().get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.child_tag_item, viewGroup);
            }

            EventHandler eventHandler = ((ChildTagListView)viewGroup).getEventHandler();
            AbstractTag tag = getItem(i);

            View item = view.findViewById(R.id.childTagItem);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventHandler.onTagOpened(tag);
                }
            });

            View deleteButton = view.findViewById(R.id.deleteBtn);
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventHandler.onTagDeleted(tag);
                }
            });

            View editButton = view.findViewById(R.id.editBtn);
            editButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventHandler.onTagEdited(tag);
                }
            });

            View checkOnButton = view.findViewById(R.id.checkOnButton);
            checkOnButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventHandler.onTagSelected(tag);
                }
            });

            TextView title = (TextView)findViewById(R.id.titleText);
            title.setText(tag.getTitle());
            return view;
        }
    }

    public interface EventHandler {
        void onTagSelected(AbstractTag tag);
        void onTagEdited(AbstractTag tag);
        void onTagDeleted(AbstractTag tag);
        void onTagOpened(AbstractTag tag);
    }

    private EventHandler eventHandler;

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public ChildTagListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildTagListAdapter getAdapter() {
        return (ChildTagListAdapter)super.getAdapter();
    }

    public void setTags(List<AbstractTag> tags) {
        ChildTagListAdapter childTagListAdapter = getAdapter();
        childTagListAdapter.setTags(tags);
    }
}
