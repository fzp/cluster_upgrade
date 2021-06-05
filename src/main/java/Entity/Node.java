package Entity;

import lombok.Data;

import java.util.List;

@Data
public class Node {
	public Node(String nodeName,List<Application> applications){
		this.nodeName = nodeName;
		this.applications = applications;
	}
	public String nodeName;
	public List<Application> applications;
}
