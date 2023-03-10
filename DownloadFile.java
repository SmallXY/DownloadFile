/*
 *
 *  * Copyright (c) 2022. 最终解释权 制作者： 小玄易(葛宝檀)
 *
 */

package net.lanternstudio.toolsapi.FileManager;


import net.lanternstudio.toolsapi.Console.CMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFile {

    /**
     *
     * @param DownLoadUrl 下载链接
     * @param FilePath 带着文件名字的
     * @param FileNameNoSuffix 没有后缀的文件名字
     * @throws Exception
     */
    public void start(String DownLoadUrl, File FilePath, String FileNameNoSuffix) throws Exception {
        //判断文件是否存在，不存在则创建文件
        CMessage.send("§a " + FileNameNoSuffix + "需要同步");
        if (!FilePath.exists()) {
            if (FilePath.getParentFile().mkdir()) {
                CMessage.send("§a " + FilePath.getParentFile() + "文件夹不在创建完毕");
            }
            if (FilePath.createNewFile()) {
                CMessage.send("§a 创建" + FileNameNoSuffix + "文件");
            }
        }
        URL url = new URL(DownLoadUrl);
        //使用http打开这个URL 以及设置连接的时间与超时时间
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK)
            throw new Exception("[Lantern] 文件读取失败 检查阿里云桶子");
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(FilePath));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        CMessage.send("§a " + FileNameNoSuffix + "同步完成");
        try {
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}