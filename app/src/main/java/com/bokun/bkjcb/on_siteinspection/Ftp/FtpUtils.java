package com.bokun.bkjcb.on_siteinspection.Ftp;

import android.util.Log;

import com.bokun.bkjcb.on_siteinspection.Utils.Constants;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JiaHuaiQing on 2017/3/22.
 * Copyright (c) 2015 sinping.com
 */
public class FtpUtils {
    private String remoteIP;
    private int remotePort;
    private String remoteUser;
    private String remotePasswrod;
    private static FTPClient ftpClient;

    public FtpUtils() {
        this.remoteIP = Constants.FTP_HOST_DEFAULT;
        this.remotePort = Constants.FTP_HOST_PORT;
        this.remoteUser = Constants.FTP_USER_DEFAULT;
        this.remotePasswrod = Constants.FTP_PASSWORD_DEFAULT;
        this.ftpClient = new FTPClient();
    }

    // -------------------------------------------------------文件上传方法------------------------------------------------

    /**
     * 上传单个文件.
     *
     * @param //localFile 本地文件
     * @param remotePath  FTP目录
     * @param listener    监听器
     * @throws IOException
     */
    public void uploadSingleFile(File singleFile, String remotePath,
                                 UploadProgressListener listener) throws IOException {

        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;
        flag = uploadingSingle(singleFile, listener);
        if (flag) {
            listener.onUploadProgress(Constants.FTP_UPLOAD_SUCCESS, 0, 0,
                    singleFile);
        } else {
            listener.onUploadProgress(Constants.FTP_UPLOAD_FAIL, 0, 0,
                    singleFile);
        }

        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }

    /**
     * 上传多个文件.
     *
     * @param //localFile 本地文件
     * @param remotePath  FTP目录
     * @param listener    监听器
     * @throws IOException
     */
    public boolean uploadMultiFile(HashMap<Integer, ArrayList<String>> pathMap, String remotePath,
                                   UploadProgressListener listener) throws IOException {
        // 上传之前初始化
        boolean isConnection = this.uploadBeforeOperate(remotePath, listener);
        if (!isConnection) {
            return false;
        }
        String childPath;
        boolean flag;
        long currentSize = 0;
        long size = 0;
        Iterator iter = pathMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            ArrayList<String> val = (ArrayList<String>) entry.getValue();
            size += val.size();
            childPath = "" + key;
            LogUtil.logI(remotePath);
            // FTP下创建文件夹
            ftpClient.makeDirectory(childPath);
            // 改变FTP目录
            ftpClient.changeWorkingDirectory(childPath);
            for (String path : val) {
                File singleFile = new File(path);
                if (!singleFile.exists()) {
                    continue;
                }
                try {
                    int reUpload = deleteSingleFile(null, singleFile, new DeleteFileProgressListener() {
                        @Override
                        public void onDeleteProgress(String currentStep) {
                            LogUtil.logI("删除文件" + currentStep);
                        }
                    });
                    if (reUpload == 1) {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onUploadProgress(Constants.FTP_UPLOAD_FAIL, 0, 0,
                            singleFile);
                    return false;
                }
                flag = uploadingSingle(singleFile, listener);
                if (flag) {
                    listener.onUploadProgress(Constants.FTP_UPLOAD_SUCCESS, ++currentSize, size,
                            null);
                    //ftpClient.changeToParentDirectory();
                } else {
                    listener.onUploadProgress(Constants.FTP_UPLOAD_FAIL, 0, 0,
                            singleFile);
                    this.uploadAfterOperate(listener);
                    return false;
                }
            }
            //回到上一级目录
            ftpClient.changeToParentDirectory();

        }


        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
        return true;

    }

    /**
     * 上传单个文件.
     *
     * @param localFile 本地文件
     * @return true上传成功, false上传失败
     * @throws IOException
     */
    private boolean uploadingSingle(File localFile,
                                    UploadProgressListener listener) throws IOException {
        boolean flag = true;
        // 不带进度的方式
        // // 创建输入流
        // InputStream inputStream = new FileInputStream(localFile);
        // // 上传单个文件
        // flag = ftpClient.storeFile(localFile.getName(), inputStream);
        // // 关闭文件流
        // inputStream.close();

        // 带有进度的方式
        BufferedInputStream buffIn = new BufferedInputStream(
                new FileInputStream(localFile));
        ProgressInputStream progressInput = new ProgressInputStream(buffIn,
                listener, localFile);
        flag = ftpClient.storeFile(localFile.getName(), progressInput);
        buffIn.close();

        return flag;
    }

    /**
     * 上传文件之前初始化相关参数
     *
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    private boolean uploadBeforeOperate(String remotePath,
                                        UploadProgressListener listener) throws IOException {

        // 打开FTP服务
        try {
            this.openConnect();
            listener.onUploadProgress(Constants.FTP_CONNECT_SUCCESSS, 0,
                    0, null);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onUploadProgress(Constants.FTP_CONNECT_FAIL, 0, 0, null);
            return false;
        }

        // 设置模式
        ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
        // 上传单个文件
        // FTP下创建文件夹
        ftpClient.makeDirectory(remotePath);
        // 改变FTP目录
        ftpClient.changeWorkingDirectory(remotePath);
        return true;
    }

    /**
     * 上传完成之后关闭连接
     *
     * @param listener
     * @throws IOException
     */
    private void uploadAfterOperate(UploadProgressListener listener)
            throws IOException {
        this.closeConnect();
        listener.onUploadProgress(Constants.FTP_DISCONNECT_SUCCESS, 0, 0, null);
    }

    // -------------------------------------------------------文件下载方法------------------------------------------------

    /**
     * 下载单个文件，可实现断点下载.
     *
     * @param serverPath Ftp目录及文件路径
     * @param localPath  本地目录
     * @param fileName   下载之后的文件名称
     * @param listener   监听器
     * @throws IOException
     */
    public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener)
            throws Exception {

        // 打开FTP服务
        try {
            this.openConnect();
            listener.onDownLoadProgress(Constants.FTP_CONNECT_SUCCESSS, 0, null);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onDownLoadProgress(Constants.FTP_CONNECT_FAIL, 0, null);
            return;
        }

        // 先判断服务器文件是否存在
        FTPFile[] files = ftpClient.listFiles(serverPath);
        if (files.length == 0) {
            listener.onDownLoadProgress(Constants.FTP_FILE_NOTEXISTS, 0, null);
            return;
        }

        //创建本地文件夹
        File mkFile = new File(localPath);
        if (!mkFile.exists()) {
            mkFile.mkdirs();
        }

        localPath = localPath + fileName;
        // 接着判断下载的文件是否能断点下载
        long serverSize = files[0].getSize(); // 获取远程文件的长度
        File localFile = new File(localPath);
        long localSize = 0;
        if (localFile.exists()) {
            localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
            if (localSize >= serverSize) {
                File file = new File(localPath);
                file.delete();
            }
        }

        // 进度
        long step = serverSize / 100;
        long process = 0;
        long currentSize = 0;
        // 开始准备下载文件
        OutputStream out = new FileOutputStream(localFile, true);
        ftpClient.setRestartOffset(localSize);
        InputStream input = ftpClient.retrieveFileStream(serverPath);
        byte[] b = new byte[1024];
        int length = 0;
        while ((length = input.read(b)) != -1) {
            out.write(b, 0, length);
            currentSize = currentSize + length;
            if (currentSize / step != process) {
                process = currentSize / step;
                if (process % 5 == 0) {  //每隔%5的进度返回一次
                    listener.onDownLoadProgress(Constants.FTP_DOWN_LOADING, process, null);
                }
            }
        }
        out.flush();
        out.close();
        input.close();

        // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
        if (ftpClient.completePendingCommand()) {
            listener.onDownLoadProgress(Constants.FTP_DOWN_SUCCESS, 0, new File(localPath));
        } else {
            listener.onDownLoadProgress(Constants.FTP_DOWN_FAIL, 0, null);
        }

        // 下载完成之后关闭连接
        this.closeConnect();
        listener.onDownLoadProgress(Constants.FTP_DISCONNECT_SUCCESS, 0, null);

        return;
    }

    // -------------------------------------------------------文件删除方法------------------------------------------------

    /**
     * 删除Ftp下的文件.
     *
     * @param serverPath Ftp目录及文件路径
     * @param listener   监听器
     * @throws IOException
     */
    public int deleteSingleFile(String serverPath, File f, DeleteFileProgressListener listener)
            throws Exception {

        // 打开FTP服务
      /*  try {
            this.openConnect();
            listener.onDeleteProgress(Constants.FTP_CONNECT_SUCCESSS);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onDeleteProgress(Constants.FTP_CONNECT_FAIL);
            return;
        }
*/
        // 先判断服务器文件是否存在
        FTPFile[] files = ftpClient.listFiles();
        if (files.length == 0) {
            listener.onDeleteProgress(Constants.FTP_FILE_NOTEXISTS);
            return 0;
        }

        //进行删除操作
        FTPFile ftpFile = null;
        boolean flag = true;
        for (FTPFile file : files) {
            LogUtil.logI("ftp文件name" + file.getName());
            if (file.getName().equals(f.getName())) {
                ftpFile = file;
                break;
            }
        }
        LogUtil.logI(serverPath + "/" + f.getName());
        if (ftpFile != null && ftpFile.getSize() == getFileSize(f)) {
            return 1;
        }
        flag = ftpClient.deleteFile(serverPath + "/" + f.getName());
        if (flag) {
            listener.onDeleteProgress(Constants.FTP_DELETEFILE_SUCCESS);
        } else {
            listener.onDeleteProgress(Constants.FTP_DELETEFILE_FAIL);
        }

        // 删除完成之后关闭连接
      /*  this.closeConnect();
        listener.onDeleteProgress(Constants.FTP_DISCONNECT_SUCCESS);*/

        return 0;
    }

    // -------------------------------------------------------打开关闭连接------------------------------------------------

    /**
     * 打开FTP服务.
     *
     * @throws IOException
     */
    public void openConnect() throws IOException {
        // 中文转码
        ftpClient.setControlEncoding("UTF-8");
        int reply; // 服务器响应值
        // 连接至服务器
        ftpClient.connect(remoteIP, remotePort);
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        }
        // 登录到服务器
        ftpClient.login(remoteUser, remotePasswrod);
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        } else {
            // 获取登录信息
            FTPClientConfig config = new FTPClientConfig(ftpClient
                    .getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            ftpClient.configure(config);
            // 使用被动模式设为默认
            ftpClient.enterLocalPassiveMode();
            // 二进制文件支持
            ftpClient
                    .setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        }
    }

    /**
     * 关闭FTP服务.
     *
     * @throws IOException
     */
    public void closeConnect() throws IOException {
        if (ftpClient != null) {
            // 退出FTP
            ftpClient.logout();
            // 断开连接
            ftpClient.disconnect();
        }
    }

    /**
     * 获取到ftp指定文件夹下的所有图片路径,最多五个
     *
     * @param remotePath ftp服务器上的图片文件夹路径
     * @param listener   获取所有图片路径监听
     */
    public void getPicFromFtpServer(String remotePath, GetAllPicPathProgressListener listener) throws IOException {
        // 打开FTP服务
         /*  try {
            this.openConnect();
            listener.onGetAllPicProgress(Constants.FTP_CONNECT_SUCCESSS, null);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onGetAllPicProgress(Constants.FTP_CONNECT_FAIL, null);
            return;
        }

        List<String> filePathLists = null;
        try {
            // 转到指定下载目录
            filePathLists = new ArrayList<String>();
            // 列出该目录下所有文件
            FTPFile[] files = ftpClient.listFiles(remotePath);
            if (files.length == 0) {
                listener.onGetAllPicProgress(Constants.FTP_FILE_NOTEXISTS, null);
                return;
            }
            // 遍历所有文件，找到指定的文件
            for (FTPFile file : files) {
                String filePath = Const.Image_URL + remotePath + "/" + file.getName();
                filePathLists.add(filePath);
            }
        } catch (IOException e) {
            listener.onGetAllPicProgress(Constants.FTP_GETALLPICPATH_FAIL, null);
            e.printStackTrace();
        }

        // 获取图片路径完成之后关闭连接
        this.closeConnect();
        listener.onGetAllPicProgress(Constants.FTP_GETALLPICPATH_SUCCESS, filePathLists);
        return;*/

    }

    /*
     * 文件删除监听
     */
    public interface GetAllPicPathProgressListener {
        public void onGetAllPicProgress(String currentStep, List<String> photoPathLists);
    }

    // ---------------------------------------------------上传、下载、删除监听---------------------------------------------

    /*
     * 上传进度监听
     */
    public interface UploadProgressListener {
        public void onUploadProgress(String currentStep, long uploadSize, long size, File file);
    }

    /*
     * 下载进度监听
     */
    public interface DownLoadProgressListener {
        public void onDownLoadProgress(String currentStep, long downProcess, File file);
    }

    /*
     * 文件删除监听
     */
    public interface DeleteFileProgressListener {
        public void onDeleteProgress(String currentStep);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return s
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
}
