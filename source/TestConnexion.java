import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class TestConnexion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			if ("20,20".indexOf("20") != -1) {
				String alert="";
			}
			
			
			//JdbcOdbcDriver
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			//String connectionUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=OUISTITI","ouistiti","ouistiti";
			
			//Connection conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=OUISTITI","ouistiti","ouistiti");
			
			//Connection conn = DriverManager.getConnection("jdbc:odbc:OUISTITINT1","ouistiti","ouistiti");
			Connection conn = DriverManager.getConnection("jdbc:odbc:OUISTITINT1;user=ouistiti;password=ouistiti");

			Statement st = conn.createStatement();
			String sql = "select * from Clients";
			
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) { 
				System.out.println ( rs.getString("CodeClient") + "  \"" + rs.getString("Adresse") +"\"" );
			}
			if ( ! conn.isClosed()) {
			   conn.close();
			}

			//rs.findColumn("Adresse");
			//rs.getString(0);
			//Array anArray = rs.getArray("Adresse");
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}

}
