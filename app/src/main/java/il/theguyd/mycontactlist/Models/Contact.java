package il.theguyd.mycontactlist.Models;




public class Contact {
    private int id;


    private String firstName;
    private String lastName;

    private String fullName;
    private String gender;
    private String email;
    private String telephone;



    //use for search contact
    public Contact(String firstName, String lastName, String email, String telephone, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.fullName = firstName +" "+lastName;
        this.gender = gender;
    }



    //use for search all contacts
    public Contact(String firstName, String lastName, String email, String telephone, String gender, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.fullName = firstName +" "+lastName;
        this.id = id;
    }


    @Override
    public String toString() {
        return "contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
