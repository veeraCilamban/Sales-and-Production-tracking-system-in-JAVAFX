package application;

import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection {

    public Connection databaseLink;

    public Connection getConnection()
    {
        String databaseName = "BricksMgt";
        String databaseUser = "root";
        String databasePassword = "bow_manojs22";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return  databaseLink;
    }



}
