package com.qiniu.nigel.cdn;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class Refresh {

    private Client client;
    private String urlQuotaDay;
    private int urlSurplusDay;
    private int dirQuotaDay;
    private int dirSurplusDay;
    private int quotaDay;
    private int surplusDay;

    public Refresh() {
        client = new Client();
    }

    public String getUrlQuotaDay() {
        return urlQuotaDay;
    }

    public int getUrlSurplusDay() {
        return urlSurplusDay;
    }

    public int getDirQuotaDay() {
        return dirQuotaDay;
    }

    public int getDirSurplusDay() {
        return dirSurplusDay;
    }

    public int getQuotaDay() {
        return quotaDay;
    }

    public int getSurplusDay() {
        return surplusDay;
    }

    public int queryUrlSurplus(Auth auth) throws QiniuException {
        String url = "http://fusion.qiniuapi.com/refresh/user/surplus";
        String authorization = "QBox " + auth.signRequest(url, null, Client.FormMime);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Response response = client.get(url, headers);
        JsonObject jsonObject = new JsonParser().parse(response.bodyString()).getAsJsonObject();
        System.out.println(jsonObject.toString());
        int code = jsonObject.get("code").getAsInt();
        String error = jsonObject.get("error").getAsString();
        if (code != 200 || !"success".equals(error)) throw new QiniuException(response);
        else urlSurplusDay = jsonObject.get("").getAsInt();
        return urlSurplusDay;
    }
}
