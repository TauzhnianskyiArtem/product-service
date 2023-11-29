package com.iprody.product.service.e2e.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JsonSerializationHelper {
    /**
     * Injecting bean with json parsing functionality.
     */
    private final ObjectMapper objectMapper;

    /**
     * @param object we want convert to JsonNode
     * @return object as JsonNode
     */
    public JsonNode getObjectAsNode(Object object) {
        return objectMapper.valueToTree(object);
    }

}