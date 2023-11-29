package com.iprody.product.service.e2e.config;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * Configuration class for create SSL-enabled RestTemplate bean to use in generated class.
 */
@Configuration
public class WebClientConfig {

    /**
     * keystore from resources folder.
     */
    private final Resource trustStore;

    /**
     * keystore password from `application.yml`.
     */
    private final String trustStorePassword;

    /**
     * Configurable variable to set TLS version.
     */
    private final TLS tlsVersion;

    public WebClientConfig(
            @Value("${product-service.ssl.trust-store}") Resource trustStore,
            @Value("${product-service.ssl.trust-store-password}") String trustStorePassword,
            @Value("${product-service.ssl.version}") TLS tlsVersion
    ) {
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.tlsVersion = tlsVersion;
    }

    /**
     * Create bean RestTemplate.
     *
     * @return instance RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        final CloseableHttpClient httpClient = buildSslHttpClient();
        final ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

    @SneakyThrows
    private CloseableHttpClient buildSslHttpClient() {
        final SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
        final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .build();
        final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultTlsConfig(TlsConfig.custom()
                        .setSupportedProtocols(tlsVersion)
                        .build())
                .build();
        return HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }
}