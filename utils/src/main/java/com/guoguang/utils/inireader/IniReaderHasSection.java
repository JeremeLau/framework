package com.guoguang.utils.inireader;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

/**
 * Author: Created by jereme on 2018/7/16
 * E-main: liuqx@guoguang.com.cn
 */
public class IniReaderHasSection {
    private HashMap<String, Properties> sections = new HashMap<>();
    private transient Properties properties;

    public IniReaderHasSection(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        read(reader);
        reader.close();
    }

    /**
     * 读取asset文件
     * @param context
     * @param iniPath
     */
    public IniReaderHasSection(Context context, String iniPath){
        try{
            InputStream inputStream = context.getResources().getAssets().open(iniPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            read(reader);
            reader.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void read(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }

    private void parseLine(String line) {
        line = line.trim();
        if (line.matches("\\[.*\\]")) {
            String section = line.replaceFirst("\\[(.*)\\]", "$1");
            properties = new Properties();
            sections.put(section, properties);
        } else if (line.matches(".*=.*")) {
            if (properties != null) {
                int i = line.indexOf('=');
                String name = line.substring(0, i);
                String value = line.substring(i + 1);
                properties.setProperty(name, value);
            }
        }
    }

    public String getValue(String section, String name) {
        Properties p = sections.get(section);
        return p == null ? null : p.getProperty(name);
    }

    public String getValue(String section, String name, String defaultValue) {
        String value = getValue(section, name);
        return value == null ? defaultValue : value;
    }
}
