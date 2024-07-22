package com.cloud.clientserver.mysql;

import java.sql.*;

/**
 * @Author: 何立森
 * @Date: 2024/07/22/15:58
 * @Description:
 */
public class MySqlTest {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            //1、加载并注册JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2、创建数据库连接
            String url = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&AllowPublicKeyRetrieval=True";
            String username = "root";
            String password = "123456";
            connection = DriverManager.getConnection(url, username, password);
            //3、创建Statement对象用于执行SQL语句
            statement = connection.createStatement();
            //4、执行SQL查询并处理结果
            String sql = "select * from t_student where id = 1";
            ResultSet resultSet = statement.executeQuery(sql);
            //5、遍历结果集
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                int sex = resultSet.getInt("sex");
                Date createAt = resultSet.getDate("create_at");
                int score = resultSet.getInt("score");
                System.out.println("id：" + id);
                System.out.println("name：" + name);
                System.out.println("age：" + age);
                System.out.println("sex：" + sex);
                System.out.println("createAt：" + createAt);
                System.out.println("score：" + score);
            }
            resultSet.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(statement != null) {
                    statement.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
