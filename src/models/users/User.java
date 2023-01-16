package models.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import factories.users.AdminFactory;
import factories.users.CustomerFactory;
import factories.users.RegisteredUserFactory;

public class User {
	private String name;
	private String email;
	private String password;
	private String role;
	private DatabaseConnection db = DatabaseConnection.getInstance();
	
	public User() {}
	
	protected User(String role) {
		super();
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void displayInformation() {
		System.out.println("Name: " + getName());
		System.out.println("Email: " + getEmail());
		System.out.println("Role: " + getRole());
	}
	
	public User map(ResultSet rs) {
		RegisteredUserFactory factory = null;
		User user = null;
		
		try {
			if (rs.getBoolean("is_admin")) {
				factory = new AdminFactory();
			} else {
				factory = new CustomerFactory();
			}
			
			user = (User) factory.getUser();
			
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			
			if (user.getRole().equalsIgnoreCase("customer")) {
				((Customer) user).setPhone(rs.getString("phone"));
				((Customer) user).addPoints(rs.getInt("points"));				
			}
		} catch (SQLException e) {}

		return user;
	}
	
	public User authenticate(String email, String password) {
		PreparedStatement st = db.prepareStatement("select * from users where email = ? and password = ?");
		User user = null;
		
		try {
			st.setString(1, email);
			st.setString(2, password);
			ResultSet results = st.executeQuery();
			
			if (results.first()) {
				user = map(results);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;	
	}
}
