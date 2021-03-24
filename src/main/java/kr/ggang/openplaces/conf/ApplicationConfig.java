package kr.ggang.openplaces.conf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class ApplicationConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter()
    {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(mappingJackson2HttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new ByteArrayHttpMessageConverter());
        
        rt.setMessageConverters(messageConverters);
        
        return rt;

    }

    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

//        om.registerModule(new JavaTimeModule());
        return om;
    }

    /*
    @Bean
    LayeredConnectionSocketFactor sslSocketFactory() {
        String supportedProtocols[] = new String[]{"TLSv1.2"};
        
        SSLConnectionSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
            {
               @Override
               public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
               {
                  return true;
               }
            }).build();

            sslSocketFactory = new SSLConnectionSocketFactory(
                    sslContext, supportedProtocols, null, new NoopHostnameVerifier());
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            LOG.error(e.getMessage(), e);
        }
        
        return sslSocketFactory;
    }
    
    @Bean HttpClient httpsClient() {
        CloseableHttpClient httpsClient = HttpClients.custom().setSSLSocketFactory(sslSocketFactory()).build();
        return httpsClient;
    }

    @Bean
    public ClientHttpRequestFactory requestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = 
                new HttpComponentsClientHttpRequestFactory();
        if( httpClient != null )
            requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(5*1000);

        return requestFactory;
    }
    */
    
}
