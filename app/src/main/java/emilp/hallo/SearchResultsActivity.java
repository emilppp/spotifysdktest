package emilp.hallo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import emilp.hallo.view.ContentList;
import emilp.hallo.view.MoreOptions;

/**
 * This activity displays the search results
 */
public class SearchResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SearchResultsActivity", query);

            searchArtists(query);
            searchAlbums(query);
            searchSongs(query);
        }
    }

    private ContentList getContentList(int id, int direction, final Class<?> c) {
        return new ContentList(SearchResultsActivity.this, id, direction) {
            @Override
            protected void onItemClick(View view, Content content) {
                ((GlobalApplication) getApplication()).setCurrentContent(content);
                Intent intent = new Intent(SearchResultsActivity.this, c);
                startActivity(intent);
            }
        };
    }

    /**
     * Searches songs based on the given query
     */
    private void searchSongs(String query) {
        ContentList contentList = new ContentList(SearchResultsActivity.this, R.id.search_results_songs, LinearLayoutManager.VERTICAL) {
            @Override
            protected void onItemClick(View view, Content content) {
                super.onItemClick(view, content);
                GlobalApplication global = (GlobalApplication) getApplication();
                global.getSpotifyService().playSong(global, (Song) content);
            }

            @Override
            protected void onSecondItemClick(View view, Content content) {
                super.onSecondItemClick(view, content);
                new MoreOptions(SearchResultsActivity.this, content);
            }
        };
        contentList.setTitle(R.string.songs);
        // TODO: Song view?
        new ApiGetSongs(contentList, query);
    }

    /**
     * Searches artists based on the given query
     */
    private void searchArtists(String query) {
        ContentList contentList = getContentList(R.id.search_results_artists, LinearLayoutManager.HORIZONTAL, ArtistPage.class);
        contentList.setTitle(R.string.artists);
        new ApiGetArtists(contentList, query);
    }

    /**
     * Searches albums based on the given query
     */
    private void searchAlbums(String query) {
        ContentList contentList = getContentList(R.id.search_results_albums, LinearLayoutManager.HORIZONTAL, AlbumPage.class);
        contentList.setTitle(R.string.albums);
        new ApiGetAlbums(contentList, query);
    }
}
