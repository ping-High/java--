package dao;

import NlpSystem.domain.Corpus;
import NlpSystem.domain.WordCode;
import SpiderUtils.Spider;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.util.*;

/**
 * Created by hi on 2020/5/19.
 */
//连接Mysql8.0.19
public class JDBCDAO {
    private static String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
    private static String URL="jdbc:mysql://localhost:3306/qa?&useSSL=false&serverTimezone=UTC";
    private static String User="root";
    private static String PassWord="LOVEYOU0812";
    static Connection connection;
    static Statement statement;
    static{
        try{
            Class.forName(JDBC_DRIVER);
        }catch (Exception e){System.out.println("异常提醒:"+e);}
        try{
            connection= DriverManager.getConnection(URL,User,PassWord);
        }catch (Exception e){System.out.println("异常提醒:"+e);}
        try{
        statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        }catch (Exception e){System.out.println("异常提醒:"+e);}
    }
    //获取所有问题
    public static Vector<String> getQuestionVectorAll() {
        Vector<String> questionVecor=new Vector<>();
        String querySql="SELECT * FROM qa";
        try{
        ResultSet resultSet=statement.executeQuery(querySql);
        while(resultSet.next()){
            String temp=resultSet.getString("question");
            //System.out.print(temp);
            temp=temp.replace(" ","");
            //System.out.print(temp);
            questionVecor.add(temp);
        }}catch (Exception e){System.out.println("查询异常1:"+e);}
        return questionVecor;
    }
    //获取问题和答案
    public static Map<String ,String> getQuestionAndAnswer(){
        Map<String,String> questionAndAnswer=new LinkedHashMap<>();
        String querySql="SELECT * FROM qa";
        try{
            ResultSet resultSet=statement.executeQuery(querySql);
            while(resultSet.next()){
                questionAndAnswer.put(resultSet.getString("question").replace(" ",""),resultSet.getString("answer").replace(" ",""));
            }
        }catch (Exception e){System.out.println("查询异常2:"+e);}
        return questionAndAnswer;
    }
    //写入问题和答案
    public static boolean addQuestionAndAnswer(String code, String question,String answer){
        String insertSql="INSERT IGNORE INTO qa(code,question,answer) VALUES(\""+code+"\",\""+question+"\",\""+answer+"\")";
        try{
            statement.executeUpdate(insertSql);
            return true;
        }catch (Exception e){System.out.print("更新异常1："+e);}
        return false;
    }
    //编码写入数据库
    public static void addCode(String code,int index){
        String updateSql="UPDATE qa SET code=\""+code+"\" WHERE id="+index;
        try{
            statement.executeUpdate(updateSql);
        }catch (Exception e){System.out.print("更新异常2："+e);}
    }
    //获取编码
    public static Vector<String>getCode(){
        Vector<String> code=new Vector<>();
        String sql="SELECT code FROM qa";
        try{
            ResultSet rs=statement.executeQuery(sql);
            while(rs.next()){
                code.add(rs.getString("code"));
            }
        }catch (Exception e){System.out.print("查询异常3："+e);}
        return code;
    }
    //自动更新数据库
    public static void addQAToAutoUpdateDB(String code,String question,String answer){
        String insertSql="INSERT IGNORE INTO qa(code,question,answer) VALUES(\""+code+"\",\""+question+"\",\""+answer+"\")";
        try{
            statement.executeUpdate(insertSql);
        }catch (Exception e){System.out.print("更新异常3："+e);}
    }
    //insertDate的每个元素code、question、answer
    public static void autoUpdateDB(ArrayList<ArrayList<String>>insertDate){
        for(ArrayList<String> temp:insertDate){
            String code=temp.get(0);
            String question=temp.get(1);
            String answer=temp.get(2);
            addQAToAutoUpdateDB(code,question,answer);
        }
    }
}
