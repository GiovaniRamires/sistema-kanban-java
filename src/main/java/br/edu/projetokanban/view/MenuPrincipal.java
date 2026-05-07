/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projetokanban.view;


import br.edu.projetokanban.model.*;
import br.edu.projetokanban.model.Bug.Severidade;
import br.edu.projetokanban.model.Feature.ValorNegocio;
import br.edu.projetokanban.model.Feature.Complexidade;
import br.edu.projetokanban.model.Tarefa.Status;
import br.edu.projetokanban.service.ProjetoService;
import br.edu.projetokanban.service.TarefaService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private Scanner sc = new Scanner(System.in);
    private ProjetoService projetoService = new ProjetoService();
    private TarefaService tarefaService = new TarefaService();

    public void exibir() {
        int opcao;

        do {
            System.out.println("\n========== SISTEMA KANBAN ==========");
            System.out.println("1 - Gerenciar Projetos");
            System.out.println("2 - Gerenciar Tarefas");
            System.out.println("3 - Relatórios");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1: menuProjetos(); break;
                case 2: menuTarefas(); break;
                case 3: menuRelatorios(); break;
                case 0: System.out.println("Encerrando..."); break;
                default: System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }


    private void menuProjetos() {
        int opcao;

        do {
            System.out.println("\n--- PROJETOS ---");
            System.out.println("1 - Listar projetos");
            System.out.println("2 - Cadastrar projeto");
            System.out.println("3 - Atualizar projeto");
            System.out.println("4 - Excluir projeto");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            try {
                switch (opcao) {
                    case 1: listarProjetos(); break;
                    case 2: cadastrarProjeto(); break;
                    case 3: atualizarProjeto(); break;
                    case 4: excluirProjeto(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    private void listarProjetos() throws SQLException {
    List<Projeto> projetos = projetoService.listarTodos();
    System.out.println("\n========== PROJETOS CADASTRADOS ==========");
    if (projetos.isEmpty()) {
        System.out.println("Nenhum projeto cadastrado.");
    } else {
        for (Projeto p : projetos) {
            System.out.println("  " + p);
        }
    }
    System.out.println("==========================================\n");
}

    private void cadastrarProjeto() throws SQLException {
        System.out.print("Nome do projeto: ");
        String nome = sc.nextLine();
        projetoService.cadastrar(nome);
    }

    private void atualizarProjeto() throws SQLException {
        listarProjetos();
        System.out.print("ID do projeto a atualizar: ");
        int id = lerInt();
        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        projetoService.atualizar(id, nome);
    }

    private void excluirProjeto() throws SQLException {
        listarProjetos();
        System.out.print("ID do projeto a excluir: ");
        int id = lerInt();
        projetoService.excluir(id);
    }


    private void menuTarefas() {
        int opcao;

        do {
            System.out.println("\n--- TAREFAS ---");
            System.out.println("1 - Listar tarefas do projeto");
            System.out.println("2 - Cadastrar Bug");
            System.out.println("3 - Cadastrar Feature");
            System.out.println("4 - Mover status da tarefa");
            System.out.println("5 - Excluir tarefa");
            System.out.println("6 - Ver histórico de eventos");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            try {
                switch (opcao) {
                    case 1: listarTarefas();    break;
                    case 2: cadastrarBug();     break;
                    case 3: cadastrarFeature(); break;
                    case 4: moverStatus();      break;
                    case 5: excluirTarefa();    break;
                    case 6: verEventos();       break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    private void listarTarefas() throws SQLException {
        System.out.print("ID do projeto: ");
        int projetoId = lerInt();
        List<Tarefa> tarefas = tarefaService.listarPorProjeto(projetoId);

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            for (Tarefa t : tarefas) {
                System.out.println(t); // polimorfismo: chama toString() do Bug ou Feature
            }
        }
    }

    private void cadastrarBug() throws SQLException {
        System.out.print("ID do projeto: ");
        int projetoId = lerInt();
        System.out.print("Título: ");
        String titulo = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Responsável: ");
        String responsavel = sc.nextLine();

        System.out.println("Severidade: 1-BAIXA  2-MEDIA  3-ALTA  4-CRITICA");
        System.out.print("Opção: ");
        int sevOpc = lerInt();
        Severidade severidade;
        if (sevOpc == 1)      severidade = Severidade.BAIXA;
        else if (sevOpc == 2) severidade = Severidade.MEDIA;
        else if (sevOpc == 3) severidade = Severidade.ALTA;
        else                  severidade = Severidade.CRITICA;

        System.out.print("É reproduzível? (1-Sim / 2-Não): ");
        boolean reproduzivel = lerInt() == 1;

        tarefaService.cadastrarBug(projetoId, titulo, descricao,
                                   responsavel, severidade, reproduzivel);
    }

    private void cadastrarFeature() throws SQLException {
        System.out.print("ID do projeto: ");
        int projetoId = lerInt();
        System.out.print("Título: ");
        String titulo = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Responsável: ");
        String responsavel = sc.nextLine();

        System.out.println("Valor de negócio: 1-BAIXO  2-MEDIO  3-ALTO");
        System.out.print("Opção: ");
        int valOpc = lerInt();
        ValorNegocio valorNegocio;
        if (valOpc == 1)      valorNegocio = ValorNegocio.BAIXO;
        else if (valOpc == 2) valorNegocio = ValorNegocio.MEDIO;
        else                  valorNegocio = ValorNegocio.ALTO;

        System.out.println("Complexidade: 1-SIMPLES  2-MEDIA  3-COMPLEXA");
        System.out.print("Opção: ");
        int compOpc = lerInt();
        Complexidade complexidade;
        if (compOpc == 1) complexidade = Complexidade.SIMPLES;
        else if (compOpc == 2) complexidade = Complexidade.MEDIA;
        else complexidade = Complexidade.COMPLEXA;

        tarefaService.cadastrarFeature(projetoId, titulo, descricao,
                                       responsavel, valorNegocio, complexidade);
    }

    private void moverStatus() throws SQLException {
        System.out.print("ID da tarefa: ");
        int id = lerInt();

        System.out.println("Novo status: 1-FAZER  2-FAZENDO  3-FEITO");
        System.out.print("Opção: ");
        int stOpc = lerInt();
        Status novoStatus;
        if (stOpc == 1) novoStatus = Status.FAZER;
        else if (stOpc == 2) novoStatus = Status.FAZENDO;
        else novoStatus = Status.FEITO;

        tarefaService.moverStatus(id, novoStatus);
    }

    private void excluirTarefa() throws SQLException {
        System.out.print("ID da tarefa a excluir: ");
        int id = lerInt();
        tarefaService.excluir(id);
    }

    private void verEventos() throws SQLException {
        System.out.print("ID da tarefa: ");
        int id = lerInt();
        tarefaService.listarEventos(id);
    }

    private void menuRelatorios() {
        int opcao;

        do {
            System.out.println("\n--- RELATÓRIOS ---");
            System.out.println("1 - Backlog priorizado");
            System.out.println("2 - Tarefas por responsável");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            try {
                switch (opcao) {
                    case 1: relatorioBacklog();      break;
                    case 2: relatorioResponsavel();  break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    private void relatorioBacklog() throws SQLException {
        List<Tarefa> lista = tarefaService.relatorioBacklog();
        System.out.println("\n===== BACKLOG PRIORIZADO =====");
        if (lista.isEmpty()) {
            System.out.println("Nenhuma tarefa pendente.");
        } else {
            for (Tarefa t : lista) {
                System.out.println(t);
            }
        }
    }

    private void relatorioResponsavel() throws SQLException {
        System.out.print("Nome do responsável: ");
        String nome = sc.nextLine();
        tarefaService.relatorioResponsavel(nome);
    }


    //Leitor de int sem travar o Scanner
    private int lerInt() {
        int valor = 0;
        try {
            valor = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido.");
        }
        return valor;
    }
}
