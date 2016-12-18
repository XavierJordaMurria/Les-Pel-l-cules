/**
 * Created by xj on 09/12/2016.
 */

package cat.jorda.xavier.lespellicules;

public class MovieInfo
{
    /**
     *      ex of received information from the movie db
     *{
     "poster_path": "/h6O5OE3ueRVdCc7V7cwTiQocI7D.jpg",
     "adult": false,
     "overview": "On January 15, 2009, the world witnessed the \"Miracle on the Hudson\" when Captain \"Sully\" Sullenberger glided his disabled plane onto the frigid waters of the Hudson River, saving the lives of all 155 aboard. However, even as Sully was being heralded by the public and the media for his unprecedented feat of aviation skill, an investigation was unfolding that threatened to destroy his reputation and his career.",
     "release_date": "2016-09-07",
     "genre_ids": [
     18,
     36
     ],
     "id": 363676,
     "original_title": "Sully",
     "original_language": "en",
     "title": "Sully",
     "backdrop_path": "/vC9H1ZVdXi1KjH4aPfGB54mvDNh.jpg",
     "popularity": 21.751519,
     "vote_count": 332,
     "video": false,
     "vote_average": 6.5
     }
     */
    public final int mID;
    public final String mTitle;
    public final String mOriginalTitle;
    public final String mOriginalLanguage;
    public final String mPosterPath;
    public final String mBackdropPath;
    public final int mPopularityIndex;
    public final int mVoteCount;
    public final Double mVoteAvg;
    public final boolean mAdultType;
    public final String mOverveiw;
    public final String mReleaseDate;
    public final int[] mGenereIDs;

    public MovieInfo(int id,
                     String title,
                     String orgTitle,
                     String orgLng,
                     String posterPath,
                     String backdropPath,
                     int popularityIndex,
                     int voteCount,
                     Double voteAvg,
                     boolean adultType,
                     String overview,
                     String releaseDate,
                     int[] genereIDs)
    {
        this.mID = id;
        this.mTitle = title;
        this.mOriginalTitle = orgTitle;
        this.mOriginalLanguage = orgLng;
        this.mPosterPath = posterPath;
        this.mBackdropPath = backdropPath;
        this.mPopularityIndex = popularityIndex;
        this.mVoteCount = voteCount;
        this.mVoteAvg = voteAvg;
        this.mAdultType = adultType;
        this.mOverveiw = overview;
        this.mReleaseDate = releaseDate;
        this.mGenereIDs = genereIDs;
    }
}
