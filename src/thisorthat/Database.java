package thisorthat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sqlite.SQLiteDataSource;

public class Database implements Serializable {
	
	/**
	 * Generated serializable ID.
	 */
	private static final long serialVersionUID = -2990418274127860974L;
	/**
	 * Database information mapped to prompt for access to 
	 * information for each question
	 */
	private Map<String, List<String>> myDatabase = new HashMap<>();	
	
	/**
	 * Reads in a csv file and creates a SQLite database
	 */
	public Database() {
		SQLiteDataSource data = null;
		
		final List<List<String>> questions = new ArrayList<>();
		try (BufferedReader read = new BufferedReader(new FileReader("Questions.csv"));) {
			String line;
			while ((line = read.readLine()) != null) {
				String[] info = line.split(",");
				questions.add(Arrays.asList(info));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Sets up a database url to input information
		try {
			data = new SQLiteDataSource();
			data.setUrl("jdbc:sqlite:questions.db");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	
		//Creates table with columns related to data pieces in each question
		String query = "CREATE TABLE IF NOT EXISTS questions ( " +
				"TOPIC , " +
				"PROMPT, ANSWER_0,"
				+ "ANSWER_1 , "
				+ "CORRECT_ANSWER_INDEX)";	
		try ( Connection conn = data.getConnection();
				Statement stmt = conn.createStatement(); ) {
			int rv = stmt.executeUpdate(query);
	      } catch ( SQLException e ) {
	    	  // TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		try(Connection conn = data.getConnection();
				Statement stmt = conn.createStatement();) {
			
			query = "SELECT * FROM questions";
			
			ResultSet rs = stmt.executeQuery(query);
			
			//reads through questions and puts them in Map
			while(rs.next()) {
				String topic = rs.getString("TOPIC");
				String prompt = rs.getString("PROMPT");
				String answer1 = rs.getString("ANSWER_0");
				String answer2 = rs.getString("ANSWER_1");
				String correctAnswerIndex = rs.getString("CORRECT_ANSWER_INDEX");
				if (!myDatabase.containsKey(prompt)) {
					List<String> info = new ArrayList<>();
					info.add(topic);
					info.add(answer1);
					info.add(answer2);
					info.add(correctAnswerIndex);
					myDatabase.put(prompt, info);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	/**
	 * Getter for myDatabase
	 * @return myDatabase
	 */
	Map<String, List<String>> getDatabase() {
		return this.myDatabase;
	}
}