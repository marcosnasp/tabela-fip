package br.com.alura.tabelafip.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@Service
public class ConversorDados implements IConversorDados {
    private static final Logger LOGGER = Logger.getLogger(ConversorDados.class.getName());

    private ObjectMapper objectMapper;

    public ConversorDados(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return objectMapper.readValue(json, classe);
        } catch (JsonMappingException e) {
            LOGGER.info(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public <T> List<T> obterLista(String json, Class<T> classe) {
        CollectionType lista = objectMapper
                .getTypeFactory().constructCollectionType(List.class, classe);
        try {
            return objectMapper.readValue(json, lista);
        } catch (JsonMappingException e) {
            LOGGER.info(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.info(e.getMessage());
        }
        return null;
    }

}
