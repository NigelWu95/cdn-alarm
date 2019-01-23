package com.qiniu.nigel.cdn;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class Refresh {

    private Client client;
    private Auth auth;
    private Quota quota;

    public Refresh(Configuration cfg, Auth auth) {
        this.client = cfg == null ? new Client() : new Client(cfg);
        this.auth = auth;
    }

    public Quota getQuota() {
        return quota;
    }

    public Quota queryQuotaAndSurplus() throws QiniuException {
        String url = "http://fusion.qiniuapi.com/refresh/user/surplus";
        String authorization = "QBox " + auth.signRequest(url, null, Client.FormMime);
        StringMap headers = new StringMap().put("Authorization", authorization);
        Response response = client.get(url, headers);
        JsonObject jsonObject = new JsonParser().parse(response.bodyString()).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        String error = jsonObject.get("error").getAsString();
        if (code != 200 || !"success".equals(error)) {
            throw new QiniuException(response);
        } else {
            quota = new Gson().fromJson(jsonObject, Quota.class);
            response.close();
            return quota;
        }
    }
}
