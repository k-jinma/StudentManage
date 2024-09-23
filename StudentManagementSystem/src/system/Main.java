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

	static final String loginPassword = "abcd";	//システムログインパスワード
	
	public static void main(String[] args) {
		
		
		//ログイン
		System.out.println("==========試験システム==========");
		
		String inputPassword;
		do {
			System.out.print("パスワード: ");
			inputPassword = sc.nextLine();
			
		}while( !inputPassword.equals(loginPassword) );
		
		System.out.println("==========ログイン成功==========");

        //MySQLに接続する
        connectDB();

        StudentManager sm = new StudentManager(conn);
        
        while (true) {
            //生徒管理システムのメニューを表示
        	System.out.println("1.生徒の一覧表示");
        	System.out.println("2.生徒の検索");
            System.out.println("3.生徒の追加");
            System.out.println("4.生徒の修正"); 
            System.out.println("5.生徒の削除");	
          
            System.out.println("6.テスト一覧表示"); 
            System.out.println("7.テストの検索"); 
            System.out.println("8.テストの実施"); 
            System.out.println("9.テスト結果を表示"); 
            System.out.println("10.テストを修正する"); // 削除時など表示を修正
            System.out.println("11.テストを削除する"); 
            
            System.out.println("99.終了");

            //入力を受け付ける
            System.out.println("メニュー番号を入力してください");
            
        	try {

				int menu = sc.nextInt();
				sc.nextLine();
				
				//メニュー番号に応じた処理を実行
				switch(menu){
				    case 1:	//生徒の一覧表示
				    	sm.listStudent();
				    	break;
				    case 2: //生徒の検索
				    	sm.searchStudent();
				    	break;
				    case 3: //生徒の追加
				        sm.addStudent();
				        break;
				    case 4: //生徒の修正
						sm.updateStudent();
						break;
				    case 5: //生徒の削除
				        sm.deleteStudent();
				        break;
				        
					case 6: //テスト一覧表示
						sm.listTest();
						break;
					case 7: //テストの検索
						sm.showTest();
						break;
				    case 8: //テストの実施
						sm.doTest();
						break;
				    case 9: //テスト結果を表示
				    	sm.showTestResult();
				    	break;
				    case 10: //テストを修正する
				    	sm.updateTest();
				    	break;
				    case 11: //テストを削除する
				    	sm.deleteTest();
				    	break;
				    	
				    case 99: //終了
				    	System.out.println("アプリケーションを終了します");
				    	sc.close();
				    	disconnectDB();
				    	return;
				    	
				    	
				    default:
				        System.out.println("無効な値です");
				        break;
				}
			} catch (Exception e) {
				System.err.println("無効な値です");
				sc.nextLine();
				continue;
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
			System.err.println("データベース接続エラーが発生しました。システムを終了します。");
			System.exit(0);	
			
		} catch (SQLException e) {
			System.err.println("データベース処理エラーが発生しました。システムを終了します。");
			System.exit(0);
		}
    }
    
    //DBを切断する
	public static void disconnectDB() {
    	try {
			conn.close();
			
		} catch (SQLException e) {
			System.err.println("データベース切断エラーが発生しました。システムを終了します。");
			System.exit(0);
		}
	}

}
















