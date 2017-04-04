package emilp.hallo.view;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import emilp.hallo.ListTypeHorizontal;
import emilp.hallo.ListTypeVertical;
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

        if(direction == LinearLayoutManager.HORIZONTAL)
            adapter = new MenuAdapter(activity, new ListTypeHorizontal(activity, data));
        else
            adapter = new MenuAdapter(activity, new ListTypeVertical(activity, data));

        recyclerView.setAdapter(adapter);
    }

    public void setTitle(int res) {
        TextView textView = (TextView) view.findViewById(R.id.list_horizontal_title);
        textView.setText(res);
    }

}
