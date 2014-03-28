package com.thu.stlgm.api;

import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.thu.stlgm.bean.Album;
import com.thu.stlgm.bean.Photo;
import com.thu.stlgm.util.ConstantUtil;
import com.thu.stlgm.util.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SemonCat on 2014/3/25.
 */
public class FacebookAlbumUtils {

    private static final String TAG = FacebookAlbumUtils.class.getName();

    private static final List<Album> mCacheAlbumList = new ArrayList<Album>();

    private static DefaultHttpClient mHttpClient = new DefaultHttpClient();

    private static ScheduledExecutorService mScheduledThreadPool = Executors.newScheduledThreadPool(5);

    public static List<Album> getAllGroupAlbum(){

        while (mCacheAlbumList==null || mCacheAlbumList.isEmpty()){

        }

        return mCacheAlbumList;

        //return getAllGroupAlbum(false);
    }

    public static List<Album> getAllGroupAlbum(boolean ignoreCache){


        if (!ignoreCache && mCacheAlbumList!=null && (!mCacheAlbumList.isEmpty())) return new ArrayList<Album>(mCacheAlbumList);

        final List<Album> albumList = new ArrayList<Album>();
        Bundle parameters = new Bundle();
        parameters.putString("access_token", ConstantUtil.FacebookAccessToken);

        new Request(
                null,
                "/"+ ConstantUtil.GroupId+"/albums",
                parameters,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        if (response.getError()==null){

                            GraphObject mGraphObject = response.getGraphObject();


                            List<GraphObject> mAlbumGraphObjectList =
                                    mGraphObject.getPropertyAsList("data", GraphObject.class);

                            List<Album> mAlbumList = Album.toAlbum(mAlbumGraphObjectList);

                            try{
                                getAllAlbum(mAlbumList);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            albumList.clear();
                            albumList.addAll(mAlbumList);


                        }else{
                            Log.d(TAG,"Error:"+response.getError().toString());
                        }
                    }
                }
        ).executeAndWait();

        mCacheAlbumList.clear();
        mCacheAlbumList.addAll(albumList);

        return albumList;
    }

    public static List<Photo> getPhotoListByAlbumId(long albumId){
        List<Album> mAlbumList = getAllGroupAlbum();

        for (Album mAlbum : mAlbumList){
            if (mAlbum.getId()==albumId){
                return mAlbum.getPhotos();
            }
        }

        return null;
    }

    public static void LoadAlbumCache(){

        mScheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                getAllGroupAlbum(true);


            }
        }, 0, 5, TimeUnit.SECONDS);

    }

    public static void cleanAlbumCache(){
        mCacheAlbumList.clear();
    }

    private static void getAllAlbum(final List<Album> mAlbumList) throws Exception{
        if (!CheckLogin()){
            LoginFacebook();
            Log.d(TAG,"登入");
        }


        String fqlQuery;
        Pattern mFacebookIdPattern = Pattern.compile("(fbid=([0-9]+))");
        for (final Album mAlbum : mAlbumList){

            String AlbumId = String.valueOf(mAlbum.getId());

            HttpPost facebookAlbum = new HttpPost("http://www.facebook.com/"+AlbumId);
            facebookAlbum.addHeader("Referer","http://www.facebook.com/");
            facebookAlbum.addHeader("Host","www.facebook.com");
            facebookAlbum.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            facebookAlbum.addHeader("Accept-Language","zh-tw,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            facebookAlbum.addHeader("Connection","keep-alive");
            HttpResponse response = mHttpClient.execute(facebookAlbum);
            HttpEntity entity = response.getEntity();


            String albumContent = EntityUtils.toString(entity);

            entity.consumeContent();

            Document doc = Jsoup.parse(albumContent);

            Matcher m = mFacebookIdPattern.matcher(doc.html());

            List<String> mIds = new ArrayList<String>();
            while( m.find() )
            {
                mIds.add("object_id = "+m.group(2));
            }

            String[] mIdArrays = mIds.toArray(new String[mIds.size()]);

            String FQLIdString = StringUtils.join(" OR ", mIdArrays);


            fqlQuery = "SELECT object_id,comment_info,caption,like_info, src_big FROM photo WHERE "+FQLIdString;


            Bundle params = new Bundle();
            params.putString("access_token", ConstantUtil.FacebookAccessToken);
            params.putString("q", fqlQuery);
            Session session = Session.getActiveSession();
            Request request = new Request(session,
                    "/fql",
                    params,
                    HttpMethod.GET,
                    new Request.Callback(){
                        public void onCompleted(Response response) {

                            List<Photo> mPhotos = Photo.toPhoto(response.getGraphObject());

                            mAlbum.setPhotos(mPhotos);

                        }
                    });
            Request.executeAndWait(request);

        }
    }

    /**
     * 檢查是否需要登入Facebook
     * @return true 表示 已登入
     * @throws Exception
     */
    private static boolean CheckLogin() throws Exception{
        Pattern redirPtn = Pattern.compile("<(?:META|meta|Meta) (?:HTTP-EQUIV|http-equiv)=\"refresh\".*URL=(.*)\">");


        mHttpClient.getParams().setParameter(
                CoreProtocolPNames.USER_AGENT,
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:28.0) Gecko/20100101 Firefox/28.0"
        );

        String FacebookURL = "https://www.facebook.com/groups/415585308579029/";
        HttpGet httpget = new HttpGet(FacebookURL);
        httpget.addHeader("Referer","http://www.facebook.com/");
        httpget.addHeader("Host","www.facebook.com");
        httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpget.addHeader("Accept-Language","zh-tw,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        httpget.addHeader("Connection","keep-alive");


        HttpResponse response = mHttpClient.execute(httpget);
        HttpEntity entity = response.getEntity();


        String Result = EntityUtils.toString(entity);
        if (entity != null) {
            entity.consumeContent();
        }

        Matcher mth = redirPtn.matcher(Result);
        while(mth.find()){
            String rurl = mth.group(1);

            if (rurl.contains("/login.php")){
                return false;
            }

        }

        return true;
    }

    private static void LoginFacebook() throws Exception{
        HttpPost httpost = new HttpPost("http://www.facebook.com/login.php");

        List <NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("email", "csthutw@gmail.com"));
        nvps.add(new BasicNameValuePair("pass", "a1a2j3c4"));
        nvps.add(new BasicNameValuePair("locale", "zh_TW"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        mHttpClient.execute(httpost).getEntity().consumeContent();
    }
}