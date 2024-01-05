package ru.nms.embeddingsclient.controller;

import lombok.RequiredArgsConstructor;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.nms.embeddingsclient.model.Embedding;
import ru.nms.embeddingsclient.service.EmbeddingService;
import ru.nms.embeddingslibrary.model.MasterServiceMeta;

@RestController
@RequiredArgsConstructor
public class EmbeddingsController {

    private final EmbeddingService embeddingService;

    @GetMapping("/embedding/{id}")
    public Embedding getEmbedding(@PathVariable int id) {
        return embeddingService.getEmbedding(id);
    }

    @GetMapping("/instance")
    public ServiceInstance<MasterServiceMeta> getInstance() {
        return embeddingService.getServiceInstance();
    }
}
