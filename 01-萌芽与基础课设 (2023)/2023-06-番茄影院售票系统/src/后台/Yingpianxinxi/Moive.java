package Yingpianxinxi;

public class Moive {
	private int id;
	private String name;//名字
	private double hot;//票房
	private String director;//导演
	private String actorid;//演员
	private String startdate;//起始日期
	private String enddate;//结束日期
	private int Movieduration;//电影时长
	private double fare;//票价
	private String Moviegenre;//类型
	private String country;//国家
	private String synopsis;//简介
	private String score;//评分
	
	public Moive() {
		super();
		// TODO 自动生成的构造函数存根
	}
	public Moive(int id, String name, double hot, String director, String actorid, String startdate, String enddate,
			int movieduration, double fare, String moviegenre, String country, String synopsis, String score) {
		super();
		this.id = id;
		this.name = name;
		this.hot = hot;
		this.director = director;
		this.actorid = actorid;
		this.startdate = startdate;
		this.enddate = enddate;
		Movieduration = movieduration;
		this.fare = fare;
		Moviegenre = moviegenre;
		this.country = country;
		this.synopsis = synopsis;
		this.score = score;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getHot() {
		return hot;
	}
	public void setHot(double hot) {
		this.hot = hot;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getActorid() {
		return actorid;
	}
	public void setActorid(String actorid) {
		this.actorid = actorid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public int getMovieduration() {
		return Movieduration;
	}
	public void setMovieduration(int movieduration) {
		Movieduration = movieduration;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public String getMoviegenre() {
		return Moviegenre;
	}
	public void setMoviegenre(String moviegenre) {
		Moviegenre = moviegenre;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
}
