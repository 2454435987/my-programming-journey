package pojo;

public class Information {
   private int ordersID;
   private String user;
   private int doctorID;
   private String date;
   private int price;
   private String state;
   private String patients;
   private String patientsCard;
   private String patientsPhone;

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getOrdersID() {
        return ordersID;
    }

    public void setOrdersID(int ordersID) {
        this.ordersID = ordersID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getPatientsCard() {
        return patientsCard;
    }

    public void setPatientsCard(String patientsCard) {
        this.patientsCard = patientsCard;
    }

    public String getPatientsPhone() {
        return patientsPhone;
    }

    public void setPatientsPhone(String patientsPhone) {
        this.patientsPhone = patientsPhone;
    }
}
