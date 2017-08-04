package br.ufpe.cin;

import java.util.EnumSet;

import br.ufpe.cin.airconditioner.AirControlee;
import br.ufpe.cin.config.IoTivityConfigurer;
import br.ufpe.cin.model.AirConditioner;

public class Main {

	public static void main(String[] args)  throws Exception{
		System.out.println("Configuring");
		
		if(args.length != 4) {
			System.out.println("[host-ipaddress:port] [authprovider] [authcode]\" for sign-up and sign-in and publish resources");
			System.out.println("[host-ipaddress:port] [uid] [accessToken]\" for sign-in and publish resources");
		}
		
		
		String host = "coap+tcp://" + System.getProperty("host");		
		String token = System.getProperty("accessToken");
		String uid = System.getProperty("uid", null);

		IoTivityConfigurer config = new IoTivityConfigurer(host);
		config.configurePlatform();
		config.signIn(token, uid);		
		
		if(System.getProperty("startMode").equals("controlee")) {
			AirControlee airControlee = new AirConditioner();
			airControlee.createResources();
		}
		else { // controller
			
		}
	}

}
