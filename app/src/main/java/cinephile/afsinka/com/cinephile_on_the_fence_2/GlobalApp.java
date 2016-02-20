package cinephile.afsinka.com.cinephile_on_the_fence_2;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cinephile.afsinka.com.cinephile_on_the_fence_2.model.Film;

/**
 * Created by afsin on 20.2.2016.
 */
public class GlobalApp extends Application {

    private List<Film> allFilms = new ArrayList<Film>();
    private List<String> allWords = new ArrayList<String>();

    public List<Film> getAllFilms() {
        return allFilms;
    }

    public void setAllFilms(List<Film> allFilms) {
        this.allFilms = allFilms;
    }

    public List<String> getAllWords() {
        return allWords;
    }

    public void setAllWords(List<String> allWords) {
        this.allWords = allWords;
    }
}