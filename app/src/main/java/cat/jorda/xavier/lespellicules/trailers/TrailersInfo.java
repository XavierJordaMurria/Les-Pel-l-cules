package cat.jorda.xavier.lespellicules.trailers;

/**
 * Created by xj1 on 15/01/2017.
 */

/**
 * Trailes information objec regarding to an specific movie
 * ex:
 * "id": "571cdc48c3a3684e620018b8",
 * "iso_639_1": "en",
 * "iso_3166_1": "US",
 * "key": "i-80SGWfEjM",
 * "name": "Official Teaser Trailer",
 * "site": "YouTube",
 * "size": 1080,
 * "type": "Trailer"
 */
public class TrailersInfo
{
    public String mId;
    public String mKey;
    public String mName;
    public String mSite;
    public String mType;

    public TrailersInfo(String id,
                        String key,
                        String name,
                        String site,
                        String type)
    {
        mId = id;
        mKey    = key;
        mName   = name;
        mSite   = site;
        mType   = type;
    }
}
