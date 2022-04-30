package ez10.com;

import java.util.ArrayList;

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

    public String getProfilePictureID() { return profilePictureID; }

    public void setProfilePictureID(String profilePictureID) { this.profilePictureID = profilePictureID; }

    public String[] getUserCourses() { return userCourses; }

    public void setUserCourses(String[] userCourses) { this.userCourses = userCourses; }

    public ArrayList<String> getFriends() { return friends; }

    public void setFriends(ArrayList<String> friends) { this.friends = friends; }

    public String getEndStudyStreakTime() { return endStudyStreakTime; }

    public void setEndStudyStreakTime(String endStudyStreakTime) { this.endStudyStreakTime = endStudyStreakTime; }

    public String getStartStudyStreakTime() { return startStudyStreakTime; }

    public void setStartStudyStreakTime(String startStudyStreakTime) { this.startStudyStreakTime = startStudyStreakTime; }

    public String getTimeStudied() { return timeStudied; }

    public void setTimeStudied(String timeStudied) { this.timeStudied = timeStudied; }

    public boolean isOnCampus() { return onCampus; }

    public void setOnCampus(boolean onCampus) { this.onCampus = onCampus; }

    public boolean isStudying() { return studying; }

    public void setStudying(boolean studying) { this.studying = studying; }

    String email, firstName, lastName, country, university, profilePictureID;
    String[] userCourses;
    ArrayList<String> friends;
    String endStudyStreakTime, startStudyStreakTime, timeStudied;
    boolean onCampus, studying;

    public UserInfo(String email, String firstName, String lastName, String country, String university) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country= country;
        this.university = university;
        profilePictureID = "0";
        userCourses = new String[5];
        userCourses[0] = "-";
        userCourses[1] = "-";
        userCourses[2] = "-";
        userCourses[3] = "-";
        userCourses[4] = "-";
        startStudyStreakTime = "00:00";
        timeStudied = "00:00";
        endStudyStreakTime = "00:00";
        onCampus = false;
        studying = false;
        friends.add("-");
    }
}
