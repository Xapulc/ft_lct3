/**
 * Класс для работы с БД.
 * Если есть соединение с БД, то, если есть таблица dataTable,
 * то она заполняется данными из интернета/используется как источник данных,
 * иначе она создается и заполняется.
 */


import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import static java.sql.DriverManager.getConnection;

class DataBaseWorker {
    private static String JDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static String table = "dataTable";

    private Connection connection;

    DataBaseWorker () {}

    private int getCountRows() {
        Statement statement = null;
        String query = "SELECT COUNT(*) FROM " + table;
        ResultSet res = null;
        try {
            statement = connection.createStatement();
            res = statement.executeQuery(query);
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        try {
            res.next();
            return res.getInt(1);
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        try {
            statement.close();
            res.close();
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        return 0;
    }

    void add(DataModel dataModel) {
        Statement statement = null;

        //проверка того, что человека с таким ФИО нет
        String query = "SELECT 1 from " + table
                + " WHERE name='" + dataModel.getName() + "' "
                + "AND surname='" + dataModel.getSurname() + "' "
                + "AND patronymic='" + dataModel.getPatronymic() + "'";

        ResultSet res = null;
        try {
            statement = connection.createStatement();
            res = statement.executeQuery(query);
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        boolean isNewHuman = false;
        try {
             isNewHuman = !(res.next());
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        try {
            res.close();
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        if (isNewHuman) {
            query = "INSERT INTO " + table
                    + "(name, surname, patronymic, sex, postcode, country, region, city, street)"
                    + "VALUES ('" + dataModel.getName() + "',"
                    + "'" + dataModel.getSurname() + "',"
                    + "'" + dataModel.getPatronymic() + "',"
                    + "'" + dataModel.getSex() + "',"
                    + "'" + dataModel.getPostcode() + "',"
                    + "'" + dataModel.getCountry() + "',"
                    + "'" + dataModel.getRegion() + "',"
                    + "'" + dataModel.getCity() + "',"
                    + "'" + dataModel.getStreet() + "')";
        } else {
            query = "UPDATE " + table + " SET "
                    + "sex='" + dataModel.getSex() + "',"
                    + "postcode='" + dataModel.getPostcode() + "',"
                    + "country='" + dataModel.getCountry() + "',"
                    + "region='" + dataModel.getRegion() + "',"
                    + "city='" + dataModel.getCity() + "',"
                    + "street='" + dataModel.getStreet()
                    + "WHERE name='" + dataModel.getName() + "' "
                    + "AND surname='" + dataModel.getSurname() + "' "
                    + "AND patronymic='" + dataModel.getPatronymic() + "'";
        }

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        try {
            statement.close();
            res.close();
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }
    }

    DataModel getRandRow() {
        Random rnd = new Random(System.currentTimeMillis());
        int numHum = 1 + rnd.nextInt(getCountRows());

        return getRow(numHum);
    }

    private DataModel getRow(int i) {
        Statement statement = null;
        ResultSet res = null;

        String query = "SELECT * FROM " + table + " WHERE humanID=" + i;

        try {
            statement = connection.createStatement();
            res = statement.executeQuery(query);
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        try {
            res.next();
            return new DataModel(
                    res.getString("name"),
                    res.getString("surname"),
                    res.getString("patronymic"),
                    res.getString("sex").charAt(0),
                    new StringBuffer(res.getString("postcode")),
                    res.getString("country"),
                    res.getString("region"),
                    res.getString("city"),
                    res.getString("street")
            );
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        try {
            statement.close();
            res.close();
        } catch (SQLException e) {
            closeAll(res, statement);
            e.printStackTrace();
        }

        return null;
    }

    boolean isEmpty() {
        return getCountRows() == 0;
    }

    boolean createConnection(){
        List<String> dataForConnection;
        try {
            dataForConnection = FileWorker.read("src/main/resources/dataForConnection.txt");
        } catch (FileNotFoundException e) {
            return false;
        }

        try {
            Class.forName(JDBCDriver);
        } catch (ClassNotFoundException e) {
            return false;
        }

        String dataBaseURL = dataForConnection.get(0)
                + "?serverTimezone=Europe/Moscow&autoReconnect=true&useSSL=false";
        String user = dataForConnection.get(1);
        String password = dataForConnection.get(2);
        try {
            connection = getConnection(dataBaseURL, user, password);
        } catch (SQLException e) {
            return false;
        }

        createTable();

        return true;
    }

    private void createTable() {
        Statement statement = null;
        //это работает
        String query = "CREATE TABLE IF NOT EXISTS " + table
                + "(humanID integer primary key auto_increment, "
                + "name varchar(100) NOT NULL, "
                + "surname varchar(100) DEFAULT NULL, "
                + "patronymic varchar(100) DEFAULT NULL, "
                + "sex varchar(1) NOT NULL, "
                + "postcode varchar(6) DEFAULT NULL, "
                + "country varchar(100) NOT NULL, "
                + "region varchar(100) DEFAULT NULL, "
                + "city varchar(100) DEFAULT NULL, "
                + "street varchar(100) NOT NULL)";

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.close();
        } catch (SQLException e) {
            try { connection.close(); } catch (SQLException er) {}
            e.printStackTrace();
        }
    }

    void close() throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        statement.executeUpdate("commit");
        statement.close();

        connection.close();

        AbandonedConnectionCleanupThread.checkedShutdown();
    }

    private void closeAll(ResultSet res, Statement statement) {
        try { res.close(); } catch (SQLException er) {}
        try { statement.close(); } catch (SQLException er) {}
        try { connection.close(); } catch (SQLException er) {}
    }
}
