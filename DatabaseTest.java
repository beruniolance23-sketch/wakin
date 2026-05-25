package lance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        // May dagdag na parameters sa dulo para i-force ang pag-save
        String url = "jdbc:mysql://localhost:3307/databasetest?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String username = "root"; 
        String password = "Lance2007!"; 

        System.out.println("Sumusubok kumonekta sa MySQL 3307...");

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Success ang koneksyon! Nagpapadala ng data...");
            
            // Siguraduhing naka-true ang Auto Commit para diretso save
            connection.setAutoCommit(true); 

            Statement statement = connection.createStatement();
            
            // Direktang SQL query para walang mintis
            String sql = "INSERT INTO users (pangalan, email) VALUES ('Maria Clara', 'maria.clara@email.com')";
            
            int rowsInserted = statement.executeUpdate(sql);
            
            if (rowsInserted > 0) {
                System.out.println("BOOM SUCCESS! Sabi ng Java na-save na raw!");
            }
            
            // Isara ang mga koneksyon nang maayos
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("May error na naganap:");
            e.printStackTrace();
        }
    }
}