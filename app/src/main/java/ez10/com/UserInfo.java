package ez10.com;

public class UserInfo {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    String email, firstName, lastName, country, university;

    public UserInfo(String email, String firstName, String lastName, String country, String university) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country= country;
        this.university = university;
    }
}
