/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package emilp.hallo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String SPOTIFY_BASE_URL =
            "https://api.spotify.com/v1/search";

    final static String PARAM_QUERY = "q";
    final static String PARAM_TYPE = "type";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */


    public static URL buildUrl(String githubSearchQuery, String type) {
        Uri builtUri = Uri.parse(SPOTIFY_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_TYPE, type).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static JSONObject getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                try {
                    String res = scanner.next();
                    return new JSONObject(res);
                } catch (JSONException e) {
                    return null;
                }
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    /**
     *
     * @param url
     * @return The bitmap of the url's image
     */
    public static Bitmap getBitmapFromUrl(String url) {
        Bitmap image = null;
        try {
            URL u = new URL(url);
            image = BitmapFactory.decodeStream(u.openStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}