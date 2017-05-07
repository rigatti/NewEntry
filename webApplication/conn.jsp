
<%@ page import="java.sql.Array" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="sun.jdbc.odbc.JdbcOdbcDriver.*" %>

<%
try {
	
		out.println("Ready");
	
	//JdbcOdbcDriver
	//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

	//String connectionUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=OUISTITI","ouistiti","ouistiti";
	
	//Connection conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=OUISTITI","ouistiti","ouistiti");
	Connection conn = DriverManager.getConnection("jdbc:odbc:OUISTITINT1;user=ouistiti;password=ouistiti");
	Statement st = conn.createStatement();
	String sql = "select * from Clients";
	
	ResultSet rs = st.executeQuery(sql);
out.println("TO PRINT");
	while (rs.next()) { 
		out.println(rs.getString("CodeClient") + "  \"" + rs.getString("Adresse") +"\"" );
	}
out.println("PRINTED");
	if ( ! conn.isClosed()) {
	   conn.close();
	}

	//rs.findColumn("Adresse");
	//rs.getString(0);
	//Array anArray = rs.getArray("Adresse");
	
//} catch (ClassNotFoundException e1) {
//out.println(e1);
//	e1.printStackTrace();
} catch (SQLException sqle){
out.println(sqle);
	sqle.printStackTrace();
} catch (Exception e){
out.println(e);
	e.printStackTrace();
}
%>
Done