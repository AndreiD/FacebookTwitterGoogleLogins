package androidadvance.com.facebooktwittergooglelogins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class TwitterActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "MjP9u3HjQY6F5agy9NnIGL2Jd";
    private static final String TWITTER_SECRET = "CCR5yr18o2igAJSvwjCRHbtwNcGSJbd91eNak2MUPBNFPjdI46";
    private TwitterLoginButton loginButton;
    private TwitterActivity mContext;
    private TwitterSession session;

    private String twitter_username;
    private long twitter_userId;
    private String twitter_image_url;
    private String twitter_email = "";
    private TextView textView_debug;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //they have to be above. or in a base activity class.
        mContext = TwitterActivity.this;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(mContext, new com.twitter.sdk.android.Twitter(authConfig));

        setContentView(R.layout.activity_twitter);

        textView_debug = (TextView) findViewById(R.id.textView_debug_twitter);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";

                twitter_username = session.getUserName();
                twitter_userId = session.getUserId();
                twitter_image_url = " https://twitter.com/" + twitter_username + "/profile_image?size=original";

                update_debug_ui();

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                //you will get an error, if your app doesn't have permission for the email.
                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        Log.d("EMAIL RESULT ", String.valueOf(result));
                        twitter_email = result.data;
                        update_debug_ui();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.e("EMAIL EXCEPTION ", String.valueOf(exception.getMessage()));
                        twitter_email = "not disclosed";
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


    }

    private void update_debug_ui() {
        textView_debug.setText("username: " + twitter_username + " | userId: " + String.valueOf(twitter_userId) + " | image_url: " + twitter_image_url + " | email: " + twitter_email);

        Glide.with(mContext).load(twitter_image_url).into((ImageView) findViewById(R.id.imageView_twitter));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
