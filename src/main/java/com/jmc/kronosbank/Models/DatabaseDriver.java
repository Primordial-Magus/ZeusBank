package com.jmc.kronosbank.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:kronosbank.db");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    * Client Section
    * */

    public ResultSet getClientData(String pAddress, String password) {
        String sql =
                """
                    SELECT * FROM Clients WHERE PayeeAddress=? AND Password=?;
                """;

        ResultSet resultSet = null;
        try {

            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);

            preparedStatement.setString(1, pAddress);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getTransactions(String pAddress, int limit) {
        String sql =
                """
                    SELECT * FROM Transactions WHERE Sender=? OR Receiver=? LIMIT ?;
                """;

        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);

            preparedStatement.setString(1, pAddress);
            preparedStatement.setString(2, pAddress);
            preparedStatement.setInt(3, limit);

            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    // Method returns savings account balance
    public double getSavingsAccountBalance(String pAddress) {
        String sql =
                """
                    SELECT * FROM SavingsAccounts WHERE Owner= ?;
                """;

        ResultSet resultSet;
        double balance = 0;
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement((sql));

            preparedStatement.setString(1, pAddress);
            resultSet = preparedStatement.executeQuery();
            balance = resultSet.getDouble("Balance");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }

    // Method to either add or subtract from balance given operation
    public void updateBalance(String pAddress, double amount, String operation) {

        String sql1 =
                """
                     SELECT * FROM SavingsAccounts WHERE Owner=?;
                """;

        String sql2 =
                """
                    UPDATE SavingsAccounts SET Balance=? WHERE Owner=?;
                """;


        ResultSet resultSet;
        try{

            PreparedStatement preparedStatement1 = this.conn.prepareStatement((sql1));
            preparedStatement1.setString(1, pAddress);

            resultSet = preparedStatement1.executeQuery();

            PreparedStatement preparedStatement2 = this.conn.prepareStatement(sql2);
            preparedStatement2.setDouble(1, amount);
            preparedStatement2.setString(2, pAddress);

            double newBalance;



            if (operation.equals("ADD")){
                newBalance = resultSet.getDouble("Balance") + amount;
                preparedStatement2.executeUpdate();
            } else {
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    preparedStatement2.executeUpdate();
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Creates and records new transaction
    public void newTransaction(String sender, String receiver, double amount, String message) {

        String sql =
                """
                    INSERT INTO
                        Transactions(Sender, Receiver, Amount, Date, Message)
                        VALUES (?, ?, ?, ?, ?);
                """;



        try {

            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            LocalDate localDate = LocalDate.now();

            // convert Date to LocalDate
//            Date date = Date.valueOf(localDate);

            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, receiver);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, localDate.toString());
            preparedStatement.setString(5, message);
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    * Admin Section
    * */

    public ResultSet getAdminData(String username, String password) {

        String sql =
                """
                    SELECT * FROM Admins WHERE Username=? AND Password=?;
                """;

        ResultSet resultSet = null;
        try {
        PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery("c");
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {

        String sql =
                """
                    INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date)
                        VALUES (?, ?, ?, ?, ?);
                        """;


        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, pAddress);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, date.toString());

            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {

        String sql =
                """
                    INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance)
                        VALUES (?, ?, ?, ?);
                """;

        try {

            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, owner);
            preparedStatement.setString(2, number);
            preparedStatement.setDouble(3, tLimit);
            preparedStatement.setDouble(4, balance);


            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {

        String sql =
                """
                    INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance)"
                        VALUES (?, ?, ?, ?);
                """;

        try {

            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, owner);
            preparedStatement.setString(2, number);
            preparedStatement.setDouble(3, wLimit);
            preparedStatement.setDouble(4,  balance);

            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getAllClientsData() {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients;");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void depositSavings(String pAddress, double amount) {

        String sql =
                """
                    UPDATE SavingsAccounts SET Balance=? WHERE Owner=?;
                """;


        try {

            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, pAddress);
            preparedStatement.setDouble(2, amount);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    * Utility Methods
    * */

    public ResultSet searchClient(String pAddress) {

        String sql =
                """
                    SELECT * FROM Clients WHERE PayeeAddress=?;
                """;

        ResultSet resultSet = null;

        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, pAddress);

            resultSet = preparedStatement.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public int getLastClientsId() {
        String sql =
                """
                    SELECT * FROM sqlite_sequence WHERE name='Clients';
                """;


        ResultSet resultSet;
        int id = 0;
        try {

            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            id = resultSet.getInt("seq");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public ResultSet getCheckingAccountData(String pAddress) {
        String sql =
                """
                    SELECT * FROM CheckingAccounts WHERE Owner=?;
                """;

        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, pAddress);

            resultSet = preparedStatement.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        String sql =
                """
                    SELECT * FROM SavingsAccounts WHERE Owner=?;
                """;

        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, pAddress);

            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
}
