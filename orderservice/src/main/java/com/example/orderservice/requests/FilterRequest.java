package com.example.orderservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

    private String userId;

    /**
     * @return Map<String, String>
     */
    public Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<>();

        if (this.userId != null) {
            filters.put("userId", this.userId);
        }

        return filters;
    }
}