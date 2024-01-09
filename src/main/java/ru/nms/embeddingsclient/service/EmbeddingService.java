package ru.nms.embeddingsclient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nms.embeddingslibrary.model.Embedding;
import ru.nms.embeddingslibrary.model.MasterServiceMeta;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {

    private final ZookeeperService zookeeperService;

    private final RestTemplate restTemplate = new RestTemplate();

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
            log.info("Was chosen " + masterService.getPayload().getName());
            String resourceUrl = "http://" + masterService.getAddress() + ":" + masterService.getPort() + "/embeddings/" + embeddingId;
            ResponseEntity<Embedding> response = restTemplate.getForEntity(resourceUrl, Embedding.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
