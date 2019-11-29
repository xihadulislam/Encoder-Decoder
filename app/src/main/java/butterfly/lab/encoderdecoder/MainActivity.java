package butterfly.lab.encoderdecoder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adfendo.sdk.ads.AdFendo;
import com.adfendo.sdk.ads.AdFendoInterstitialAd;
import com.adfendo.sdk.ads.BannerAd;
import com.adfendo.sdk.interfaces.BannerAdListener;
import com.adfendo.sdk.interfaces.InterstitialAdListener;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editText;
    Button encode, decode;
    TextView textView;
    ImageView share, copy;

    LinearLayout linearLayout;

    private BannerAd bannerAd;
    private AdFendoInterstitialAd mAdFendoInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextID);
        encode = findViewById(R.id.encodeId);
        decode = findViewById(R.id.decodeID);
        textView = findViewById(R.id.showTextId);
        share = findViewById(R.id.shareID);
        copy = findViewById(R.id.copyId);
        linearLayout = findViewById(R.id.linID);

        encode.setOnClickListener(this);
        decode.setOnClickListener(this);
        share.setOnClickListener(this);
        copy.setOnClickListener(this);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        AdFendo.initialize("pub-app-652082353");

        bannerAd = findViewById(R.id.bannerAd);
        bannerAd = new BannerAd(this, "ad-unit-652082353~584475139");
        bannerAd.setOnBannerAdListener(new BannerAdListener() {
            @Override
            public void onRequest(boolean isSuccessful) {
                // Code to be executed when an ad is requested.
            }

            @Override
            public void onClosed() {
                // Code to be executed when an ad closed.
            }

            @Override
            public void onFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void isLoaded(boolean isLoaded) {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onImpression() {
                // Code to be executed when the ad is shown.
            }
        });


        mAdFendoInterstitialAd = new AdFendoInterstitialAd(this, "ad-unit-652082353~592959308");

        // Customize as your need
        mAdFendoInterstitialAd.setInterstitialAdListener(new InterstitialAdListener() {
            @Override
            public void onClosed() {
                // Code to be executed when an ad closed.
                mAdFendoInterstitialAd.requestAd();
            }

            @Override
            public void onFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void isLoaded(boolean isLoaded) {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onImpression() {
                // Code to be executed when the ad is shown.
            }
        });

        mAdFendoInterstitialAd.requestAd();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_privacy) {
            Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/drivesafe-ae4da.appspot.com/o/privacy_policy.html?alt=media&token=a3c6926a-94fa-42ba-8623-b955939ecfce");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {


        if (view == encode) {
            String text = editText.getText().toString().trim();
            if (!text.isEmpty()) {
                byte[] encrpt = new byte[0];
                try {
                    encrpt = text.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String base64 = Base64.encodeToString(encrpt, Base64.DEFAULT);

                linearLayout.setVisibility(View.VISIBLE);
                textView.setText(base64);

            } else {
                editText.setError("Write Something..");
            }


            if (MyApp.cnt == 1 || MyApp.cnt == 3 || MyApp.cnt == 6 || MyApp.cnt == 9 || MyApp.cnt == 12) {

                if (mAdFendoInterstitialAd.isLoaded()) {
                    mAdFendoInterstitialAd.showAd();
                    MyApp.cnt += 1;
                } else {
                    mAdFendoInterstitialAd.requestAd();

                }
            }
        }
        if (view == decode) {

            String text = "";
            String base64 = editText.getText().toString().trim();

            if (!base64.isEmpty()) {


                try {
                    byte[] decrypt = Base64.decode(base64, Base64.DEFAULT);
                    text = new String(decrypt, "UTF-8");

                    linearLayout.setVisibility(View.VISIBLE);
                    textView.setText(text);

                } catch (Exception e) {
                    e.printStackTrace();
                    editText.setError("Text can not be decoded");
                }

            } else {
                editText.setError("Write Something..");
            }


            if (MyApp.cnt == 4 || MyApp.cnt == 8 || MyApp.cnt == 10 || MyApp.cnt == 14 || MyApp.cnt == 19) {

                if (mAdFendoInterstitialAd.isLoaded()) {
                    mAdFendoInterstitialAd.showAd();
                    MyApp.cnt += 1;
                } else {
                    mAdFendoInterstitialAd.requestAd();

                }

            }

        }

        if (view == copy) {
            String text = textView.getText().toString().trim();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            linearLayout.setVisibility(View.GONE);
            textView.setText("");
            Toast.makeText(this, "Copied Text", Toast.LENGTH_SHORT).show();
        }

        if (view == share) {
            String text = textView.getText().toString().trim();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        }

    }
}

