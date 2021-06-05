package Entity;

import lombok.Data;

@Data
public class Application {

	public Application(String appName, int disruptionAllowed){
		this.appName = appName;
		this.disruptionAllowed = disruptionAllowed;
	}
	public String appName;
	public Integer disruptionAllowed;
}
