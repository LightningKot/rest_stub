package com.example.dao;


import com.example.model.User;

import java.sql.*;
import java.util.Optional;

public class DataBaseWorker {

    static final String USER = "qwe";
    static final String PASSWORD = "qwe123";
    static final String DATABASE_URL = "jdbc:postgresql://192.168.0.100:5432/testdb";
    static final String DB_DRIVER_POSTGRES = "org.postgresql.Driver";

    private static Connection connection;

    public static void test_connection() throws SQLException {
        try {
            //Class.forName("org.postgresql.Driver");
            Class.forName(DB_DRIVER_POSTGRES); //Проверяем наличие JDBC драйвера для работы с БД
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);//соединение с БД
            System.out.println("Соединение с СУБД выполнено");
            connection.close();       // отключение от БД
            System.out.println("Отключение от СУБД выполнено");
        }
        catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
        catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");
        }
    }

    public static void createTable() {

        String createTableAuth = """
            CREATE TABLE IF NOT EXISTS authentication_data (
                login VARCHAR(50) PRIMARY KEY,
                password VARCHAR(256) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        String createTableProfile = """
            CREATE TABLE IF NOT EXISTS users_profile (
                login VARCHAR(50) PRIMARY KEY REFERENCES authentication_data(login) ON DELETE CASCADE,
                email VARCHAR(256) NOT NULL
            )
            """;



        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createTableAuth);
            System.out.println("Таблица успешно создана или уже существует");
            stmt.executeUpdate(createTableProfile);

        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка: " + e.getMessage());
        }
    }

    public static void createProcedure() {
        String url = DATABASE_URL+ "?preferQueryMode=simple";
        String user = "qwe";
        String password = "qwe123";


        String procFillTables = """
                CREATE OR REPLACE PROCEDURE fill_profile_auth(count INT DEFAULT 1000)
                LANGUAGE plpgsql
                AS $$
                DECLARE
                    i INT;
                    login_text VARCHAR(50);
                BEGIN
                    FOR i IN 1..count LOOP
                        login_text := 'user_' || i;
                
                        -- Вставляем данные аутентификации
                        INSERT INTO authentication_data (login, password, created_at)
                        VALUES (
                            login_text,
                            'hashed_' || MD5(login_text || 'salt'),
                            NOW() - (random() * INTERVAL '365 days')
                        )
                        ON CONFLICT (login) DO NOTHING;
                
                        -- Вставляем профиль
                        INSERT INTO users_profile (login, email)
                        VALUES (
                            login_text,
                            login_text || '@' || (CASE floor(random() * 4)\s
                                WHEN 0 THEN 'gmail.com'
                                WHEN 1 THEN 'yahoo.com'\s
                                WHEN 2 THEN 'outlook.com'
                                ELSE 'example.com'
                            END)
                        )
                        ON CONFLICT (login) DO NOTHING;
                
                
                    END LOOP;
                END;
                $$;
            """;


        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute(procFillTables);
            System.out.println("Процедура успешно создана");


        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка: " + e.getMessage());
        }
    }

    public static void execProcedure() {
        String url = DATABASE_URL+ "?escapeSyntaxCallMode=callIfNoReturn";

        try (Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             CallableStatement procStmt = conn.prepareCall("{call fill_profile_auth(?)}")) {
            procStmt.setInt(1, 100);
            procStmt.execute();
            System.out.println("Процедура выполнена");

        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка: " + e.getMessage());
        }
    }

    //[2] insert
    public static int insertUser(User user) {
        // Используем CTE для атомарной вставки в обе таблицы
        String sql = """
            WITH inserted_auth AS (
                INSERT INTO authentication_data (login, password)
                VALUES (?, ?)
                ON CONFLICT (login) DO UPDATE SET password = EXCLUDED.password
                RETURNING login
            )
            INSERT INTO users_profile (login, email)
            SELECT login, ?
            FROM inserted_auth
            ON CONFLICT (login) DO UPDATE SET email = EXCLUDED.email
            """;

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Устанавливаем параметры
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPass());
            pstmt.setString(3, user.getEmail());

            // Выполняем и получаем количество затронутых строк
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected;

        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при вставке пользователя: " + e.getMessage());
            return 0;
        }
    }

    //[1] select
    public static Optional<User> findUserByLogin(String login) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            stmt = conn.createStatement();

            String sql = "SELECT a.login, a.password, p.email " +
                    "FROM authentication_data a " +
                    "JOIN users_profile p ON a.login = p.login " +
                    "WHERE a.login = '" + login + "'";

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                User user = new User();
                user.setLogin(rs.getString("login"));
                user.setPass(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                return Optional.of(user);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске пользователя: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ResultSet: " + e.getMessage());
            }

            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии Statement: " + e.getMessage());
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии Connection: " + e.getMessage());
            }
        }

        return Optional.empty();
    }

}
