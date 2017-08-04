package org.ufpe.cin.config;

import java.util.EnumSet;

import br.ufpe.cin.OcAccountManager;
import br.ufpe.cin.PlatformConfig;
import br.ufpe.cin.account.OcException;
import br.ufpe.cin.common.SignInManager;

import java.util.concurrent.locks;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

	public static class IoTivityConfigurer {
		
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


	public void static configurePlatform() {
		 PlatformConfig config = new PlatformConfig(
	                ServiceType.IN_PROC,
	                ModeType.BOTH,
	                "0.0.0.0",
	                0,
	                QualityOfService.LOW
	        );
		 
		 OcPlatform.Configure(config);		
	}
	
	public void static signIn(String accessToken, String uid) throws OcException{
			
		OcAccountManager accountManager = OcPlatform
									.constructAccountManagerObject(
											this.host(), 
											EnumSet.of(OcConnectivityType.CT_ADAPTER_TCP)
								                );		
        if (uid == null) {
            signInmanager = new SignInManager(manager,
                    SignInManager.Provider.GITHUB,
                    accessToken);
        } else {
            signInmanager = new SignInManager(manager, uid, accessToken);
        }
                
        signInmanager.signIn();				
	}
}