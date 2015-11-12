package androidadvance.com.facebooktwittergooglelogins;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class FacebookActivity extends AppCompatActivity {

    private FacebookActivity mContext;
    private CallbackManager callbackmanager;
    private LoginButton loginButton;
    private TextView textView_debug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = FacebookActivity.this;
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_facebook);
        callbackmanager = CallbackManager.Factory.create();

        show_facebook_keyhash();


        textView_debug = (TextView) findViewById(R.id.textView_debug_facebook);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));

        loginButton.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                update_ui("login successfully");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            update_ui("error");

                        } else {
                            String jsonresult = String.valueOf(json);
                            System.out.println("JSON Result" + jsonresult);

                            try {
                                Glide.with(mContext).load("https://graph.facebook.com/"+json.getString("id")+"/picture?type=normal").into((ImageView) findViewById(R.id.imageView_fb_profile));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            update_ui(jsonresult);
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                update_ui("CANCELED");
            }

            @Override
            public void onError(FacebookException exception) {
                update_ui("ERROR");
            }
        });


    }


    private void update_ui(String message) {
        textView_debug.setText(message);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);

    }


    //------- simple, without keytool.
    private void show_facebook_keyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("androidadvance.com.facebooktwittergooglelogins", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Facebook KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

}
