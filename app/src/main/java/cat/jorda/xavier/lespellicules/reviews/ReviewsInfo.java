package cat.jorda.xavier.lespellicules.reviews;

/**
 * Created by xj1 on 15/01/2017.
 */


/**
 * Review information related to a particular movie.
 *
 * Ex:
 * "id": "579cfaac9251411b36008316",
 * "author": "Review author",
 * "content": "review Content"
 * "url": "https://www.themoviedb.org/review/579cfaac9251411b36008316"
 */
public class ReviewsInfo
{
    public String mId;
    public String mAuthor;
    public String mContent;
    public String mURL;

    public ReviewsInfo(String id, String author, String content, String url)
    {
        mId = id;
        mAuthor = author;
        mContent    = content;
        mURL    = url;
    }
}
