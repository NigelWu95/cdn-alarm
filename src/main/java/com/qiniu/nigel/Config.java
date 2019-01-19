package com.qiniu.nigel;

import java.io.*;
import java.util.Properties;

public class Config {

    private Properties properties;

    public Config(String resourceName) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(resourceName);
            properties = new Properties();
            properties.load(new InputStreamReader(new BufferedInputStream(inputStream), "utf-8"));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    inputStream = null;
                }
            }
        }
    }

    public String getParamValue(String key) throws IOException {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        } else {
            throw new IOException("not set " + key + " param.");
        }
    }
}
