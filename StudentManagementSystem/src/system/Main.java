package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	static Connection conn;
	static Scanner sc = new Scanner(System.in);
	
	static final String userName = "root";
	static final String password = "root";
	static String dbName = "testdb";

	public static void main(String[] args) {

        //MySQLに接続する
        connectDB();

        StudentManager sm = new StudentManager(conn);
        
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
                    sm.addStudent();
                    break;
                case 2:
                    sm.deleteStudent();
                    break;
                case 3:
                    sm.searchStudent();
                    break;
                case 4:
                    sm.listStudent();
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


    //DBに接続する
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
    
    //DBを切断する
	public static void disconnectDB() {
    	try {
			conn.close();
			
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
















