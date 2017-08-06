package br.ufpe.cin;

import java.io.Console;
import java.util.EnumSet;
import java.util.List;

import org.iotivity.base.OcConnectivityType;
import org.iotivity.base.OcException;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcRDClient;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResourceHandle;

import br.ufpe.cin.airconditioner.AirControlee;
import br.ufpe.cin.airconditioner.AirController;
import br.ufpe.cin.config.IoTivityConfigurer;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("Configuring");

		if (args.length != 4) {
			System.out.println(
					"[host-ipaddress:port] [authprovider] [authcode]\" for sign-up and sign-in and publish resources");
			System.out.println("[host-ipaddress:port] [uid] [accessToken]\" for sign-in and publish resources");
		}

		String host = "coap+tcp://" + System.getProperty("host");
		String token = System.getProperty("accessToken");
		String uid = System.getProperty("uid", null);

		IoTivityConfigurer config = new IoTivityConfigurer(host);
		config.configurePlatform();
		config.signIn(token, uid);

		if (System.getProperty("startMode").equals("controlee")) {
			AirControlee airControlee = new AirControlee();
			airControlee.createResources();
			List<OcResourceHandle> resourceHandles = airControlee.getResourceHandles();

			// publish resources to cloud

			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						OcRDClient.publishResourceToRD(host, EnumSet.of(OcConnectivityType.CT_ADAPTER_TCP),
								resourceHandles, new OcRDClient.OnPublishResourceListener() {
									@Override
									public void onPublishResourceCompleted(OcRepresentation arg0) {
										System.out.println("Resources published");
									}

									@Override
									public void onPublishResourceFailed(Throwable arg0) {
									}
								});
					} catch (OcException e) {
						System.out.println("Something went wrong on publishing resources");
						e.printStackTrace();
					}
				}
			});
			t.start();

			startControleeMenu(airControlee);

		} else { // controller
			AirController airController = new AirController(host);
			System.out.println("Trying to find air conditioner");
			airController.findAirConditioner(host);
			startControllerMenu(airController);
		}
	}

	private static void startControleeMenu(AirControlee airControlee) throws OcException {
		while (true) {
			Console console = System.console();
			System.out.println("Press 1/0 to turn on/off air conditioner for observe testing, q to terminate");
			String option = console.readLine();
			switch (option) {
			case "1":
				airControlee.setBinarySwitchRep(true);
				break;
			case "0":
				airControlee.setBinarySwitchRep(false);
				break;
			case "q":
				System.out.println("Terminating...");
				System.exit(0);
				break;
			default:
				System.out.println("Wrong option");
				break;
			}
		}
	}

	private static void startControllerMenu(AirController airController) throws OcException {
			while (true) {
				Console console = System.console();
				System.out.println("Press 1/0 to turn on/off air conditioner, q to terminate");
				String option = console.readLine();
				switch (option) {
				case "1":
					airController.turnOnOff(true);
					break;
				case "0":
					airController.turnOnOff(false);
					break;
				case "q":
					System.out.println("Terminating...");
					System.exit(0);
					break;
				default:
					System.out.println("Wrong option");
					break;
				}
			}
	}
}
