import java.sql.*;

public class Sqlconnect
{
	public static Connection dbConnector()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			Connection conn=DriverManager.getConnection("jdbc:sqlite:C:\\Users\\hariharn.r\\eclipse-workspace\\WhatsApp Integration\\Whatsapp.db");
			System.out.println("Connected");
			return conn;
		}
		catch(Exception e)
		{
			System.out.println("Error");
			return null;
		}
	}
}
