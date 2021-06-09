package fzp.entity.greed;

import lombok.Data;

import java.util.Set;

@Data
public class GNode {
    public GNode(String nodeName, Set<GApplication> applications) {
        this.nodeName = nodeName;
        this.applications = applications;
    }

    public String nodeName;
    public Set<GApplication> applications;

}
