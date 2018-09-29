package test;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import test.Rule;

class Firewall{
	
	// Class variable filePath which will be used through out the methods of this class
	String filePath;
	List<Rule> listOfRules;
	
	Firewall(String path){
		filePath = path;
		listOfRules = new ArrayList<>();
	}
	
	private void addRule(String direction, String protocol, String port, String ip) {
		Rule newRule = new Rule(direction, protocol, port, ip);
		listOfRules.add(newRule);
	}
	
	// Reads the CSV file from location line by line and processes whether to allow packet or reject
	private void readFile() throws IOException {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String current;
			while((current=br.readLine())!=null) {
				String arr[] = current.split(",");
				addRule(arr[0], arr[1], arr[2], arr[3]);
//				System.out.println("Adding rule: "+arr[0]+" "+arr[1]+" "+arr[2]+" "+arr[3]);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private long convertIpToLong(String ip) {
		
		long ans = 0;
		try {
			byte octets[] = InetAddress.getByName(ip).getAddress();
			for (byte octet : octets) {
	            ans <<= 8;
	            ans |= octet & 0xff;
	        }
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
	
	private boolean compareIp(String startIp, String endIp, String testIp) {
		
		if(convertIpToLong(testIp) >= convertIpToLong(startIp) && convertIpToLong(testIp) <= convertIpToLong(endIp)) {
			return true;
		}
		return false;
		
	}
	
	private boolean verifyAgainstRule(Rule r, String direction, String protocol, int port, String ip) {
		
		if(r.direction.equals(direction) && r.protocol.equals(protocol)) {
			if(r.port.contains("-")) {
				String ports[] = r.port.split("-");
				int startPort = Integer.parseInt(ports[0]);
				int endPort = Integer.parseInt(ports[1]);
				if(port < startPort || port > endPort) {
					return false;
				}
			}
			else {
				int rulePort = Integer.parseInt(r.port);
				if(rulePort !=  port) {
					return false;
				}
			}
			if(r.ip.contains("-")) {
				String ips[] = r.ip.split("-");
				String startIp = ips[0];
				String endIp = ips[1];
				if(!compareIp(startIp, endIp, ip)) {
					return false;
				}
			}
			else {
				if(convertIpToLong(r.ip) != convertIpToLong(ip)) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public boolean accept_packet(String direction, String protocol, int port, String ip) {
		for(int i=0;i<listOfRules.size();i++) {
			Rule currentRule = listOfRules.get(i);
			if(verifyAgainstRule(currentRule, direction, protocol, port, ip)) {
				return true;
			}
		}
		if(listOfRules.size()==0) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		Firewall fw = new Firewall("/Users/abhaysoni/Abhay/Illumio/Project/test/rules.csv");
		
		try {
			fw.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(fw.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
//		System.out.println(fw.accept_packet("inbound", "udp", 53, "192.168.2.1"));
		System.out.println(fw.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
		System.out.println(fw.accept_packet("outbound", "udp", 10134, "192.168.10.11"));

//		System.out.println(fw.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
//		System.out.println(fw.accept_packet("inbound", "udp", 24, "52.12.48.92"));
		
		
	}
	

}
