package emilp.hallo.view;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import emilp.hallo.Content;
import emilp.hallo.MenuAdapter;
import emilp.hallo.R;

/**
 * Created by jonas on 2017-04-04.
 */

public class ContentList {

    private Activity activity;
    private MenuAdapter adapter;
    private View view;

    /**
     * Creates a list in the given direction with the provided data
     * @param activity
     *  Activity that contains the list
     * @param container
     * The view that is the correct layout
     * @param direction
     * The direction of the list
     * @param data
     * The data to be displayed
     */
    public ContentList(Activity activity, int container, int direction, Object[] data) {
        this.activity = activity;
        this.view = activity.findViewById(container);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.song_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, direction, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        int layout = R.layout.list_item_song;
        if(direction == LinearLayoutManager.HORIZONTAL)
            layout = R.layout.song_history_item;

        adapter = new MenuAdapter(activity, layout, data) {
            @Override
            public void onClickListener(View view, Content content) {
                super.onClickListener(view, content);
                onItemClick(view, content);
            }

            @Override
            protected void onSecondClickListener(View view, Content content) {
                super.onSecondClickListener(view, content);
                onSecondItemClick(view, content);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    protected void onItemClick(View view, Content content) {
    }
    protected void onSecondItemClick(View view, Content content) {
    }

    public void setTitle(int res) {
        TextView textView = (TextView) view.findViewById(R.id.list_horizontal_title);
        textView.setText(res);
    }

}
