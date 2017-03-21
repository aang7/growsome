package xyz.growsome.growsome.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import xyz.growsome.growsome.R;

/**
 * Descripcion: Clase que se debe usar para realizar conexiones al servicio
 * Autor: Sergio Cruz
 * Fecha: 2016-10-17
 * Modificacion: 2017-03-12.Sergio Cruz.Funcionalidad de AsyncDone
 **/

public abstract class Connection extends AsyncTask<Void, Void, String> implements AsyncDone
{
    private Context myContext;
    private String Url = "http://www.growsome.xyz/service.php";
    private byte[] postDataBytes;

    public Connection (Context context, Map<String, Object> parametros)
    {
        this.myContext = context;
        try
        {
            this.postDataBytes = postData(parametros);
        }
        catch (Exception ex)
        {
            this.postDataBytes = null;
        }
    }

    public Connection (Context context, byte[] postDataBytes)
    {
        this.myContext = context;
        this.postDataBytes = postDataBytes;
    }

    public Connection (Context context, byte[] postDataBytes, String url)
    {
        this.myContext = context;
        this.postDataBytes = postDataBytes;
        this.Url = url;
    }

    @Override
    protected String doInBackground(Void... Params)
    {
        String result;

        // Se valida que haya internet

        ConnectivityManager connMgr = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            try
            {
                result = goConnect(this.Url);
            }
            catch (IOException ex)
            {
                return myContext.getResources().getString(R.string.bad_url);
            }
        }
        else
        {
            return myContext.getResources().getString(R.string.no_internet);
        }

        return result;

    }

    @Override
    protected void onPostExecute(final String data)
    {
        onTaskFinished(data);
    }

    public abstract void onTaskFinished(String data);

    //Genera el array de bytes para hacer el request
    protected byte[] postData(Map<String, Object> parametros) throws IOException
    {
        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String,Object> param : parametros.entrySet())
        {
            if (postData.length() != 0)
            {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        return  postDataBytes;
    }

    //Se realiza la conexion al webserver
    protected String goConnect(String myurl) throws IOException
    {
        InputStream is = null;

        try
        {
            URL url = new URL(myurl);
            byte[] postDataBytes = this.postDataBytes;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", String.valueOf(postDataBytes.length));
            conn.getOutputStream().write(postDataBytes);
            Log.d("TAG", conn.getOutputStream().toString());

            // Se inicia la conexion
            conn.connect();

            int response = conn.getResponseCode();
            Log.d("TAG", "The response is: " + response); // 200 es conexion correcta
            is = conn.getInputStream();

            // readIt convierte el input stream a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Se cierra la conexion
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    //Lee el input stream y lo regresa como string.
    protected String readIt(InputStream stream) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String input = "";
        while((input = reader.readLine()) != null)
        {
            sb.append(input);
        }
        return sb.toString();
    }
}
