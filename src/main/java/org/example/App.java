package org.example;

import org.example.infra.CreateConnection;
import org.example.infra.SetupDatabase;

import java.sql.Connection;

/**
 * Hello world!
 *
 */
public class App 
{

    private CreateConnection createConnection;
    private SetupDatabase database;
    public App() {
        this.createConnection = new CreateConnection();
        this.database = new SetupDatabase();
    }
    public static void main( String[] args )
    {
        App app = new App();
        app.demo();
    }

    public void demo() {
        database.createUserTable();
        database.createAccountTable();
        database.createTransactionTable();
    }
}
