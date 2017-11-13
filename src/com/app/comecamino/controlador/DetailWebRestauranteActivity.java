package com.app.comecamino.controlador;

import com.app.comecamino.modelo.Restaurante;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class DetailWebRestauranteActivity extends Activity{
	
	private Restaurante mRestaurante;
	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        WebView webView = (WebView)findViewById(R.id.webView);
        
        //webView.setWebViewClient(new WebViewClient());
        mRestaurante= (Restaurante) getIntent().getExtras().getSerializable("restaurante");
        if(mRestaurante!=null)
        {
        	 webView.getSettings().setJavaScriptEnabled(true);
        	 webView.loadUrl(mRestaurante.getGuid());
        }
          
    }

}
