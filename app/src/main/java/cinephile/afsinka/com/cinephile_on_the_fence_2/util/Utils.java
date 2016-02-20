package cinephile.afsinka.com.cinephile_on_the_fence_2.util;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cinephile.afsinka.com.cinephile_on_the_fence_2.R;
import cinephile.afsinka.com.cinephile_on_the_fence_2.model.Film;

/**
 * Created by afsin on 19.2.2016.
 */
public class Utils {

    public static List<Film> getAllFilms(Resources resources) {
        List<Film> list = new ArrayList<Film>();
        try {
            InputStream in = resources.openRawResource(R.raw.films);

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                //dammit video games
                if (!line.contains("(VG)")) {

                    String numberOfVotes = line.substring(0, line.indexOf(" "));
                    String nameAndRate = line.substring(line.indexOf(" "), line.length()).trim();

                    String rating = nameAndRate.substring(0, nameAndRate.indexOf(" "));
                    String name = nameAndRate.substring(nameAndRate.indexOf(" "), nameAndRate.length()).trim();

                    list.add(new Film(name, Integer.parseInt(numberOfVotes), Double.parseDouble(rating)));

                }
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> getAllWords(Resources resources) {
        List<String> list = new ArrayList<String>();
        try {
            InputStream in = resources.openRawResource(R.raw.names);

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.contains("'") && !line.contains(".") && !line.contains("-") && !line.contains("/")) {
                    list.add(line);
                }
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static String getRandomWord(List<String> list) {
        Random r = new Random();
        if (list.size() > 0) {
            int value = r.nextInt(list.size());
            return list.get(value);
        } else {
            return null;
        }
    }

    public static Film getFilm(List<Film> list, double rate, int votes) {

        List<Film> filtered = new ArrayList<Film>();

        for (Film f : list) {
            if (f.getRating() >= rate && f.getNoOfVotes() >= votes) {
                filtered.add(f);
            }

            //stop memory exceed
            if (filtered.size() > 5000) {
                break;
            }
        }

        Random r = new Random();
        if (filtered.size() > 0) {
            int value = r.nextInt(filtered.size());
            return filtered.get(value);
        } else {
            return null;
        }


    }
}
