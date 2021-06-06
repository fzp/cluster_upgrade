package entity.greed;

import entity.INode;
import lombok.Data;

import java.util.Set;

@Data
public class GNode implements INode {
	public GNode(String nodeName, Set<GApplication> applications){
		this.nodeName = nodeName;
		this.applications = applications;
	}
	public String nodeName;
	public Set<GApplication> applications;
}
