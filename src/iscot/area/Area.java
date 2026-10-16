package iscot.area;

import java.util.ArrayList;
import java.util.List;

import iscot.server.DealWithClient;

public class Area {
	public final String name;
	private List<DealWithClient> clients= new ArrayList<>();
	public Area(String name) {
		super();
		this.name = name;
	}
	public List<DealWithClient> getClients() {
		return clients;
	}
	
}
