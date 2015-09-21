package tago;

import domain.DeleteDeviceResult;
import domain.FindDeviceCountResult;
import domain.FindDeviceResult;
import domain.InsertDeviceResult;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Roberto Canoff
 */
public class Init {

    String API_TAGO = "https://api.tago.io/";
    String URL = API_TAGO + "data";
    HttpHeaders headers;
    RestTemplate restTemplate;

    public Init(String token) {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Device-Token", token);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(new MappingJackson2HttpMessageConverter());
    }

    public InsertDeviceResult insert(Device device) {
        HttpEntity<Device> request = new HttpEntity<Device>(device, headers);
        return restTemplate.postForObject(URL, request, InsertDeviceResult.class);
    }

    public List<Device> find(String key, String type) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam(key, type);
        
        HttpEntity entity = new HttpEntity(headers);

        HttpEntity<FindDeviceResult> response = restTemplate
                .exchange(builder.build().encode().toUriString(),
                        HttpMethod.GET,
                        entity,
                        FindDeviceResult.class);

        return response.getBody().result;
    }
    
    public Integer count() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam(Constant.Find.QUERY, Constant.Query.COUNT);
        
        HttpEntity entity = new HttpEntity(headers);
        
        HttpEntity<FindDeviceCountResult> response = restTemplate
                .exchange(builder.build().encode().toUriString(),
                        HttpMethod.GET,
                        entity,
                        FindDeviceCountResult.class);

        return response.getBody().result;
    }

    public Boolean delete() {
        return deleteDevice(null);
    }
    
    public Boolean delete(String data_ID){
        return deleteDevice(data_ID);
    }
    
    private Boolean deleteDevice(String data_ID){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
        
        if (data_ID != null) {
            builder.queryParam("data_ID", data_ID);
        }

        HttpEntity entity = new HttpEntity(headers);

        HttpEntity<DeleteDeviceResult> response = restTemplate
                .exchange(builder.build().encode().toUriString(),
                        HttpMethod.DELETE,
                        entity,
                        DeleteDeviceResult.class);

        return response.getBody().status;
    }
    
    public InsertDeviceResult update(Device device) throws Exception{
        throw new Exception("Not Implemented yet");
    }

    public InsertDeviceResult listening(Device device) throws Exception {
        throw new Exception("Not Implemented yet");
    }
}
