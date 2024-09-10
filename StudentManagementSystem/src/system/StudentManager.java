package system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class StudentManager {
	
	private Connection conn;
	private Scanner sc;
	
	public StudentManager(Connection conn) {
		this.conn = conn;
		sc = new Scanner(System.in);
	}
	
    //生徒の追加
    public void addStudent(){
    	
        System.out.println("生徒の追加");
        
        System.out.print("IDを入力してください：");
        String id = sc.next();
        System.out.print("名前を入力してください：");
        String name = sc.next();
        System.out.print("年齢を入力してください：");
        String age = sc.next();
        System.out.print("住所を入力してください：");
        String address = sc.next();
        
        
        try {
            String sql = "insert into student (id, name, age, address, delflg) values(?,?,?,?,0);";
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
    public void deleteStudent(){
        System.out.println("生徒の削除");
        System.out.print("削除したい生徒のIDを入力してください：");
        String deleteId = sc.next();

        try {
            String sql = "UPDATE student SET delflg = 1 WHERE id = ?";
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
    public void searchStudent(){
        System.out.println("生徒の検索");
        System.out.print("検索したい生徒の名前を入力してください：");        
        String name = sc.next();
        System.out.print("年齢を入力してください：");        
        String age = sc.next();
        System.out.print("退学者も表示しますか？(y/n)：");
        String flg = sc.next();
        
        String sql;
        if (flg.equals("y")) {
        	// 退学者も表示
        	sql = "SELECT * FROM student where name like ? and age = ?";
        	
        }else {
        	// 退学者を表示しない
        	sql = "SELECT * FROM student where name like ? and age = ? and delflg = 0";
        }
        
        try {
            
            
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
    public void listStudent(){
        System.out.println("生徒の一覧表示");

        try {           
        	
            String sql = "SELECT * FROM student WHERE delflg = 0";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                System.out.print(rs.getString("id") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.print(rs.getInt("age") + " ");
                System.out.print(rs.getString("address") + " ");
                System.out.print(rs.getString("createdate") + " ");
                System.out.println(rs.getString("deletedate") + " ");
                //System.out.println(rs.getInt("delflg") + " ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    //テスト結果の表示
	public void showTestResult() {
		System.out.println("テスト結果の表示");
		
		System.out.print("試験名：");
		String testName = sc.nextLine();
		
		System.out.print("試験No：");
		String testNo = sc.nextLine();
		
		
		try {
			String sql = "SELECT * FROM shiken where subject_name = ? and subject_no = ? order by gakusei_id";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, testName);
			pstmt.setString(2, testNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (!rs.next()) {
                System.out.println("該当する試験はありません");
			}else {
				System.out.println("実施日：" + rs.getString("test_date"));
				System.out.println("学生No\t点数");
                do {
                    System.out.print(rs.getString("gakusei_id") + " ");
                    System.out.println(rs.getString("score") + " ");
                }while (rs.next());
            }
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
	}

	// テストを実施する
	public void doTest() {
		System.out.println("テストの実施");
		
		System.out.print("試験名：");
		String testName = sc.nextLine();
		
		System.out.print("試験No：");
		String testNo = sc.nextLine();
		
		//試験が存在するか確認
		String flg = "y";
		try {
			if (existTest(testName, testNo)) {
				System.out.println("試験が存在します");
				return;
			}else {
				do {
					//学生NOとスコアを入力する
					System.out.println("学生NOとスコアを入力してください");
					System.out.print("学生NO：");
					String studentNo = sc.nextLine();
					System.out.print("スコア：");
					String score = sc.nextLine();
					
					//学生NOが学生表に存在するか確認
					String sql = "SELECT * FROM student where id = ?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, studentNo);
					
					ResultSet rs = pstmt.executeQuery();
					
					if (!rs.next()) {
						System.out.println("学生NOが存在しません");
						System.out.print("続けますか？(y/n):");
						flg = sc.nextLine();
						continue;
					}
					
					//学生NOが試験に登録済か
					sql = "SELECT * FROM shiken WHERE subject_name = ? AND subject_no = ? AND gakusei_id = ?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, testName);
					pstmt.setString(2, testNo);
					pstmt.setString(3, studentNo);
					
					rs = pstmt.executeQuery();
					
					if (rs.next()) {
						System.out.println("すでにスコアが記録されています");
						System.out.print("続けますか？(y/n):");
						flg = sc.nextLine();
						continue;
					}
					
					
					//テスト結果を登録する
					sql = "INSERT INTO shiken (subject_name, subject_no, gakusei_id, score) VALUES(?,?,?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, testName);
					pstmt.setString(2, testNo);
					pstmt.setString(3, studentNo);
					pstmt.setString(4, score);
					
					int rowsAffected = pstmt.executeUpdate();
					
					if (rowsAffected > 0) {
	                    System.out.println("テスト結果を登録しました");
	                    System.out.print("続けますか？(y/n):");
	                    flg = sc.nextLine();
	                    
					}else {
						System.out.println("テスト結果の登録に失敗しました");
						return;
					}
				}while(flg.equals("y"));
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// テストを修正する
	public void updateTest() {
		
		int rowsAffected = 0;
		String updateSql;
		
		
		System.out.print("科目名：");
		String kamoku = sc.nextLine();
		System.out.print("試験No:");
		String no = sc.nextLine();
		
		String sql = "SELECT * FROM shiken WHERE subject_name = ? AND subject_no = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kamoku);
			pstmt.setString(2, no);
			
			ResultSet rs = pstmt.executeQuery();
			System.out.println("-----------------------------------------");
			System.out.println("id\t学生番号\t試験名 試験No 得点 実施日   ");
			System.out.println("-----------------------------------------");
			while( rs.next() ) {
				System.out.print( rs.getInt("id") );
				System.out.print("\t");
				System.out.print( rs.getString("gakusei_id") );
				System.out.print("\t\t");
				System.out.print( rs.getString("subject_name") );
				System.out.print(" ");
				System.out.print( rs.getInt("subject_no") );
				System.out.print(" ");
				System.out.print( rs.getInt("score") );
				System.out.print(" ");
				System.out.println( rs.getString("test_date") );
				
			}
			System.out.println("-----------------------------------------");
			
			System.out.print("修正id：");
			String id = sc.nextLine();
			
			System.out.println("修正項目");
			System.out.println("1.学生番号");
			System.out.println("2.試験名");
			System.out.println("3.試験No");
			System.out.println("4.得点");
			System.out.println("5.実施日");
			
			// SQLをもう1回実行
			rs = pstmt.executeQuery();
			
			String dbId = "";
			String gakuseiId = "";
			String subjectName ="";
			int subjectNo = 0;
			
			// 指定されたidの情報を取得する
			while(rs.next()) {
				dbId = String.valueOf( rs.getInt("id") );
				if( dbId.equals(id) ) {
					gakuseiId = rs.getString("gakusei_id");	//学生番号
					subjectName = rs.getString("subject_name"); //試験名
					subjectNo = rs.getInt("subject_no"); //試験No
					break;
				}
			}
			
			if( dbId.isEmpty() ) {
				System.out.println("そのIDはありません");
				
			}else {
				
				// メニューの番号を入力
				String inputNo = sc.nextLine();
				
				switch (inputNo) {
				
				// 学生番号の修正
				case "1": 
					// > 学生番号（240001）→：240005
					System.out.print("学生番号(" + gakuseiId +")→：");	
					String updateGakuseiID = sc.nextLine();
					
					// 学生が存在するかチェック
					String gakuseiSql = "SELECT * FROM student WHERE id = ?";
					pstmt = conn.prepareStatement(gakuseiSql);
					pstmt.setString(1, updateGakuseiID);
					if( !pstmt.executeQuery().next() ) {
						System.out.println("学生が存在しません");
						break;
					}
										
					
					updateSql = "UPDATE shiken SET gakusei_id = ? WHERE id = ?";
					
		            pstmt = conn.prepareStatement(updateSql);
		            pstmt.setString(1, updateGakuseiID);
		            pstmt.setString(2, dbId);

		            rowsAffected = pstmt.executeUpdate();
					System.out.println("修正しました");
					
					break;
				
				//試験名の修正
				case "2":
					System.out.println("試験名：" );
					subjectName = sc.nextLine();
					
					// 変更する試験名とIDが存在するか
					sql = "SELECT * FROM shiken WHERE subject_name = ? AND subject_no =?";
		            pstmt = conn.prepareStatement(sql);
		            pstmt.setString(1, subjectName);
		            pstmt.setInt(2, subjectNo);
					if( pstmt.executeQuery().next() ) {
						System.out.println("同一試験が存在します");
						break;
					}
					
					
					updateSql = "UPDATE shiken SET subject_name = ? WHERE id = ?";
		            pstmt = conn.prepareStatement(updateSql);
		            pstmt.setString(1, subjectName);
		            pstmt.setString(2, dbId);

		            rowsAffected = pstmt.executeUpdate();
					System.out.println("修正しました");
					
					
					break;
				
				//試験Noの修正
				case "3":
					System.out.println("試験No：" );
					subjectNo = Integer.parseInt(sc.nextLine());
					
					// 変更する試験名とIDが存在するか
					sql = "SELECT * FROM shiken WHERE subject_name = ? AND subject_no =?";
		            pstmt = conn.prepareStatement(sql);
		            pstmt.setString(1, subjectName);
		            pstmt.setInt(2, subjectNo);
					if( pstmt.executeQuery().next() ) {
						System.out.println("同一試験が存在します");
						break;
					}
					
					
					String updataSql = "UPDATE shiken SET subject_no = ? WHERE id = ?";
		            pstmt = conn.prepareStatement(updataSql);
		            pstmt.setString(1, subjectName);
		            pstmt.setString(2, dbId);

		            rowsAffected = pstmt.executeUpdate();
					System.out.println("修正しました");
					break;
				
				//得点の修正
				case "4":
					System.out.println("得点：" );
					int score = sc.nextInt();
					
					updataSql = "UPDATE shiken SET score = ? WHERE id = ?";
		            pstmt = conn.prepareStatement(updataSql);
		            pstmt.setInt(1, score);
		            pstmt.setString(2, dbId);

		            rowsAffected = pstmt.executeUpdate();
					System.out.println("修正しました");
					break;

					
				//実施日の修正
				case "5":
					System.out.println("実施日：" );
					String date = sc.nextLine();
					
					updataSql = "UPDATE shiken SET test_date = ? WHERE id = ?";
		            pstmt = conn.prepareStatement(updataSql);
		            pstmt.setString(1, date);
		            pstmt.setString(2, dbId);

		            rowsAffected = pstmt.executeUpdate();
					System.out.println("修正しました");
					break;

				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}

	// テストを削除する
	public void deleteTest() {
		String sql = "SELECT subject_name, subject_no FROM shiken GROUP BY subject_no, subject_name";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			System.out.println("-----------------------------------------");
			System.out.println("試験名\t\t試験No");
			System.out.println("-----------------------------------------");
			while (rs.next()) {
				
				System.out.print(rs.getString("subject_name"));
				System.out.print("\t\t");
				System.out.print(rs.getInt("subject_no"));
				System.out.println();
			}
			System.out.println("-----------------------------------------");

			System.out.print("削除する試験名：");
			String subject_name = sc.nextLine();
			
			System.out.print("削除する試験No：");
			int subject_no = Integer.parseInt( sc.nextLine() );

			sql = "DELETE FROM shiken WHERE subject_name = ? AND subject_no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject_name);
			pstmt.setInt(2, subject_no);

			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("削除しました");
			} else {
				System.out.println("削除に失敗しました");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
    
	// 生徒情報を修正する
	public void updateStudent() {
		
		// 生徒一覧を表示する
		String sql = "SELECT id, name, age, address FROM student WHERE delflg = 0";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
		
			ResultSet rs = pstmt.executeQuery();
			System.out.println("-----------------------------------------");
			System.out.println("id\t\t氏名\t\t年齢\t住所   ");
			System.out.println("-----------------------------------------");
			while( rs.next() ) {
				System.out.print( rs.getInt("id") );
				System.out.print("\t");
				System.out.print( rs.getString("name") );
				System.out.print("\t\t");
				System.out.print( rs.getInt("age") );
				System.out.print(" ");
				System.out.println( rs.getString("address") );

			}
			System.out.println("-----------------------------------------");
			
			System.out.print("修正id：");
			String id = sc.nextLine();
			
			System.out.println("修正項目");
			System.out.println("1.id(学生番号)");
			System.out.println("2.氏名");
			System.out.println("3.年齢");
			System.out.println("4.住所");
			
			String no = sc.nextLine();
			
			int cnt = 0;
			switch (no) {
				case "1":
					try {
						// id修正
						System.out.print(id + "->");
						String updateId = sc.nextLine();
						
						sql = "update student set id = ? where id = ?";
						
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, updateId);
						pstmt.setString(2, id);
						
						cnt = pstmt.executeUpdate();
						
						break;
						
					}catch(SQLIntegrityConstraintViolationException e ) {
						System.out.println("同じIDが存在します");
						updateStudent();
					}
					
				case "2":
					

					// 氏名修正
					sql = "select * from student where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					rs = pstmt.executeQuery();
					while( rs.next() ) {
						System.out.print( rs.getString("name") + "->");
						
					}
					
					String name = sc.nextLine();
					
					sql = "update student set name = ? where id = ?";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, name);
					pstmt.setString(2, id);
					
					cnt = pstmt.executeUpdate();
					
					break;

					
					
				case "3":
					// 年齢修正
					
					sql = " select age from student where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					rs = pstmt.executeQuery();
					while( rs.next() ) {
						System.out.print( rs.getString("age")  + "->");
						
					}
					
					String age = sc.nextLine();
					
					sql = "update student set age = ? where id = ?";
	
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, age);
					pstmt.setString(2, id);
					
					cnt = pstmt.executeUpdate();
					
					break;
					
				case "4":
					// 住所修正
					sql = "select * from student where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					rs = pstmt.executeQuery();
					while( rs.next() ) {
						System.out.print( rs.getString("address") + "->");
						
					}
					
					String address = sc.nextLine();
					if( address.length() >= 200 ) { //文字サイズ確認
						updateStudent();
					}
					
					sql = "update student set address = ? where id = ?";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, address);
					pstmt.setString(2, id);
					
					cnt = pstmt.executeUpdate();
					
					break;
						

			}

			
			
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	
	
	// 試験が存在するか確認する
	private boolean existTest(String testName, String testNo) {
		try {
			String sql = "SELECT * FROM shiken where subject_name = ? and subject_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, testName);
			pstmt.setString(2, testNo);

			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	//テスト一覧表示
	public void listTest() {
        System.out.println("試験の一覧表示");

        try {           
        	
        	String sql = "select subject_name, subject_no from shiken group by subject_name, subject_no";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("--------------------------------");
            System.out.println("subject_name\tsubject_no");
            System.out.println("--------------------------------");
            while (rs.next()) {
                System.out.print(rs.getString("subject_name") + "\t\t\t");
                System.out.println(rs.getString("subject_no"));

            }
            System.out.println("--------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	//テストの検索
	public void showTest() {
        System.out.println("試験の検索");
        System.out.print("試験名->");
        String testName = sc.nextLine();
        
        System.out.print("試験No->");
        int testNo = sc.nextInt();
        sc.nextLine();
        

        try {           
        	
        	String sql = "select * from shiken where subject_name = ? and subject_no = ?";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, testName);
            pstmt.setInt(2, testNo);
            
            ResultSet rs = pstmt.executeQuery();

            System.out.println("----------------------------------------------------------------");
            System.out.println("id\tgakusei_id\tsubject_name\tsubject_no\tscore\ttest_date");
            System.out.println("----------------------------------------------------------------");
            while (rs.next()) {
                System.out.print(rs.getString("id")+"\t");
                System.out.print(rs.getString("gakusei_id")+"\t");
                System.out.print(rs.getString("subject_name")+"\t");
                System.out.print(rs.getInt("subject_no")+"\t");
                System.out.print(rs.getInt("score")+"\t");
                System.out.println(rs.getString("test_date")+"\t");
            }
            System.out.println("----------------------------------------------------------------");

            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    
}



