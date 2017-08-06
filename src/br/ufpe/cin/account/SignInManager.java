package br.ufpe.cin.account;

import java.util.List;
import java.util.Map;

import org.iotivity.base.OcAccountManager;
import org.iotivity.base.OcException;
import org.iotivity.base.OcHeaderOption;
import org.iotivity.base.OcRepresentation;
/**
 * Created on 31/07/17.
 */
public class SignInManager implements OcAccountManager.OnPostListener{

    private String mAccessToken;
    private String mProvider;
    private String mUuid;
    private OcAccountManager mManager;
    private boolean mWorking;

    public SignInManager(OcAccountManager manager, String accessToken, String uuid) {
        this.mManager = manager;
        this.mAccessToken = accessToken;
        this.mUuid = uuid;
    }

    public SignInManager(OcAccountManager manager, Provider provider, String accessToken) {
        this.mManager = manager;
        this.mProvider = provider.toString().toLowerCase();
        this.mAccessToken = accessToken;

    }

    public void signIn() throws OcException {
        if (mUuid != null) {
            this.mWorking = true;
            this.mManager.signIn(this.mUuid, this.mAccessToken, this);
            waitUntilUnlocked();
        } else {
            this.mWorking = true;
            this.mManager.signUp(this.mProvider, this.mAccessToken, this);
            waitUntilUnlocked();
            this.mWorking = true;
            this.mManager.signIn(this.mUuid, this.mAccessToken, this);
            waitUntilUnlocked();
        }
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getUuid() {
        return mUuid;
    }

    private void waitUntilUnlocked() {
        while(mWorking) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //nop
            }
        }
    }

    public void onPostCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
        try {
            Map<String, Object> values = ocRepresentation.getValues();
            if (values.containsKey("accesstoken")) {
                this.mAccessToken = ocRepresentation.getValue("accesstoken");
                System.out.println("AccessToken: "+mAccessToken);
            }
            if (values.containsKey("uid")) {
                this.mUuid = ocRepresentation.getValue("uid");
                System.out.println("Uid: "+mUuid);
            }
        } catch (Exception e) {
            e.printStackTrace();	
        }
        mWorking = false;
    }

    public void onPostFailed(Throwable throwable) {
        throwable.printStackTrace();
        System.exit(1);
    }

    public enum Provider {
        GITHUB,
        GOOGLE
    }
}
