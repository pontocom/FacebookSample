package pt.iscte_iul.daam.facebooksample;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowProfileActivity extends AppCompatActivity {
    protected TextView tvFBname, tvFBemail;
    protected ImageView ivFBProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        tvFBname = (TextView) findViewById(R.id.tvFBName);
        tvFBemail = (TextView) findViewById(R.id.tvFBEmail);
        ivFBProfilePic = (ImageView) findViewById(R.id.ivFBProfilePic);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        Log.i("FACEBOOK_SAMPLE", "AccessToken = " + accessToken.getToken());

        GraphRequest graphRequest =  GraphRequest.newGraphPathRequest(
                accessToken,
                "/me?fields=id,name,email,picture{url}",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.i("FACEBOOK_SAMPLE", "Response = " + response.toString());

                        JSONObject jsonObject = response.getJSONObject();
                        Log.i("FACEBOOK_SAMPLE", "Object = " + jsonObject.toString());

                        fillData(jsonObject);
                    }
                }
        );

        graphRequest.executeAsync();
    }

    protected void fillData(JSONObject object)
    {
        try{
            tvFBname.setText(object.getString("name"));
            tvFBemail.setText(object.getString("email"));

            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");

            Glide.with(this).load(url).into(ivFBProfilePic);

        } catch (JSONException e) {
            Log.i("FACEBOOK_SAMPLE", "Exception = " + e.getMessage());
        }
    }
}
