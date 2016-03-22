package org.openweathermap.api;

import org.openweathermap.api.model.WeatherInfo;
import org.openweathermap.api.query.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by iuriis on 22.03.2016.
 */
public class UrlConnectionWeatherClient extends AbstractWeatherClient {
    private final String apiKey;

    public UrlConnectionWeatherClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getWeatherData(Query query) {
        return makeRequest(query.toStringRepresentation(apiKey));
    }

    @Override
    public WeatherInfo getWeatherInfo(Query query) {
        return toWeatherInfo(getWeatherData(query), query.getResponseFormat());
    }

    private String makeRequest(String query) {
        try {
            URL url = getUrl(query);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets url from query string.
     *
     * @param query the query
     * @return the url
     */
    private URL getUrl(String query) {
        try {
            return new URL(query);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed query", e);
        }
    }
}
