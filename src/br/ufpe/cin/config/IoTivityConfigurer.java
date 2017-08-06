package br.ufpe.cin.config;

import java.util.EnumSet;

import org.iotivity.base.ModeType;
import org.iotivity.base.OcAccountManager;
import org.iotivity.base.OcConnectivityType;
import org.iotivity.base.OcException;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.PlatformConfig;
import org.iotivity.base.QualityOfService;
import org.iotivity.base.ServiceType;

import br.ufpe.cin.account.SignInManager;

	public class IoTivityConfigurer {
	
		private SignInManager mSignInManager;		
		private String mUid;
		private String mAccessToken;
		private String mHost;
		
		public IoTivityConfigurer(String host) {
			this.mHost = host; 
		}
		

	public String getmUid() {
			return mUid;
		}


		public void setmUid(String mUid) {
			this.mUid = mUid;
		}


		public String getmAccessToken() {
			return mAccessToken;
		}


		public void setmAccessToken(String mAccessToken) {
			this.mAccessToken = mAccessToken;
		}


		public String getmHost() {
			return mHost;
		}


		public void setmHost(String mHost) {
			this.mHost = mHost;
		}

		

	public SignInManager getmSignInManager() {
			return mSignInManager;
		}


		public void setmSignInManager(SignInManager mSignInManager) {
			this.mSignInManager = mSignInManager;
		}


	public void configurePlatform() {
		 PlatformConfig config = new PlatformConfig(
	                ServiceType.IN_PROC,
	                ModeType.CLIENT_SERVER,
	                "0.0.0.0",
	                0,
	                QualityOfService.LOW
	        );
		 
		 OcPlatform.Configure(config);		
	}
	
	public void signIn(String accessToken, String uid) throws OcException{
			
		OcAccountManager accountManager = OcPlatform
									.constructAccountManagerObject(
											this.mHost, 
											EnumSet.of(OcConnectivityType.CT_ADAPTER_TCP)
								                );		
        if (uid == null) {
        	mSignInManager = new SignInManager(accountManager,
                    SignInManager.Provider.GITHUB,
                    accessToken);
        } else {
        	mSignInManager = new SignInManager(accountManager, uid, accessToken);
        }
                
        mSignInManager.signIn();				
	}
}