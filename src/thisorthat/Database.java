package thisorthat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sqlite.SQLiteDataSource;

public class Database {
	
	public static void main(String[] args) {
		SQLiteDataSource data = null;
		
		final List<List<String>> questions = new ArrayList<>();
		try (BufferedReader read = new BufferedReader(new FileReader("Questions.csv"));) {
			String line;
			while ((line = read.readLine()) != null) {
				String[] info = line.split(",");
				questions.add(Arrays.asList(info));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			data = new SQLiteDataSource();
			data.setUrl("jdbc:sqlite:questions.db");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	
		String query = "CREATE TABLE IF NOT EXISTS questions ( " +
				"TOPIC , " +
				"PROMPT, ANSWER_0,"
				+ "ANSWER_1 , "
				+ "CORRECT_ANSWER_INDEX)";
		try ( Connection conn = data.getConnection();
				Statement stmt = conn.createStatement(); ) {
			int rv = stmt.executeUpdate(query);
			System.out.println( "executeUpdate() returned " + rv );
	      } catch ( SQLException e ) {
	    	  e.printStackTrace();
	    	  System.exit(0);
    	  }
		
		List<String> queries = new ArrayList<>();
		for(List<String> listS: questions.subList(1, questions.size())) {
			queries.add("INSERT INTO questions ( TOPIC, PROMPT, ANSWER_0, ANSWER_1, CORRECT_ANSWER_INDEX ) "
					+ "VALUES ('"+ listS.get(0) +"', '"+ listS.get(1) +
					"', '"+ listS.get(2) +"', '"+ listS.get(3) +"', '" +
					listS.get(4) + "' )");
		}
		
		try (Connection conn = data.getConnection();
				Statement stmt = conn.createStatement();) {
//			int rv = 0;
			for (String s: queries) {
				int rv = stmt.executeUpdate(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		try(Connection conn = data.getConnection();
				Statement stmt = conn.createStatement();) {
			
			query = "SELECT * FROM questions";
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String topic = rs.getString("TOPIC");
				String prompt = rs.getString("PROMPT");
				String answer1 = rs.getString("ANSWER_0");
				String answer2 = rs.getString("ANSWER_1");
				String correctAnswerIndex = rs.getString("CORRECT_ANSWER_INDEX");
				System.out.println( "Result: Topic = " + topic + ", Prompt =" + prompt +
	                    ", Answer_0 = " + answer1 +
	                    ", Answer_1 = " + answer2 +
	                    ", Correct_Answer_Index = " + correctAnswerIndex);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}