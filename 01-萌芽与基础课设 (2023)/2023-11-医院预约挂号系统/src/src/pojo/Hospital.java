package pojo;

import java.util.Arrays;

//医院表
public class Hospital {
	private int hospitalID;//医院编号
	private String hospitalName;//医院名称
	private String hospitalGrade;//医院等级
	private String hospitalType;//医院类型
	private String hospitalAddress;//医院地址
	private String hospitalIntro;//医院简介
	private byte[] hospitalImg;//医院图片
	
	private String hosSrc;//医院图片保存路径的字段
	private int doctorCount;//外键---查询医院里所有医生的个数
	
	public Hospital() {
		super();
	}
	
	public Hospital(int hospitalID, String hospitalName, String hospitalGrade, String hospitalType, String hospitalAddress, String hospitalIntro, byte[] hospitalImg) {
		this.hospitalID = hospitalID;
		this.hospitalName = hospitalName;
		this.hospitalGrade = hospitalGrade;
		this.hospitalType = hospitalType;
		this.hospitalAddress = hospitalAddress;
		this.hospitalIntro = hospitalIntro;
		this.hospitalImg = hospitalImg;
	}
	
	public String toString() {
		return "Hospital{" +
				"hospitalID=" + hospitalID +
				", hospitalName='" + hospitalName + '\'' +
				", hospitalGrade='" + hospitalGrade + '\'' +
				", hospitalType='" + hospitalType + '\'' +
				", hospitalAddress='" + hospitalAddress + '\'' +
				", hospitalIntro='" + hospitalIntro + '\'' +
				", hospitalImg=" + Arrays.toString(hospitalImg) +
				'}';
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


	public String getHospitalGrade() {
		return hospitalGrade;
	}


	public void setHospitalGrade(String hospitalGrade) {
		this.hospitalGrade = hospitalGrade;
	}
	
	
	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}


	public String getHospitalAddress() {
		return hospitalAddress;
	}


	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}


	public String getHospitalIntro() {
		return hospitalIntro;
	}


	public void setHospitalIntro(String hospitalIntro) {
		this.hospitalIntro = hospitalIntro;
	}


	public byte[] getHospitalImg() {
		return hospitalImg;
	}


	public void setHospitalImg(byte[] hospitalImg) {
		this.hospitalImg = hospitalImg;
	}


	public String getHosSrc() {
		return hosSrc;
	}


	public void setHosSrc(String hosSrc) {
		this.hosSrc = hosSrc;
	}
	
	public int getDoctorCount() {
		return doctorCount;
	}

	public void setDoctorCount(int doctorCount) {
		this.doctorCount = doctorCount;
	}
}
