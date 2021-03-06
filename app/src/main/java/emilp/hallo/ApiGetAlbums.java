package emilp.hallo;

import android.os.AsyncTask;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import emilp.hallo.view.ContentList;

class ApiGetAlbums {

    private ContentList contentList;
    private ArrayList<Content> songs = new ArrayList<>();
    private String query = null;
    private int limit = 3;

    /**
     * Using this constructor, <code>limit</code> random results will be returned
     */
    ApiGetAlbums(ContentList contentList, int limit) {
        this.contentList = contentList;
        this.limit = limit;

        contentList.init(songs);
        getSongHistory();
    }

    /**
     * Using this constructor, results will be based on the query
     */
    ApiGetAlbums(ContentList contentList, String query) {
        this.contentList = contentList;
        this.query = query;

        contentList.init(songs);
        getSongHistory();
    }

    ApiGetAlbums(ContentList contentList, Artist artist) {
        this.contentList = contentList;

        contentList.init(songs);
        getAlbumsFromArtist(artist);
    }

    /**
     * Get albums from a given artist
     */
    private void getAlbumsFromArtist(final Artist artist) {
        URL url = NetworkUtils.buildArtistAlbumsURL(artist.getSpotifyID());
        new AsyncTask<URL, Void, Void>() {
            @Override
            protected Void doInBackground(URL... urls) {
                URL searchUrl = urls[0];
                try {
                    JSONObject res = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    parseArtistAlbumsJSON(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                contentList.notifyDataSetChanged();
                contentList.getSpinner().setVisibility(View.GONE);
            }
        }.execute(url);
    }

    private void addSongToResult(Content song) {
        songs.add(song);
    }

    /**
     * get the users song history
     */
    private void getSongHistory() {
        URL url;
        if(query == null)
            url = NetworkUtils.buildRandom("album", limit);
        else
            url = NetworkUtils.buildUrlSearch(query, "album");
        new AsyncTask<URL, Void, Void>(){
            @Override
            protected void onPreExecute(){
                contentList.getSpinner().setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(URL... params) {
                URL searchUrl = params[0];
                try {
                    JSONObject res = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    parseTracksJSON(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                contentList.notifyDataSetChanged();
                contentList.getSpinner().setVisibility(View.GONE);
            }
        }.execute(url);
    }

    private void extractArtists(JSONObject obj, Album album) throws JSONException {
        JSONArray artists = obj.getJSONArray("artists");
        for(int k=0; k<artists.length(); k++) {
            JSONObject b = artists.getJSONObject(k);
            String bName = b.getString("name");
            String bId = b.getString("id");
            Artist artist = new Artist(bName, bId);
            album.addArtists(artist);
        }
    }

    private void parseArtistAlbumsJSON(JSONObject obj) {
        try {
            JSONArray arr = obj.getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);
                String aName = obj.getString("name");
                String aId = obj.getString("id");
                String aUrl = obj.getJSONArray("images").getJSONObject(obj.getJSONArray("images").length()-1).getString("url");
                Album album = new Album(aName, aId, aUrl);
                album.downloadImage();

                extractArtists(obj, album);

                addSongToResult(album);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseTracksJSON(JSONObject obj) {
        try {
            JSONArray arr = obj.getJSONObject("albums").getJSONArray("items");
            for(int i=0; i<arr.length(); i++) {
                obj = arr.getJSONObject(i);
                String aName = obj.getString("name");
                String aId = obj.getString("id");
                String aUrl = obj.getJSONArray("images").getJSONObject(obj.getJSONArray("images").length()-1).getString("url");
                Album album = new Album(aName, aId, aUrl);
                album.downloadImage();

                JSONArray artists = obj.getJSONArray("artists");
                for(int k=0; k<artists.length(); k++) {
                    JSONObject b = artists.getJSONObject(k);
                    String bName = b.getString("name");
                    String bId = b.getString("id");
                    Artist artist = new Artist(bName, bId);
                    album.addArtists(artist);
                }

                addSongToResult(album);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
