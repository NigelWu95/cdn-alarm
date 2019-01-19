package com.qiniu.nigel;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.IOException;

public class QiniuCdn {

    public static void main(String[] args) throws IOException {

        Config config = new Config("qiniu.properties");
        String accessKey = config.getParamValue("ak");
        String secretKey = config.getParamValue("sk");
        Auth auth = Auth.create(accessKey, secretKey);
        QiniuCdn qiniuCdn = new QiniuCdn();
        qiniuCdn.queryCdnAmounts(auth);
    }

    public void queryCdnAmounts(Auth auth) {

        String url = "http://fusion.qiniuapi.com/refresh/user/surplus";
        String authorization = "QBox " + auth.signRequest(url, null, Client.FormMime);
        StringMap headers = new StringMap().put("Authorization", authorization);
        System.out.println(headers.formString());
        Client client = new Client();
        Response response = null;

        try {
            response = client.get(url, headers);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
