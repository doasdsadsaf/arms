package com.my.user.utils;

import com.my.user.config.RshConfigBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.SocketException;

/**
 * @创建人 dw
 * @创建时间 2020/5/26
 * @描述
 */
@Component
public class FtpUtil {

    @Autowired
    private RshConfigBean rshConfigBean;


    /**
     *
     * @param filePath 存放文件路径
     * @param filename 文件名
     * @param input 文件流
     * @return
     */
    public String uploadFile(String filePath, String filename, InputStream input){
        Boolean flag = uploadFile(rshConfigBean.getHost(), rshConfigBean.getPort(), rshConfigBean.getUsername(), rshConfigBean.getPassword(),
                rshConfigBean.getBasePath(), filePath, filename, input);
        if(flag){
            return filePath+File.separator+filename;
        }else{
            return null;
        }
    }

    /**
     * 删除
     */
    public boolean deleteFile(String remotePath,String fileName){
        return deleteFile(rshConfigBean.host, rshConfigBean.port, rshConfigBean.username, rshConfigBean.password,
                "files"+remotePath,fileName);
    }

    /**
     * 下载
     */
    public boolean downloadFile(String fileName, String localPath){
        return downloadFile(rshConfigBean.host, rshConfigBean.port, rshConfigBean.username, rshConfigBean.password,
                rshConfigBean.basePath,fileName,localPath);
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param host     FTP服务器hostname
     * @param port     FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param basePath FTP服务器基础目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    private Boolean uploadFile(String host, int port, String username, String password, String basePath,
                                    String filePath, String filename, InputStream input) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器

            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            boolean login = ftp.login(username, password);// 登录
            ftp.enterLocalPassiveMode();
            reply = ftp.getReplyCode();
            ftp.getReplyString();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir))
                        continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            int replyCode = ftp.getReplyCode();
                            System.out.println("======转移到FTP服务器目录:"+replyCode);
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 上传文件

            if (!ftp.storeFile(filename, input)) {
                int replyCode = ftp.getReplyCode();
                System.out.println("======转移到FTP服务器目录:"+replyCode);
                return result;
            }
            input.close();
            ftp.logout();
            result = true;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
                                       String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 获取文件后缀
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot);
            }
        }
        return filename;
    }

    /**
     * Description: 从FTP删除文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要删除的文件名
     * @return
     */
    public static boolean deleteFile(String host, int port, String username, String password, String remotePath,
                                     String fileName) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            result = ftp.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    public Boolean createFolder(String filePath){
        return createFolder(rshConfigBean.getHost(), rshConfigBean.getPort(), rshConfigBean.getUsername(), rshConfigBean.getPassword(),
                rshConfigBean.getBasePath(), filePath);
    }

    public Boolean rename(String filePath,String toFilePath){
        return rename(rshConfigBean.getHost(), rshConfigBean.getPort(), rshConfigBean.getUsername(), rshConfigBean.getPassword(),
                rshConfigBean.getBasePath(), filePath,toFilePath);
    }

    private Boolean rename(String host, Integer port, String username, String password, String basePath, String filePath, String toFilePath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        int reply;
        try {
            ftp.connect(host, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            boolean login = ftp.login(username, password);// 登录
            ftp.enterLocalPassiveMode();
            reply = ftp.getReplyCode();
            ftp.getReplyString();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.rename(filePath,toFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 创建文件夹
     */
    private Boolean createFolder(String host, int port, String username, String password, String basePath,
                               String filePath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        int reply;
        try {
            ftp.connect(host, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            boolean login = ftp.login(username, password);// 登录
            ftp.enterLocalPassiveMode();
            reply = ftp.getReplyCode();
            ftp.getReplyString();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir))
                        continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (ftp.makeDirectory(tempPath)) {
                            int replyCode = ftp.getReplyCode();
                            System.out.println("======转移到FTP服务器目录:" + replyCode);
                            result = true;
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    }
