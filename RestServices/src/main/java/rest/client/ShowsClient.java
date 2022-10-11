package rest.client;

import app.model.Show;
import app.services.rest.ServiceException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class ShowsClient {
    public static final String URL = "http://localhost:8080/app/shows";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable){
        try{
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e){
            throw new ServiceException(e);
        } catch (Exception e){
            throw new ServiceException(e);
        }
    }

    public List<Show> getAll(){
        return Arrays.asList(execute(()->restTemplate.getForObject(URL, Show[].class)));
    }

    public Show getById(Long id){
        return execute(()->restTemplate.getForObject(String.format("%s/%s", URL, id),Show.class));
    }

    public Show create(Show show){
        return execute(()->restTemplate.postForObject(URL, show, Show.class));
    }

    public void update(Show show){
        execute(()->{
           restTemplate.put(URL, show);
           return null;
        });
    }

    public void delete(Long id){
        execute(()->{
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
