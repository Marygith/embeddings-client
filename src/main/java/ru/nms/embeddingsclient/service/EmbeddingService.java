package ru.nms.embeddingsclient.service;

import lombok.RequiredArgsConstructor;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nms.embeddingsclient.model.Embedding;
import ru.nms.embeddingslibrary.model.MasterServiceMeta;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final ZookeeperService zookeeperService;

    public ServiceInstance<MasterServiceMeta> getServiceInstance() {
        try {
            return zookeeperService.findMainServerInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Embedding getEmbedding(int embeddingId) {
        try {
            ServiceInstance<MasterServiceMeta> masterService = zookeeperService.findMainServerInstance();
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://" + masterService.getAddress() + ":" + masterService.getPort() + "/embeddings/{" + embeddingId + "}";
            ResponseEntity<Embedding> response = restTemplate.getForEntity(resourceUrl, Embedding.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
