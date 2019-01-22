package com.qiniu.nigel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiniuCdn {

    private Client client;

    public QiniuCdn() {
        client = new Client();
    }

    public String queryCdnAmounts(Auth auth) throws QiniuException {
        String url = "http://fusion.qiniuapi.com/refresh/user/surplus";
        String authorization = "QBox " + auth.signRequest(url, null, Client.FormMime);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Response response = client.get(url, headers);
        JsonObject jsonObject = new JsonParser().parse(response.bodyString()).getAsJsonObject();
        System.out.println(jsonObject.toString());
//        return jsonObject.get("").getAsInt();
        return "";
    }
}
