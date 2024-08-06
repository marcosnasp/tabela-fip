package br.com.alura.tabelafip.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.alura.tabelafip.config.ApiConfig;
import br.com.alura.tabelafip.model.Modelo;
import br.com.alura.tabelafip.model.Modelos;
import br.com.alura.tabelafip.model.Veiculo;
import br.com.alura.tabelafip.service.ConsumoApi;
import br.com.alura.tabelafip.service.IConversorDados;

@Service
public class Principal {

    private Scanner scanner = new Scanner(System.in);

    private ApiConfig apiConfig;
    private ConsumoApi consumoApi;
    private IConversorDados conversorDados;

    public Principal(ApiConfig apiConfig, ConsumoApi consumoApi, IConversorDados conversorDados) {
        this.apiConfig = apiConfig;
        this.consumoApi = consumoApi;
        this.conversorDados = conversorDados;
    }

    public enum OpcaoBuscaFipe {
        CARROS(1, "carros"),
        MOTOS(2, "motos"),
        CAMINHAO(3, "caminhão");

        private final Integer opcao;
        private final String descricao;

        private OpcaoBuscaFipe(Integer opcao, String descricao) {
            this.opcao = opcao;
            this.descricao = descricao;
        }

        public Integer getOpcao() {
            return opcao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public String exibeMenu() {
        var menu = """
                OPÇÕES (digite o número relativo a opção):
                1 - carros
                2 - motos
                3 - caminhão

                Digite o número de uma das opções acima, para consultar:
                """;

        System.out.println(menu);
        
        String endereco = null;
        try {
            final Integer opcaoSelecionada = Integer.parseInt(scanner.nextLine());
            final int carros = 1;
            final int motos = 2;
            final int caminhao = 3;
    
            switch (opcaoSelecionada) {
                case carros -> endereco = apiConfig.getUrl() + "/" + OpcaoBuscaFipe.CARROS.getDescricao() + "/marcas";
                case motos -> endereco = apiConfig.getUrl() + "/" + OpcaoBuscaFipe.MOTOS.getDescricao() + "/marcas";
                case caminhao -> endereco = apiConfig.getUrl() + "/" + OpcaoBuscaFipe.CAMINHAO.getDescricao() + "/marcas";
                default -> System.out.println("Opcao selecionada: " + opcaoSelecionada + " Inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida, não é um número.");
        }

        var json = consumoApi.obterDados(endereco);
        System.out.println(json);

        var marcas = conversorDados.obterLista(json, Modelo.class);

        marcas.stream()
            .sorted(Comparator.comparing(Modelo::codigo))
            .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: ");
        
        var codigoMarca = scanner.nextLine();    

        endereco += "/" + codigoMarca + "/modelos";

        json = consumoApi.obterDados(endereco);
        var modeloLista = conversorDados.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca: ");
        modeloLista
            .modelos()
            .stream()
            .sorted(Comparator.comparing(Modelo::codigo))
            .forEach(System.out::println);
        
        System.out.println("\nDigite um trecho do nome do carro a ser buscado: ");
        var nomeVeiculo = scanner.nextLine();

        List<Modelo> modelosFiltrados = modeloLista.modelos()
            .stream()
            .filter(modelo -> modelo.nome().toLowerCase().contains(nomeVeiculo))
            .collect(Collectors.toList());

        modelosFiltrados.forEach(System.out::println);    

        System.out.println("\nDigite, por favor, o código do modelo: ");
        var codigoModelo = scanner.nextLine();

        endereco += "/" + codigoModelo + "/anos";

        json = consumoApi.obterDados(endereco);
        List<Modelo> anosModelos = conversorDados.obterLista(json, Modelo.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anosModelos.size(); i++) {
            var enderecoAnos = endereco + "/" + anosModelos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = conversorDados.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);

        return null;
    }

}
