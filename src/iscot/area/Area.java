package iscot.area;

import java.util.ArrayList;
import java.util.List;

import iscot.server.DealWithClient;

public class Area {
	public final String name;
	public final int id;
	private List<DealWithClient> subscriptions= new ArrayList<>();
	public Area(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}
	
}
