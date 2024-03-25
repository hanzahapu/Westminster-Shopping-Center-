// // Class User
public class  User {
    String userName;
    String pw;


    // Parameterized Constructor
    public User(String userName, String pw) {
        this.userName = userName;
        this.pw = pw;
    }


    // Getters and Setters
    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPw() {
        return pw;
    }


    public void setPw(String pw) {
        this.pw = pw;
    }
}
