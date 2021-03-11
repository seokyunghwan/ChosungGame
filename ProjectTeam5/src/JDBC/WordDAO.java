package JDBC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDAO {
	
	private Connection conn;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Map<Character, String[]> wordMap;
	private List<String> list;
	
	public WordDAO (Connection conn) throws ClassNotFoundException, SQLException {
		this.conn = conn;
		this.wordMap = new HashMap<>();
		wordMap.put('ㄱ', new String[] {"가", "44619"});
		wordMap.put('ㄴ', new String[] {"나", "45795"});
		wordMap.put('ㄷ', new String[] {"다", "46383"});
		wordMap.put('ㄹ', new String[] {"라", "47559"});
		wordMap.put('ㅁ', new String[] {"마", "48147"});
		wordMap.put('ㅂ', new String[] {"바", "48735"});
		wordMap.put('ㅅ', new String[] {"사", "49911"});
		wordMap.put('ㅇ', new String[] {"아", "51087"});
		wordMap.put('ㅈ', new String[] {"자", "51675"});
		wordMap.put('ㅊ', new String[] {"차", "52851"});
		wordMap.put('ㅋ', new String[] {"카", "53439"});
		wordMap.put('ㅌ', new String[] {"타", "54027"});
		wordMap.put('ㅍ', new String[] {"파", "54615"});
		wordMap.put('ㅎ', new String[] {"하", "55203"});
	}
	
	// DB에 모든 단어 넣기
	@SuppressWarnings("resource")
	public void insertWord() {
		String word;
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/VO/WORD_TEXT.txt"));
			while ((word = br.readLine()) != null) {
				if (!word.isEmpty() && word.length()==2) {
					String sql = "INSERT INTO WORD_TABLE values(?)";
					try {
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, word);
						pstmt.executeUpdate();
					} catch (SQLException e) {
					} finally {
						try {
							if(pstmt != null) pstmt.close();
						} catch (SQLException e) {
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	// 테이블 생성
	public boolean createTable() {
		String sql = "CREATE TABLE WORD_TABLE (WORD VARCHAR2(20) PRIMARY KEY)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	
	// SELECT * FROM word_table WHERE (SUBSTR(word, 1, 1) BETWEEN '가' AND NCHR(44619)) AND (SUBSTR(word, 2, 2) BETWEEN '나' AND NCHR(45795));
	// 단어 길이 무조건 2개
	public List<String> selectWord (String initial) {
		char[] initialChar = initial.toCharArray();
		String[] condition1 = wordMap.get(initialChar[0]);
		String[] condition2 = wordMap.get(initialChar[1]);
		String sql = "SELECT * FROM WORD_TABLE WHERE (SUBSTR(word, 1, 1) BETWEEN ? AND NCHR(?)) AND (SUBSTR(word, 2, 2) BETWEEN ? AND NCHR(?))";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, condition1[0]);
			pstmt.setInt(2, Integer.parseInt(condition1[1]));
			pstmt.setString(3, condition2[0]);
			pstmt.setInt(4, Integer.parseInt(condition2[1]));
			rs = pstmt.executeQuery();
			list = new ArrayList<>();
			while (rs.next()) {
				list.add(rs.getString("word"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
