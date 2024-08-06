package br.com.alura.tabelafip.service;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumoApi {

    public String obterDados(String endereco) {
        System.out.println("Realizando a chamada a API: " + endereco);
        
        RestClient restclient = RestClient.create(getRestTemplate());
        return restclient.get()
            .uri(endereco)
            .retrieve()
            .body(String.class);
    }

    private RestTemplate getRestTemplate() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.220.11", 3128));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

}
