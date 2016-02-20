package cinephile.afsinka.com.cinephile_on_the_fence_2.model;

/**
 * Created by afsin on 20.2.2016.
 */
public class Film {

    private String name;
    private int noOfVotes;
    private double rating;

    public Film(String nameArg, int noOfVotesArg, double ratingArg) {
        name = nameArg;
        noOfVotes = noOfVotesArg;
        rating = ratingArg;
    }

    public int getNoOfVotes() {
        return noOfVotes;
    }

    public void setNoOfVotes(int noOfVotes) {
        this.noOfVotes = noOfVotes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Film=[").append(name).append(",").append(rating).append(",").append(noOfVotes).append("]");
        return sb.toString();
    }
}
