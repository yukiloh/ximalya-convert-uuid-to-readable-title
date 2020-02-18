package com.example.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

class RunMe {
    //请执行main()

    //(只有)以下3+1个属性需要根据自己配置进行手动设置
    private static String dbPath = "C:/Users/Ash/Nox_share/ImageShare/ximalaya.db";       //ximalaya.db的位置;注意,分隔符为斜杠/
    private static String m4aPath = "C:\\Users\\Ash\\Nox_share\\ImageShare\\download";    //指定m4a所在的文件夹位置
    private static String saveTo = "C:\\Users\\Ash\\Nox_share\\ImageShare";               //指定需要保存的文件夹位置
    private static Integer deviation = 0;                                                 //文件允许误差,单位:字节;一般不需要设置

    private static Connection connection;
    private static Statement statement;

    @Test
    void main() {
        //遍历文件夹,返回文件名列表
        String[] fileList = getM4AFolder();
        if (fileList != null) {
            //获取单个m4a文件的名称,参考：fed761a164917cac687fb23655cb5b3a.m4a
            for (String filename : fileList) {

                //从.db中查找声音信息
                TrackInfo trackInfo = findTrack(filename);

                //如果查出重复结果则会返回null,跳过本次执行,需要另外人工校对
                if (trackInfo == null) {
                    continue;
                }
                //重命名,并移动至专辑对应的文件夹
                renameAndMove(filename,trackInfo);
            }
        }
        System.out.println("all finished");
    }



    private void renameAndMove(String filename, TrackInfo trackInfo) {
        //检查专辑文件夹是否存在,不存在则创建
        checkFolder(trackInfo);

        File oldFile = new File(m4aPath + "\\" + filename);
        String newName = trackInfo.getTrackID()+" "+trackInfo.getTrackTitle();
        File newFile = new File(saveTo+"\\"+trackInfo.getAlbumTitle()+"\\"+newName+".m4a");

        if (oldFile.renameTo(newFile)) {
            System.out.println("renamed and moved");
        } else {
            System.out.println("error");
        }
    }


    private void checkFolder(TrackInfo trackInfo) {
        File folder = new File(saveTo + "\\" + trackInfo.getAlbumTitle());
        if (!folder.exists()) if (folder.mkdir()) System.out.println("folder "+trackInfo.getAlbumTitle()+" created!");
    }

    private TrackInfo findTrack(String filename) {
        //无法通过文件名的uuid来查询到file,因此进行比对大小查询
        Integer fileSize = getFileSize(filename);

        TrackInfo trackInfo = null;
        int count = 0;  //用于判断是否发现多个结果

        try {
            String sql="select * from newtrack ";
            String where = "where downloadedsize >= " + (fileSize - deviation) + " and downloadedsize <= " + (fileSize +deviation);
            ResultSet rs = statement.executeQuery(sql+where);

            //sqlite的常规用法,第一个指针在null行,next后才是第一行
            while ( rs.next() ) {
                //如果发现有多个结果则打印异常以及文件名,并返回null
                if (count > 0) {
                    System.out.println("!!!!!duplicate file,name: "+filename+",size: "+rs.getInt("downloadedsize"));
                    return null;
                }

                String trackID = redefineTid(rs.getInt("ordernum"));                    //重新定义编号,例如:123改为0123
                String trackTitle = replaceUnallowableChar(rs.getString("tracktitle")); //去除win下非法字符
                String albumTitle = rs.getString("albumtitle");

                trackInfo = new TrackInfo(trackID, trackTitle, albumTitle);
                count++;
            }
        }catch (SQLException e) {
            System.out.println("sql error！"+e.getMessage());
        }
        return trackInfo;
    }

    private Integer getFileSize(String filename) {
        File file = new File(m4aPath + "\\" + filename);
        return (int)file.length();
    }

    private String[] getM4AFolder() {
        File folderFile = new File(m4aPath);
        if (!folderFile.isDirectory() && !folderFile.exists()) {
            System.out.println("not a folder");
        } else if (folderFile.isDirectory()) {
            String[] fileList = folderFile.list();
            if (fileList != null) {
                return fileList;
            } else {
                System.out.println("folder is empty");
            }
        }
        return null;
    }


    //替换非法字符
    private String replaceUnallowableChar(String title) {
        //win10的非法字符:< > / \ | : " * ?
        //其他os或者win10以外的os不在其中
        ArrayList<String> list = new ArrayList<>();
        list.add("<");
        list.add(">");
        list.add("/");
        list.add("\\");
        list.add("|");
        list.add(":");
        list.add("\"");
        list.add("*");
        list.add("?");

        for (String s : list) {
            while (title.contains(s)) {
                title = title.replace(s, "");
            }
        }
        return title;
    }

    //重定义序号,方便根据文件名排序;例如:123更改为0123,12更改为0012
    private String redefineTid(Integer tid) {
        if (tid<1000) {
            int prefixCount = 1;
            if (tid<100){
                prefixCount++;
                if (tid<10){
                    prefixCount++;
                }
            }
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < prefixCount; i++) {
                result.append("0");
            }
            result.append(tid);
            return result.toString();
        } else return tid.toString();
    }


    //初始化
    @BeforeAll
    static void init(){
        System.out.println("init...");
        String url = "jdbc:sqlite:"+dbPath;   //定义连接数据库的url(url:访问数据库的URL路径),test为数据库名称
        try {
            Class.forName("org.sqlite.JDBC");//加载数据库驱动
            connection = DriverManager.getConnection(url);    //获取数据库连接
            System.out.println("数据库连接成功！");//数据库连接成功输出提示
            statement = connection.createStatement();//得到Statement实例
        }
        //捕获异常信息
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("数据库连接失败！"+e.getMessage());
        }
    }

    //扫尾
    @AfterAll
    static void close(){
        System.out.println("closing...");
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //草稿================================================
    @Test
    void contextLoad1() {
        String filename = "fed761a164917cac687fb23655cb5b3a.m4a";
        TrackInfo trackInfo = findTrack(filename);
        renameAndMove(filename,trackInfo);
    }
}
