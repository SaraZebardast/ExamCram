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

    public String getProfilePictureID() {
        return profilePictureID;
    }

    public void setProfilePictureID(String profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    public long getEndStudyStreakTime() {
        return endStudyStreakTime;
    }

    public void setEndStudyStreakTime(long endStudyStreakTime) {
        this.endStudyStreakTime = endStudyStreakTime;
    }

    public long getStartStudyStreakTime() {
        return startStudyStreakTime;
    }

    public void setStartStudyStreakTime(long startStudyStreakTime) {
        this.startStudyStreakTime = startStudyStreakTime;
    }

    public double getTimeStudied() {
        return timeStudied;
    }

    public void setTimeStudied(double timeStudied) {
        this.timeStudied = timeStudied;
    }

    public boolean isOnCampus() {
        return onCampus;
    }

    public void setOnCampus(boolean onCampus) {
        this.onCampus = onCampus;
    }

    public boolean isStudying() {
        return studying;
    }

    public void setStudying(boolean studying) {
        this.studying = studying;
    }

    String email, firstName, lastName, country, university, profilePictureID;
    long endStudyStreakTime, startStudyStreakTime;
    double timeStudied;
    boolean onCampus, studying;

    public UserInfo(String email, String firstName, String lastName, String country, String university) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country= country;
        this.university = university;
        profilePictureID = "0";
        startStudyStreakTime = 0;
        timeStudied = 0.0000001;
        endStudyStreakTime = 0;
        onCampus = false;
        studying = false;
    }
}
