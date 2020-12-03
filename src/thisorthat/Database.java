package thisorthat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteDataSource;

public class Database {
	
	public static void main(String[] args) {
		SQLiteDataSource data = null;
		
		try {
			data = new SQLiteDataSource();
			data.setUrl("jdbc:sqlite:room_questions.db");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	
		String query = "CREATE TABLE IF NOT EXISTS questions ( " +
				"SUBJECT TEXT NOT NULL, " +
				"ANSWER_1 TEXT NOT NULL,"
				+ "ANSWER_2 TEXT NOT NULL, "
				+ "CORRECT_ANSWER_INDEX TEXT NOT NULL )";
		try ( Connection conn = data.getConnection();
				Statement stmt = conn.createStatement(); ) {
			int rv = stmt.executeUpdate( query );
			System.out.println( "executeUpdate() returned " + rv );
	      } catch ( SQLException e ) {
	    	  e.printStackTrace();
	    	  System.exit(0);
    	  }
		
		String query1 = "INSERT INTO room_questions ( QUESTION, ANSWER, OPTIONS ) "
				+ "VALUES ( 'Gullunge', 'Cheese', 'An Ikea Product or A Type of Cheese' )";
		String query2 = "INSERT INTO room_questions ( QUESTION, ANSWER, OPTIONS ) "
				+ "VALUES ( 'Gullunge', 'Cheese', 'An Ikea Product or A Type of Cheese' )";
		String query3 = "INSERT INTO room_questions ( QUESTION, ANSWER, OPTIONS ) "
				+ "VALUES ( 'Gullunge', 'Cheese', 'An Ikea Product or A Type of Cheese' )";
		
		try (Connection conn = data.getConnection();
				Statement stmt = conn.createStatement();) {
			int rv = stmt.executeUpdate(query1);
			rv = stmt.executeUpdate(query2);
			rv = stmt.executeUpdate(query3);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		try(Connection conn = data.getConnection();
				Statement stmt = conn.createStatement();) {
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String subject = rs.getString("QUESTION");
				String answer1 = rs.getString("ANSWER_1");
				String answer2 = rs.getString("ANSWER_2");
				String correctAnswerIndex = rs.getString("CORRECT_ANSWER_INDEX");
				System.out.println( "Result: Subject = " + subject +
	                    ", Answer 1 = " + answer1 +
	                    ", Answer 2 = " + answer2 +
	                    ", correct answer index = " + correctAnswerIndex);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}