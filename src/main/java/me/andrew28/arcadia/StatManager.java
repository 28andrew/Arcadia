package me.andrew28.arcadia;

import net.dv8tion.jda.core.JDA;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class StatManager {
    JDA jda;
    public StatManager(JDA jda){
        this.jda = jda;
    }

    public JDA getJDA() {
        return jda;
    }

    public Integer update(Statistics info){

        try {
            //Stackoverflow
            //http://stackoverflow.com/questions/13302245/getting-set-cookie-header
            //
            CloseableHttpClient client = null;
            SSLContext sslcontext = SSLContexts.createSystemDefault();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslcontext, new String[] { "TLSv1", "SSLv3" }, null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslConnectionSocketFactory)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            RequestConfig globalConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.DEFAULT)
                    .build();
            RequestConfig localConfig = RequestConfig.copy(globalConfig)
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .build();

            client = HttpClients.custom()
                    .setDefaultRequestConfig(globalConfig)
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setConnectionManager(cm)
                    .build();

            String url = String.format("https://bots.discord.pw/api/bots/%s/stats", Arcadia.getInstance().getJdaInstance().getSelfUser().getId());
            HttpPost post = new HttpPost(url);
            post.setConfig(localConfig);

            post.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH );

            post.setHeader("Authorization", (String) Arcadia.getInstance().getConfig().get("discordpw-auth"));
            post.setHeader("Content-Type","application/json");

            post.setEntity(new StringEntity(String.format("{\"server_count\": %s}", Arcadia.getInstance().getJdaInstance().getGuilds().size()))); //I'm not installing a JSON library
            HttpResponse response = client.execute(post);
            //
            System.out.println("bots.discord.pw RESPONSE CODE: " + response.getStatusLine().getStatusCode());
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            Arcadia.log("Something went wrong while posting stats to discord.pw");
            e.printStackTrace();
        }
        return null;
    }

    public static interface Statistics{
       int getAmountOfServers();
    }
}
