package com.datarelay.core.service.rest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.stereotype.Service;

import com.datarelay.core.entity.DataStream;
import com.datarelay.core.entity.Feature;
import com.datarelay.core.repository.mongo.DataRepository;
import com.datarelay.core.repository.sql.FeatureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatasetServiceImpl implements DatasetService {
    private final FeatureRepository featureRepository;
    private final DataRepository dataRepository;
    
    @Override
    public Flux<DataBuffer> streamDataset(UUID schemaId, DataBufferFactory bufferFactory) {
        return featureRepository.findBySchemaId(schemaId)
            .map(Feature::getName)
            .collectList()
            .flatMapMany(featureNames -> {
                String header = featureNames.stream()
                .map(this::escapeCsv)
                .collect(Collectors.joining(",")) + "\n";

                DataBuffer headBuffer = bufferFactory.wrap(header.getBytes(StandardCharsets.UTF_8));

                Flux<DataBuffer> rows = dataRepository.getData(schemaId.toString())
                    .map(data -> toCsvRow(data, featureNames))
                    .map(rowStr -> bufferFactory.wrap(rowStr.getBytes(StandardCharsets.UTF_8)));

                return Flux.just(headBuffer).concatWith(rows);
            });
    }

    private String toCsvRow(DataStream stream, List<String> featureNames) {
        Map<String, Object> data = stream.getData();
        StringBuilder row = new StringBuilder();

        for (int i = 0; i < featureNames.size(); i++) {
            String feature = featureNames.get(i);
            Object value = (data != null) ? data.get(feature) : null;

            if (value != null) {
                row.append(escapeCsv(value.toString()));
            }

            if (i < featureNames.size() - 1) {
                row.append(",");
            } else {
                row.append("\n");
            }
        }

        return row.toString();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
