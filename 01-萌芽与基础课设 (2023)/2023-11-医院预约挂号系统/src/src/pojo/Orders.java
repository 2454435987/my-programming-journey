package pojo;

public class Orders {
	private int ordersID;//订单编号
	private int user;//用户账户
	private String patients;//就诊人
	private String patientsCard;//就诊人身份证号
	private String patientsPhone;//就诊人手机号
	private int doctorID;//医生编号
	private String doctorName;//外键---医生名
	private String departmentType;//外键---科室类别(名称)
	private String hospitalName;//外键---医院名
	private String date;//预约日期
	private int price;//金额
	private String state;//订单状态

	public Orders() {
		super();
	}

	public int getOrdersID() {
		return ordersID;
	}

	public void setOrdersID(int ordersID) {
		this.ordersID = ordersID;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
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

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}


	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
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
}
