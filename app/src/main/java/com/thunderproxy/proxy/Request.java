package com.thunderproxy.proxy;

import android.net.Uri;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 6a209 on 16/1/28.
 *
 *
 */
public class Request {

    private static final String CRLF = "\r\n";
    private static final String CONNECT_OK = "HTTP/1.0 200 Connection Established"
            + CRLF + "Proxy-agent: ThunderProxy" + CRLF + CRLF;

    private String mUrl;
    private METHOD mMethod;
    private Map<String, String> mHeader;
    private String mHttpVersion;
    private Map<String, String> mParams;
    private String mQueryString;
    private String mBody;
    private String mHost;
    private String mPath;

    private SocketChannel mSocketChannel;


    public enum METHOD {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE,
        CONNECT,
        PATCH
    }

    public void setMethod(String method){
        mMethod = parseMethod(method);
    }

    public METHOD getMethod(){
        return mMethod;
    }

    METHOD parseMethod(String method){
        for(METHOD m : METHOD.values()){
            if(m.toString().equalsIgnoreCase(method)){
                return m;
            }
        }
        return null;
    }

    public void setUrl(String url){
        mUrl = url;
        Uri uri = Uri.parse(url);
        mHost = uri.getHost();
        mPath = uri.getPath();
    }

    public String getUrl(){
        return mUrl;
    }

    public String getHost(){
        return mHost;
    }

    public String getPath(){
        return mPath;
    }

    public void setHeader(Map<String, String> header){
        mHeader = header;
    }

    public void addHeader(String key, String value){
        if(null == mHeader){
            mHeader = new HashMap<>();
        }
        mHeader.put(key, value);
    }

    public Map<String, String> getHeader(){
        return mHeader;
    }

    public void setHttpVersion(String ver){
        mHttpVersion = ver;
    }

    public String getHttpVersion(){
        return mHttpVersion;
    }

    public void setQueryString(String queryString){
        mQueryString = queryString;
    }

    public String getQueryString(){
        return mQueryString;
    }

    public void setBody(String body){
        mBody = body;
    }

    public String getBody(){
        return mBody;
    }

    public void setSocketChannel(SocketChannel channel){
        mSocketChannel = channel;
    }

    public SocketChannel getSocketChannel(){
        return mSocketChannel;
    }

    public ByteBuffer factoryRequest(){

        if(mMethod == METHOD.CONNECT){
            ByteBuffer byteBuffer = ByteBuffer.wrap(CONNECT_OK.getBytes());
            return byteBuffer;
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append(mMethod.toString() + " ");
            sb.append("/" + " ");
            sb.append(mHttpVersion);
            sb.append(CRLF);
            for(String key : mHeader.keySet()){
                sb.append(key).append(": ").append(mHeader.get(key)).append(CRLF);
            }
            sb.append(CRLF);
            return ByteBuffer.wrap(sb.toString().getBytes());
        }
    }
}
