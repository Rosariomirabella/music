package com.example.music.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
	
	private static final String URL = "jdbc:mysql://localhost:3306/music";
	private static final String USERNAME = "root"; 
	private static final String PASSWORD = "Utsafut418";
	
	public static Connection open() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
	
}
