package br.com.alura.tabelafip.service;

import java.util.List;

public interface IConversorDados {

    <T> T obterDados(String json, Class<T> classe);
    <T> List<T> obterLista(String json, Class<T> classe);

}
