package pojo;

//用户表
public class User {
	private String user;//账号
	private String pwd;//密码
	private String name;//姓名
	private String gender;//性别
	private String identity;//身份证号
	private String mobile;//手机号
	private byte[] userTx;//头像
	private String TxSrc;//这个有点特殊，在其他类中用于给头像保存路径使用
	
	public User() {
		super();
	}

	//用户登录
	public User(String user, String pwd) {
		super();
		this.user = user;
		this.pwd = pwd;
	}

	//用户注册
	public User(String user, String pwd, String name, String gender, String identity, String mobile) {
		super();
		this.user = user;//用户名
		this.pwd = pwd;//密码
		this.name = name;//姓名
		this.gender = gender;//性别
		this.identity = identity;//身份证号
		this.mobile = mobile;//手机号
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public byte[] getUserTx() {
		return userTx;
	}

	public void setUserTx(byte[] userTx) {
		this.userTx = userTx;
	}

	public String getTxSrc() {
		return TxSrc;
	}

	public void setTxSrc(String txSrc) {
		TxSrc = txSrc;
	}
}
