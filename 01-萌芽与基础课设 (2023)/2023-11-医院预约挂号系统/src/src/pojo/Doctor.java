package pojo;

import java.util.Arrays;

//医生表
public class Doctor {
	private int doctorID;//医生编号
	private int hospitalID;//所属医院编号
	private String hospitalName;//外键---查询对应的医院名称
	private int departmentID;//所属科室编号
	private String departmentType;//外键---查询对应的科室名称
	private String doctorName;//医生姓名
	private String title;//职称
	private String goodAt;//擅长
	private String professional;//职业经历
	private int outpatientCharge;//门诊费
	private byte[] doctorImg;//医生头像
	
	
	public String toString() {
		return "Doctor{" +
				"doctorID=" + doctorID +
				", hospitalID=" + hospitalID +
				", departmentType='" + departmentType + '\'' +
				", doctorName='" + doctorName + '\'' +
				", title='" + title + '\'' +
				", goodAt='" + goodAt + '\'' +
				", professional='" + professional + '\'' +
				", outpatientCharge=" + outpatientCharge +
				", doctorImg=" + Arrays.toString(doctorImg) +
				'}';
	}

	public Doctor(int doctorID, int hospitalID, String departmentType, String doctorName, String title, String goodAt, String professional, int outpatientCharge, byte[] doctorImg) {
		this.doctorID = doctorID;
		this.hospitalID = hospitalID;
		this.departmentType = departmentType;
		this.doctorName = doctorName;
		this.title = title;
		this.goodAt = goodAt;
		this.professional = professional;
		this.outpatientCharge = outpatientCharge;
		this.doctorImg = doctorImg;
	}
	
	public Doctor() {
		super();
	}
	
	public int getDoctorID() {
		return doctorID;
	}


	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}


	public int getHospitalID() {
		return hospitalID;
	}


	public void setHospitalID(int hospitalID) {
		this.hospitalID = hospitalID;
	}

	
	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}


	public int getDepartmentID() {
		return departmentID;
	}

	
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	
	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}
	

	public String getDoctorName() {
		return doctorName;
	}


	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getGoodAt() {
		return goodAt;
	}


	public void setGoodAt(String goodAt) {
		this.goodAt = goodAt;
	}


	public String getProfessional() {
		return professional;
	}


	public void setProfessional(String professional) {
		this.professional = professional;
	}


	public int getOutpatientCharge() {
		return outpatientCharge;
	}


	public void setOutpatientCharge(int outpatientCharge) {
		this.outpatientCharge = outpatientCharge;
	}


	public byte[] getDoctorImg() {
		return doctorImg;
	}


	public void setDoctorImg(byte[] doctorImg) {
		this.doctorImg = doctorImg;
	}
}
