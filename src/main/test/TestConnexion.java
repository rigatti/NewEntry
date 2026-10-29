import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnexion {

	public static void main(String[] args) {
		String url = "jdbc:sqlserver://localhost:1433;databaseName=OUISTITI-TEST;encrypt=false";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			try (Connection conn = DriverManager.getConnection(url, "ouistiti", "ouistiti");
				 Statement st = conn.createStatement();
				 ResultSet rs = st.executeQuery("select CodeClient, Adresse from Clients")) {
				while (rs.next()) {
					System.out.println(rs.getString("CodeClient") + "  \"" + rs.getString("Adresse") + "\"");
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
