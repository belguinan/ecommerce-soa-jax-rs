package com.example.productservice.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class FilterRequest {
    private String type;
    private String sellerId;

    /**
     * @return Map<String, String>
     */
    public Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<>();

        if (this.type != null) {
            filters.put("type", this.type);
        }

        if (this.sellerId != null) {
            filters.put("sellerId", this.sellerId);
        }

        return filters;
    }
}