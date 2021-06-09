package fzp.entity.greed;

import lombok.Data;

@Data
public class GApplication {

	public GApplication(String appName, int disruptionAllowed){
		this.appName = appName;
		this.disruptionAllowed = disruptionAllowed;
	}
	public String appName;
	public Integer disruptionAllowed;
}
