package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	static Connection conn;
	
	static final String userName = "root";
	static final String password = "root";
	static String dbName = "testdb";

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
        Scanner sc = new Scanner(System.in);
        
        System.out.print("IDを入力してください：");
        String id = sc.next();
        System.out.print("名前を入力してください：");
        String name = sc.next();
        System.out.print("年齢を入力してください：");
        String age = sc.next();
        System.out.print("住所を入力してください：");
        String address = sc.next();
        
        
        try {
            String sql = "insert into testtbl (id, name, age, address, delflg) values(?,?,?,?,0);";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, age);
            pstmt.setString(4, address);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("生徒を追加しました");
            } else {
                System.out.println("生徒が追加できませんでした");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    //生徒の削除
    public static void deleteStudent(){
        System.out.println("生徒の削除");
        System.out.print("削除したい生徒のIDを入力してください：");
        Scanner sc = new Scanner(System.in);
        String deleteId = sc.next();

        try {
            String sql = "DELETE FROM testtbl WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, deleteId);
            
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("生徒が削除されました");
            } else {
                System.out.println("該当する生徒が見つかりませんでした");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    //生徒の検索
    public static void searchStudent(){
        System.out.println("生徒の検索");
        System.out.print("検索したい生徒の名前を入力してください：");        
        Scanner sc = new Scanner(System.in);
        String name = sc.next();
        System.out.print("年齢を入力してください：");        
        String age = sc.next();
        
        try {
            
            String sql = "SELECT * FROM testtbl where name like ? and age = ?";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            pstmt.setString(2, age);
            
            ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("該当する生徒はいません");
			}else {
				do {
					System.out.print(rs.getString("id") + " ");
					System.out.print(rs.getString("name") + " ");
					System.out.print(rs.getInt("age") + " ");
					System.out.print(rs.getString("address") + " ");
					System.out.print(rs.getString("createdat") + " ");
					System.out.print(rs.getString("deletedat") + " ");
					System.out.println(rs.getInt("delflg") + " ");
				}while (rs.next());
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
    
    public static void connectDB() {
    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/" + dbName;
			conn = DriverManager.getConnection(url, userName, password);
			
		} catch (ClassNotFoundException e) {
			//エラーが起きた時の処理
			
			
		} catch (SQLException e) {
			//エラーが起きた時の処理
			
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
















