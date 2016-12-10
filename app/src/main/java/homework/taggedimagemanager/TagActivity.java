package homework.taggedimagemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import homework.taggedimagemanager.model.AbstractTag;
import homework.taggedimagemanager.model.DatabaseManager;
import homework.taggedimagemanager.model.Tag;

public class TagActivity extends AppCompatActivity {
    private ChildTagListView childTagListView;

    private AbstractTag root;

    private static final int OPEN_TAG_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTagDialog();
            }
        });

        Intent intent = getIntent();
        TextView rootText = (TextView)findViewById(R.id.rootText);
        root = (AbstractTag)intent.getSerializableExtra("root");
        if (root == null) {
            rootText.setVisibility(View.GONE);
        } else {
            rootText.setText(root.getTitle());
        }

        ArrayList<AbstractTag> children = (ArrayList<AbstractTag>)intent.getSerializableExtra("children");

        childTagListView = (ChildTagListView)findViewById(R.id.childList);
        childTagListView.setTags(children);

        childTagListView.setEventHandler(new ChildTagListView.EventHandler() {
            @Override
            public void onTagSelected(AbstractTag tag) {
                TagActivity.this.onTagSelected(tag);
            }

            @Override
            public void onTagEdited(AbstractTag tag) {
                modifyTag(tag);
            }

            @Override
            public void onTagDeleted(AbstractTag tag) {
                deleteTag(tag);
            }

            @Override
            public void onTagOpened(AbstractTag tag) {

            }
        });
    }

    private void onTagSelected(AbstractTag tag) {
        Intent intent = new Intent();
        intent.putExtra("tag", tag);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_TAG_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    private void addTag(String title) {
        AbstractTag tag = DatabaseManager.getInstance().insertTag(root, title);
    }

    private void addTagDialog() {
        final EditText textTitle = new EditText(this);

        textTitle.setHint("Tag Title");

        new AlertDialog.Builder(this)
                .setTitle("Input Tag Title")
                .setMessage("Set the title for the new tag.")
                .setView(textTitle)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void deleteTag(AbstractTag tag) {
        return;
    }

    private void modifyTag(AbstractTag tag) {
        return;
    }

    private void openTagActivity(AbstractTag tag) {
        Intent intent = new Intent(this, TagActivity.class);
        Tag fullTag = DatabaseManager.getInstance().getFullTag(tag);
        intent.putExtra("root", fullTag);
        intent.putExtra("children", fullTag.getChildren());
        startActivityForResult(intent, OPEN_TAG_ACTIVITY);
    }
}
