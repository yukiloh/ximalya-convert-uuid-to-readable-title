package com.example.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.sql.*;

//@SpringBootTest
class ReadXimalayaVipApplicationTests {

    //需要手动设置
    private static String dbPath = "C:\\Users\\yukil\\Downloads\\ximalaya.db";  //ximalaya.db的位置
    private static String m4aPath = "C:\\Users\\yukil\\Downloads\\ximalaya";    //m4a的文件夹位置
    private static String saveTo = "C:\\Users\\yukil\\Downloads";               //所需要保存的文件夹位置

    private static Connection connection;

    //初始化
    @AfterAll
    static void init(){
        System.out.println("init...");
        String url = "jdbc:sqlite:"+dbPath;   //定义连接数据库的url(url:访问数据库的URL路径),test为数据库名称
        try {
            Class.forName("org.sqlite.JDBC");//加载数据库驱动
            connection = DriverManager.getConnection(url);    //获取数据库连接
            System.out.println("数据库连接成功！\n");//数据库连接成功输出提示
        }
        //捕获异常信息
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("数据库连接失败！"+e.getMessage());
        }
    }

    //扫尾
    @BeforeAll
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

    @Test
    void contextLoads() {

        //遍历文件夹
        File file = new File(m4aPath);

        for (File f : folder) {
            //读取单个m4a文件的名称
            String absolutePath = f.getName()
            //从.db中查找声音信息

            //重命名

            //移动至专辑对应的文件夹
        }
        System.out.println("all finished");

    }



    public void selectAll() {   //选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
        String sql="Select * from newtrack";//将从表中查询到的的所有信息存入sql
        try {
            Statement stmt = connection.createStatement();//得到Statement实例
            ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集

//            String c1 = rs.getCursorName();
            System.out.println(rs.getArray(1).toString());
//            String c2 = rs.getString(2);
//            String c3 = rs.getString(3);
//            String c4 = rs.getString(4);
//            String c5 = rs.getString(5);
//            System.out.println(c1+c2+c3+c4+c5);


            /*while (rs.next()) {
                //输出获得记录中的"name","sex","age"字段的值
                System.out.println(rs.getString("name") + "\t" + rs.getString("sex")+ "\t" +rs.getInt("age"));
            }*/
        }
        catch (SQLException e) {
            System.out.println("查询数据时出错！"+e.getMessage());
        }
    }



    //草稿================================================

    @Test
    void contextLoad1() {
        String filename = "";
        File file = new File(filename);

    }

}
