package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import beans.Student;

public class Main {

	static Connection conn;

	public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        //MySQLに接続する
        connectDB();

        while (true) {
            //生徒管理システムのメニューを表示
            System.out.println("1.生徒の追加");
            System.out.println("2.生徒の削除");
            System.out.println("3.生徒の検索");
            System.out.println("4.生徒の一覧表示");
            System.out.println("5.終了");

            //入力を受け付ける
            System.out.println("メニュー番号を入力してください");
            int menu = sc.nextInt();

            //メニュー番号に応じた処理を実行
            switch(menu){
                case 1:
                    addStudent();
                    break;
                case 2:
                    deleteStudent();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    listStudent();
                    break;
                case 5:
                    System.out.println("アプリケーションを終了します");
                    sc.close();
                    disconnectDB();
                    return;
                default:
                    System.out.println("無効な値です");
                    break;
            }
        }


	}

	
    //生徒の追加
    public static void addStudent(){
        System.out.println("生徒の追加");
        Student student = new Student("田中", 20, "東京");

        //生徒情報の表示
        student.showInfo();
    }

    //生徒の削除
    public static void deleteStudent(){
        System.out.println("生徒の削除");
        
        
    }

    //生徒の検索
    public static void searchStudent(){
        System.out.println("生徒の検索");
                
        Scanner sc = new Scanner(System.in);
        String id = sc.next();
        
        try {
            
            
            String sql = "SELECT * FROM testtbl where id = '"+ id + "'";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                System.out.print(rs.getString("id") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.print(rs.getInt("age") + " ");
                System.out.print(rs.getString("address") + " ");
                System.out.print(rs.getString("createdat") + " ");
                System.out.print(rs.getString("deletedat") + " ");
                System.out.println(rs.getInt("delflg") + " ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    //生徒の一覧表示
    public static void listStudent(){
        System.out.println("生徒の一覧表示");

        try {           
            
            String sql = "SELECT * FROM testtbl";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                System.out.print(rs.getString("id") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.print(rs.getInt("age") + " ");
                System.out.print(rs.getString("address") + " ");
                System.out.print(rs.getString("createdat") + " ");
                System.out.print(rs.getString("deletedat") + " ");
                System.out.println(rs.getInt("delflg") + " ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * DBに接続する
     */
    public static void connectDB() {
    	
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/testdb";
	        conn = DriverManager.getConnection(url, "root", "root");
	        
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

    }
    
    public static void disconnectDB() {
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    	
    }

}
