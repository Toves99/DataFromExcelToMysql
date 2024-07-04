package org.example;

import java.sql.*;

public class DatabaseConnection {
    private static final String dbUrl="jdbc:mysql://localhost:3306/MyData";
    private static final String username="root";
    private static final String password="";



    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl,username,password);
    }

    public void insertDataFromExcel(String[] data){
        String sqlSelect = "SELECT * FROM studentTable WHERE first_name = ? AND last_name = ?";
        String sql="INSERT INTO studentTable(first_name,last_name,dob,age,country,county,school) VALUES(?,?,?,?,?,?,?)";
        try(Connection connection=getConnection();
            PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
            PreparedStatement insertStatement  = connection.prepareStatement(sql)){

            selectStatement.setString(1, data[0]); // Assuming first_name is data[0]
            selectStatement.setString(2, data[1]); // Assuming last_name is data[1]

            ResultSet resultSet = selectStatement.executeQuery();

            if(!resultSet.next()){
                insertStatement.setString(1, data[0]);
                insertStatement.setString(2, data[1]);
                insertStatement.setString(3, data[2]);
                insertStatement.setString(4, data[3]);
                insertStatement.setString(5, data[4]);
                insertStatement.setString(6, data[5]);
                insertStatement.setString(7, data[6]);


                insertStatement.executeUpdate();
                System.out.println("Inserted data for: " + data[0] + " " + data[1]);
            }else{
                System.out.println("Skipping insertion for existing data: " + data[0] + " " + data[1]);
            }



        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
