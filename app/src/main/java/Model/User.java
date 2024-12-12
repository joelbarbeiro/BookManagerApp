package Model;

public class User {
    private String email, password, token , message, status;

    public User(String email, String password, String token, String message, String status) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.message = message;
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
