package dao;
import app.FileMeta;
import util.DBUtil;
import util.PinYin4jUtil;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class fileOperator {
    public static void insert(FileMeta localMeta) {
        //1.获取数据库连接
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            try {
                connection = DBUtil.getConnection();
                String sql = "insert into file_meta" +
                        "(name, path, size, last_modified, pinying, pinying_first, is_Directory)" +
                        "values (?,?,?,?,?,?,?)";
                //2.获取操作命令对象
                statement = connection.prepareStatement(sql);
                //填充占位符
                statement.setString(1, localMeta.getName());
                statement.setString(2, localMeta.getPath());
                statement.setLong(3, localMeta.getSize());
                statement.setTimestamp(4, new Timestamp(localMeta.getLast_modified()));
                String pinying = null;
                String pinying_first = null;
                System.out.println("insert"+localMeta.getName()+","+localMeta.getPath());
                //包含中文字符时，需要保存全拼和拼音首字符
                if (PinYin4jUtil.containsChinese(localMeta.getName())) {
                    String[] pinyins = PinYin4jUtil.get(localMeta.getName());
                    pinying = pinyins[0];
                    pinying_first = pinyins[1];
                }
                statement.setString(5, pinying);
                statement.setString(6, pinying_first);
                statement.setBoolean(7, localMeta.isIs_Directory());
                //3.执行sql语句
                statement.executeUpdate();
            } finally {
                DBUtil.close(connection,statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    * 数据库查询操作
    * */
    public static List<FileMeta> query(String dirPath) {
        //获取数据库连接
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FileMeta> metas = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select name,path,size,last_modified,is_Directory" +
                    " from file_meta where path=?";
            System.out.println(sql);
            statement = connection.prepareStatement(sql);
            statement.setString(1, dirPath);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                long size = resultSet.getLong("size");
                long last_modified = resultSet.getLong("last_modified");
                boolean is_Directory = resultSet.getBoolean("is_Directory");
                FileMeta meta = new FileMeta(name, path, size, last_modified, is_Directory);
                System.out.println("query:"+meta.getName()+","+meta.getPath());
                metas.add(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return metas;
        //System.out.println();
    }

    /*
    在数据库中的查询操作
    public static void main(String[] args) {
        System.out.println(query("D:\\ccc"));
    }*/
//数据库有，本地没有
    //如果meta是文件夹，还要删除子文件/子文件夹
    public static void delete(FileMeta meta) {
        //获取数据库连接
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //获取连接
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            String sql = "delete from file_meta where name=? and path=? and is_Directory=?";
            //通过connection获取操作命令对象
            statement = connection.prepareStatement(sql);
            //填充占位符
            statement.setString(1, meta.getName());
            statement.setString(2, meta.getPath());
            statement.setBoolean(3, meta.isIs_Directory());

            //执行sql语句
            statement.executeUpdate();
            //删除子文件/子文件夹
            if (meta.isIs_Directory()) {
                sql = "delete from file_meta where path=? or path like ?";
                statement = connection.prepareStatement(sql);
                //要删除的类型
                //meta path=d:/ name =abc;
                //name=hello path=d:/abc
                //name =world path:d:/abc/.....都是需要删除
                String path=meta.getPath()+File.separator+meta.getName();
                statement.setString(1, path);
                statement.setString(2, path+File.separator + "%");
                System.out.println("delete ："+meta.getPath()+File.separator+meta.getName());
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection!=null)
                    connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
             }
        }finally {
            DBUtil.close(connection, statement);
        }
    }
    /*public static void main(String[] args) {
        delete(new FileMeta("中华人民共和国","D:\\ccc" , 0L, 0L, true));
    }*/

    public static List<FileMeta> search(String dir, String text) {
        //获取数据库连接
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FileMeta> metas = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            boolean empty = dir ==null|| dir.trim().length() == 0;
            String sql = "select name,path,size,last_modified, is_Directory" +
                    " from file_meta where name like ? or  pinying like ? " +
                    " or pinying_first like ? "
                    + (empty ?
                    "" : "and (path=? or path like ?)");
            System.out.println(sql);
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%"+text+"%");
            statement.setString(2, "%"+text+"%");
            statement.setString(3, "%"+text+"%");
            if (!empty){
                statement.setString(4, dir);
                statement.setString(5, dir+File.separator+"%");
            }
            System.out.println("search path=" + dir + "," + "text=" + text);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                long size = resultSet.getLong("size");
                long last_modified = resultSet.getLong("last_modified");
                boolean is_Directory = resultSet.getBoolean("is_Directory");
                FileMeta meta = new FileMeta(name, path, size, last_modified, is_Directory);
                System.out.println("query:" + meta.getName() + meta.getPath());
                metas.add(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return metas;
    }

}
